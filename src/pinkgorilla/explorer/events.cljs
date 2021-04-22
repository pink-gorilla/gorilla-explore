(ns pinkgorilla.explorer.events
  (:require
   [re-frame.core :refer [reg-event-db dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   ; side-effects
   [pinkgorilla.document.collection.events]
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

(reg-event-db
 :oauth2/logged-in
 (fn [db [_ provider]]
   (info "logged in to: " provider)
   (when (= provider :github)
     (info "loading my github repos after github login..")
     (dispatch [:explorer/fetch-my-github]))
   db))