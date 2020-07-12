(ns demo.app
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :refer [dispatch-sync]]   
   [webly.web.app]
   [webly.config :refer [webly-config]]
   [pinkgorilla.explorer.default-config :refer [config-client]] ;; side-effects
   [demo.routes :refer [demo-routes-backend]]
   [demo.views] ; side-effects
   ))

(defn ^:export start []
  (swap! webly-config assoc :timbre-loglevel :debug)
  (info "explorer demo starting ..")
  (dispatch-sync [:explorer/init config-client])
  (webly.web.app/start demo-routes-backend)
  (webly.web.app/mount-app))



