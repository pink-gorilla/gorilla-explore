(ns demo.app
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :refer [dispatch dispatch-sync]]
   [pinkgorilla.explore.default-config :refer [explorer-routes-api]] ;; side-effects
   ; demo
   [demo.views]
   [demo.config :refer [config-client]]))

(enable-console-print!)

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn stop []
  (info "demo Stopping..."))

(defn ^:export start []
  (info "demo starting ..")
  (dispatch-sync [:bidi/init explorer-routes-api])
  (dispatch [:explorer/init config-client])
  (dispatch [:documents/init])
  (reagent.dom/render [demo.views/app]
                      (.getElementById js/document "app")))

