(ns pinkgorilla.bidi
  (:require
   [cemerick.url :as url]
   [reagent.core :as r]
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [taoensso.timbre :refer-macros [debug info error]]
   [bidi.bidi :as bidi]
   [pushy.core :as pushy]
   [pinkgorilla.storage.protocols :refer [gorilla-path]]))

; query param handling
; bidi does not handle query params
; idea how to solve the problem:
; https://github.com/juxt/bidi/issues/51
 #_(defn match-route-with-query-params
    [route path & {:as options}]
    (let [query-params (->> (:query (cemerick.url/url path))
                            (map (fn [[k v]] [(keyword k) v]))
                            (into {}))]
      (-> (bidi/match-route* route path options)
          (assoc :query-params query-params))))
; bidi swagger:
; https://github.com/pink-junkjard/bidi-swagger

(defn window-query-params []
  (info "window query params: href: " (.. js/window -location -href))
  (-> (.. js/window -location -href)
      (url/url)
      ; (url/query->map)
      :query))

(def query-params (r/atom nil))

(defn set-query-params [params]
  (info "setting query params to: " params)
  (reset! query-params params))

(defn set-initial-query-params []
  (let [params (window-query-params)]
    (set-query-params params)))

; bidi routing

(def current (r/atom nil))
(def routes (r/atom nil))

(defn bidi-goto! [match]
  (info "setting page to: " match)
  (reset! current match))

(defn on-url-change [path & options]
  (let [options (or options {})]
    (info "url did change to: " path " options:" options)
    (bidi/match-route @routes path))) ; options

; see: 
; https://github.com/clj-commons/pushy

(def history
  (pushy/pushy bidi-goto! on-url-change))

(reg-event-db
 :bidi/init
 (fn [db [_ new-routes]]
   (info "bidi init ..")
   (reset! routes new-routes)
   (info "bidi routes are: " new-routes)
   (let [db (or db {})]
     (info "starting pushy")
     (pushy/start! history) ; link url => state
     (set-initial-query-params)
     db)))

(defn link [handler]
  (info "link for: " handler)
  (let [url (bidi/path-for @routes handler)]
    (info "bidi link url: " url)
    url))

(defn nav! [url]
  (set! (.-location js/window) url))

(defn goto! [handler & params]
  (let [[qp] params]
    (info "goto! handler: " handler " query-params: " qp)
    (reset! current {:handler handler})
    (if qp
      (let [url (str (link handler) "?" (url/map->query qp))]
        (set-query-params qp)
        (pushy/set-token! history url))
      (pushy/set-token! history (link handler)))))

(defn subs2 [s start]
  (.substring s start (count s)))

(defn goto-notebook! [notebook]
  (let [storage (:storage notebook)
        query-params (gorilla-path storage)
        query-params (url/query->map (subs2 query-params 1))
        _ (info "query params: " query-params)]
    (goto! :ui/notebook query-params)))


(comment

  (link :ui/notebook)
;
  )


