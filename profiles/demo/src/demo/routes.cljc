(ns demo.routes
  (:require
   [webly.web.resources :refer [resource-handler]]
   [pinkgorilla.explorer.default-config :refer [explorer-routes-app explorer-routes-api]]))

(def demo-routes-app
  (assoc explorer-routes-app
         "" :demo/main
         "demo/save" :demo/save))

(def demo-routes-frontend
  ["/" demo-routes-app])

(def demo-routes-backend
  ["/" {"" demo-routes-app
        "api/" explorer-routes-api
        "r" resource-handler}])




