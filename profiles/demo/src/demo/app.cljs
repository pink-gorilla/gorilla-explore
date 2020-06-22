(ns demo.app
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [cljs.core.async :as async :refer [<! >! chan timeout close!]]
   [re-frame.core :refer [dispatch]]
   ; demo
   [demo.routes]
   [demo.views]
   [demo.config :refer [config-client]]
   [pinkgorilla.explore.default-config]
   ))

(enable-console-print!)

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn stop []
  (js/console.log "Stopping..."))


(defn ^:export start []
  (println "demo starting ..")

  (dispatch [:explorer/init config-client])
  ;(dispatch [:nrepl/init "ws://localhost:9000/nrepl"])

  #_(go
      (<! (timeout 7000))
      (info "requesting describe..")
      (dispatch [:nrepl/describe]))

  (reagent.dom/render [demo.views/app]
                      (.getElementById js/document "app")))

