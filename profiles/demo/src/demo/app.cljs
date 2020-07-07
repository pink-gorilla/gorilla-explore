(ns demo.app
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :refer [dispatch-sync]]
   [pinkgorilla.ui.config :refer [set-prefix!]]
   [pinkgorilla.explorer.default-config :refer [config-client]] ;; side-effects
   [demo.routes :refer [demo-routes-api]] ; side effects
   [demo.views :refer [app]]))

(enable-console-print!)

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(set-prefix! "/")

(defn stop []
  (info "demo Stopping..."))

(defn ^:export start []
  (info "demo starting ..")
  (dispatch-sync [:bidi/init demo-routes-api])
  (dispatch-sync [:explorer/init config-client])
  (reagent.dom/render [app]
                      (.getElementById js/document "app")))

