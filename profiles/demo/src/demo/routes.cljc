(ns demo.routes
  (:require
   [pinkgorilla.explorer.default-config :as explorer]))

(def routes-api
  explorer/routes-api)

(def routes-app
  (assoc explorer/routes-app
         "" :demo/main
         "demo/save" :demo/save))






