(ns demo.routes
  (:require
   [taoensso.timbre :as timbre :refer-macros [tracef debugf infof warnf errorf info]]
   [bidi.bidi :as bidi]
   #?(:cljs [re-frame.core :refer [dispatch]])
   #?(:cljs [pushy.core :as pushy])
   #?(:clj [bidi.ring])
   #?(:clj [demo.explore-handler :refer [handler-explore-async]])
   #?(:clj [demo.middleware :refer [wrap-api-handler]])))

; see: 
; https://github.com/clj-commons/pushy

#_(defn page []
    (fn []
      (let [page (:current-page (session/get :route))]
        [:div
         [:p [:a {:href (bidi/path-for app-routes :index)} "Go home"]]
         [:hr]
         (page-contents page) ;;
         [:hr]
         [:p "(Using "
          [:a {:href "https://reagent-project.github.io/"} "Reagent"] ", "
          [:a {:href "https://github.com/juxt/bidi"} "Bidi"] " & "
          [:a {:href "https://github.com/venantius/accountant"} "Accountant"]
          ")"]])))

(def explorer-routes
  ["/" {"explorer" {"/index"      :explorer/index
                    ["/nb"]       :explorer/nb
    ;"section-a"            {"" :section-a
    ;                        ["/item-" :item-id] :a-item}
    ;"section-b"            :section-b
    ;true                   :four-o-four
    ;["" :id]               :bongo
                    }
        ; edit is here because I didnt want to rewrite encoding
        "edit"  :explorer/nb}]) 


#?(:cljs
   (do
     (defn set-page! [match]
       (info "setting page to: " match)
       (dispatch [:goldly/nav match]))

     (def history
       (pushy/pushy set-page! (partial bidi/match-route explorer-routes)))

     (defn init-routes []
       (info "starting pushy")
       (pushy/start! history))))

#?(:clj
   (do
     (defn notebook-handler [req & args]
       (println "e/nb!  qs:" (:query-string req))
       (println "e/nb! req: " req)
       (println "e/nb! args: " args)
       {:body (str "<h1> nb : " (:query-string req) "</h1>")
        :status 200})

     (def wrapped-explore-handler
       (wrap-api-handler handler-explore-async))

     (defn route->handler
       ([]
        (println "->handler no args ..")
        nil)
       ([h & args]
        (println "h:" h " args:" args)
;       (println "r:" r)
        (case h
          :explorer/index wrapped-explore-handler
          :explorer/nb notebook-handler ;args)
          nil)))


     (def handler
       (bidi.ring/make-handler explorer-routes route->handler))))