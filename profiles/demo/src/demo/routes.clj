(ns demo.routes
  (:require
   [clojure.tools.logging :refer [trace debug info]]
   [bidi.ring]
   [pinkgorilla.explorer.default-config
    :refer [explorer-routes-api
            explore-handler
            notebook-load-handler
            notebook-save-handler]]))

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
     :demo/save         app-html
     nil)))

; handler is used by shadow-cljs
(def handler
  (bidi.ring/make-handler explorer-routes-api route->handler))

