(ns demo.routes
  (:require
   [taoensso.timbre :as timbre :refer [trace debug info warn error]]
   [bidi.bidi :as bidi]
   #?(:clj [bidi.ring])
   #?(:cljs [pinkgorilla.explorer.default-config :refer [explorer-routes-ui explorer-routes-api]]
      :clj [pinkgorilla.explorer.default-config :refer [explorer-routes-ui explorer-routes-api
                                                        explore-handler notebook-load-handler notebook-save-handler]])))

(def demo-routes-ui
  (assoc explorer-routes-ui
         "" :demo/main
         "demo/save" :demo/save))

(def demo-routes-frontend
  ["/" demo-routes-ui])

(def demo-routes-api
  ["" {"/" demo-routes-ui
       "" explorer-routes-api}])



#?(:clj
   (do

     (defn app-html
       [req]
       {:status  200
        :headers {"Content-Type" "text/html; charset=utf-8"}
        :body    (slurp "profiles/demo/public/index.html")})


     (defn route->handler
       ([]
        (info "->handler no args ..")
        nil)
       ([h & args]
        (info "route server explore-handler:" h " args:" args)
        (case  h
          :api/explorer      explore-handler
          :api/notebook-load notebook-load-handler
          :api/notebook-save notebook-save-handler
          :ui/explorer       app-html
          :ui/notebook       app-html
          :demo/main         app-html
          :demo/save         app-html
          nil)))

; handler is used by shadow-cljs
     (def handler
       (bidi.ring/make-handler demo-routes-api route->handler))

     (comment
       (bidi/path-for demo-routes-api :demo/main)
       (bidi/path-for demo-routes-api :ui/explorer)
       (bidi/path-for demo-routes-api :ui/notebook)
       (bidi/path-for demo-routes-api :ui/unknown)

       (bidi/path-for demo-routes-api :api/explorer)
       (bidi/path-for demo-routes-api  :api/notebook-load)

       (bidi.bidi/match-route demo-routes-api "/explorer")
       (bidi.bidi/match-route demo-routes-api "/api/explorer" :request-method :get)

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

       ;TODO : make a unit test with this
       (notebook-load-handler demo-load-request)

       ;
       )

; clj
     ))
