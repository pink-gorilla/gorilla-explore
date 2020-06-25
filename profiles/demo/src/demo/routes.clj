(ns demo.routes
  (:require
   ;#?(:clj 
   [clojure.tools.logging :refer [trace debug info]]
    ;  :cljs [taoensso.timbre :refer-macros [trace debug info]])
  ; [bidi.bidi :as bidi]
  ; #?(:cljs [reagent.core :as r])
  ; #?(:cljs [re-frame.core :refer [dispatch-sync reg-event-db]])
   [bidi.ring]
   [pinkgorilla.document.handler :refer [notebook-load-handler notebook-save-handler]]
   [pinkgorilla.explore.handler :refer [handler-explore-async]]
   [pinkgorilla.explore.default-config :refer [explorer-routes-api]]
   [demo.middleware :refer [wrap-api-handler]]))

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
     :demo/save         app-html
     nil)))

; handler is used by shadow-cljs
(def handler
  (bidi.ring/make-handler explorer-routes-api route->handler))

