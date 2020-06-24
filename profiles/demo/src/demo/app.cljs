(ns demo.app
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [cljs.core.async :as async :refer [<! >! chan timeout close!]]
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.explore.default-config]
   ; demo
   [demo.routes :refer [init-routes explorer-routes-api]]
   [demo.views]
   [demo.config :refer [config-client]]
   ))

(enable-console-print!)

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn stop []
  (js/console.log "Stopping..."))


(defn ^:export start []
  (println "demo starting ..")

  (init-routes)
  (dispatch [:explorer/init config-client])
  (dispatch [:documents/init])
  (dispatch [:bidi/init {:api explorer-routes-api}])

  #_(go
      (<! (timeout 7000))
      (info "requesting describe..")
      (dispatch [:nrepl/describe]))

  (reagent.dom/render [demo.views/app]
                      (.getElementById js/document "app")))

