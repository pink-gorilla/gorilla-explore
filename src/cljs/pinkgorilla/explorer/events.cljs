(ns pinkgorilla.explorer.events
  (:require
   [re-frame.core :refer [reg-event-db dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   ; side-effects
   [pinkgorilla.explore.events]
   [pinkgorilla.document.events]))

(reg-event-db
 :explorer/init
 (fn [db [_ config-explorer-app]]
   (let [config-explorer (get-in db [:config :explorer :client])
         config-explorer (merge config-explorer-app config-explorer)]
     (info "explorer init ..")
     (dispatch [:explore/init])
     (dispatch [:document/init config-explorer]))
   db))