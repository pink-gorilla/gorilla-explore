(ns demo.routes
  (:require
   #?(:clj [clojure.tools.logging :refer [trace debug info]]
      :cljs [taoensso.timbre :refer-macros [trace debug info]])
   [bidi.bidi :as bidi]
   #?(:cljs [reagent.core :as r])
   #?(:cljs [re-frame.core :refer [dispatch-sync reg-event-db]])
   #?(:clj [bidi.ring])
   #?(:clj [pinkgorilla.document.handler :refer [notebook-load-handler notebook-save-handler]])
   #?(:clj [pinkgorilla.explore.handler :refer [handler-explore-async]])
   #?(:clj [demo.middleware :refer [wrap-api-handler]])))

; see: 
; https://github.com/clj-commons/pushy


(def explorer-routes-ui
  {"explorer"        :ui/explorer
   "notebook"        :ui/notebook
   "edit"            :ui/notebook})   ; edit is here because I didnt want to rewrite encoding  

(def explorer-routes-frontend
  ["/" explorer-routes-ui])

(def explorer-routes-api
  ["" {"/" explorer-routes-ui
       "/api/" {"explorer"  {:get  :api/explorer}
                "notebook"  {:get  :api/notebook-load
                             :post :api/notebook-save}}}])

    ;"section-a"            {"" :section-a
    ;                        ["/item-" :item-id] :a-item}
    ;"section-b"            :section-b
    ;true                   :four-o-four
    ;["" :id]               :bongo      


#?(:cljs
   (do
     (dispatch-sync [:bidi/init explorer-routes-api])
;
     ))

#?(:clj
   (do

     (defn app-html
       [req]
       {:status  200
        :headers {"Content-Type" "text/html; charset=utf-8"}
        :body    (slurp "profiles/demo/public/index.html")})

     (def wrapped-explore-handler
       (wrap-api-handler handler-explore-async))

     (def wrapped-notebook-load-handler
       (wrap-api-handler notebook-load-handler))

     ;TODO : make a unit test with this
     (def demo-load-request
       {:headers
        {"content-type" "application/edn"
         "accept" "application/transit+json"}
        :body nil
        :params {:token nil
                 :storagetype :repo
                 :user "pink-gorilla"
                 :repo "gorilla-ui"
                 :filename "notebooks/videos.cljg"}})

     (comment
         ;TODO : make a unit test with this
       (notebook-load-handler demo-load-request)
       ;
       )

     (def wrapped-notebook-save-handler
       (wrap-api-handler notebook-save-handler))

     (defn route->handler
       ([]
        (info "->handler no args ..")
        nil)
       ([h & args]
        (info "route server explore-handler:" h " args:" args)
        (case  h
          :api/explorer      wrapped-explore-handler
          :api/notebook-load wrapped-notebook-load-handler
          :api/notebook-save wrapped-notebook-save-handler
          :ui/explorer       app-html
          :ui/notebook       app-html
          nil)))

     (def handler
       (bidi.ring/make-handler explorer-routes-api route->handler))))


(comment

  (bidi/path-for explorer-routes-frontend  :ui/explorer)
  (bidi/path-for explorer-routes-frontend  :ui/notebook)
  (bidi/path-for explorer-routes-frontend  :ui/unknown)

  (bidi/path-for explorer-routes-api  :api/explorer)
  (bidi/path-for explorer-routes-api  :api/notebook-load)
  (bidi/path-for explorer-routes-api  :ui/explorer)

  ;
  )