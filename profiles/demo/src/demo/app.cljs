(ns demo.app
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :refer [dispatch dispatch-sync]]
   [pinkgorilla.explorer.default-config :refer [config-client]] ;; side-effects
   [demo.routes :refer [demo-routes-api]] ; side effects
   [demo.views]))

(enable-console-print!)

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn stop []
  (info "demo Stopping..."))

(defn ^:export start []
  (info "demo starting ..")
  (dispatch-sync [:bidi/init demo-routes-api])
  (dispatch-sync [:explorer/init config-client])
  (reagent.dom/render [demo.views/app]
                      (.getElementById js/document "app")))

