(ns demo.app
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.build :refer [build-cli]]
   [webly.config :refer [webly-config]]
   [webly.web.handler :refer [make-handler]]
   [pinkgorilla.explorer.default-config :refer [config-server]] ; side effects
   [pinkgorilla.explorer.handler] ; side-effects   
   [pinkgorilla.explore.handler :refer [explore-directories-start]]
   [demo.routes :refer [demo-routes-backend demo-routes-frontend]]))

(info "making handler ..")
(def handler (make-handler demo-routes-backend demo-routes-frontend))

(defn -main
  [mode]
  (info "demo starting mode: " mode)
  (swap! webly-config assoc :timbre-loglevel :info)
  (swap! webly-config assoc :title "notebook-explorer")
  (swap! webly-config assoc :start "demo.app.start (); ")

  (explore-directories-start config-server)

  (build-cli mode "+demo" "demo.app/handler" "demo.app"))