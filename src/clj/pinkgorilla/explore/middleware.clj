(ns pinkgorilla.explore.middleware
  (:require
   [taoensso.timbre :as timbre :refer-macros [tracef debugf infof warnf errorf info]]
   ;[ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [ring.middleware.params :refer [wrap-params]]
   ;[ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
   ;[ring.middleware.format :refer [wrap-restful-format]]
   [muuntaja.middleware :refer [wrap-format]]
   #_[ring.middleware.json :refer [wrap-json-response]]))

(defn wrap-api-handler
  "a wrapper for JSON API calls"
  [handler]
  (-> handler ; middlewares execute from bottom -> up
      ;(wrap-defaults api-defaults)
      (wrap-keyword-params)
      (wrap-params)
      (wrap-format) ; muuntaja
      #_(wrap-restful-format :formats [:json
                                     ;:json-kw 
                                       :transit-json
                                       :edn])
      ;(wrap-json-response)
      ;(wrap-gzip)
      ))

