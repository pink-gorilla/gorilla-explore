(ns pinkgorilla.explorer.events
  (:require
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   ; side-effects
   [pinkgorilla.document.collection.events]
   [pinkgorilla.document.events]
   [pinkgorilla.explorer.css :refer [components config]]))

(rf/dispatch [:css/add-components components config])

(rf/reg-event-db
 :explorer/init
 (fn [db [_ config-explorer-app]]
   (let [config-explorer (get-in db [:config :explorer :client])
         config-explorer (merge config-explorer-app config-explorer)]
     (info "explorer init ..")
     (rf/dispatch [:explore/init])
     (rf/dispatch [:document/init config-explorer]))
   db))

(rf/reg-event-db
 :oauth2/logged-in
 (fn [db [_ provider]]
   (info "logged in to: " provider)
   (when (= provider :github)
     (info "loading my github repos after github login..")
     (rf/dispatch [:explorer/fetch-my-github]))
   db))