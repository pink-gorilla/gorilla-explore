(ns pinkgorilla.document.component
  (:require
   [cemerick.url :as url]
   [clojure.walk]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [reg-sub subscribe dispatch]]
   [pinkgorilla.storage.protocols :refer [storagetype query-params-to-storage gorilla-path storageformat]]))


(defn query-params []
  (println "href: " (.. js/window -location -href))
  (println "query: " (-> (.. js/window -location -href)
                         (url/url)
                         ;:query
                         (url/query->map)))
  (-> (.. js/window -location -href)
      (url/url)
      :query))

(reg-sub
 :document/get
 (fn [db storage]
   (get-in db [:documents storage])))



(defn err [reason]
  [:div
   [:h1 "Error loading document!"]
   [:p "Reason"]
   [:p (pr-str reason)]])

(defn notebook-page [notebook-view]
  (let [params  (query-params)
        kparams (clojure.walk/keywordize-keys params)
        _(info "kw params: " kparams)
        stype (keyword (:source kparams))
        storage (query-params-to-storage stype kparams)
        document (if storage (subscribe [:document/get storage]))]
    (println "e/nb! qp: " params)
    (when (and storage (not @document))
      (info "loading document storage: " storage)
      (dispatch [:document/load storage]))

    [:div
     [:h1 "Notebook Viewer"]
     [:p (pr-str params)]
     (cond
       (not storage)
       [err "storage parameter are bad!"]

       (not @document)
       [err "requesting document"]

       (= :document/loading @document)
       [err "loading .."]

       (:error @document)
       [err "Document could not be loaded! " (pr-str (:error @document))]

       :else
       [notebook-view document])]))





