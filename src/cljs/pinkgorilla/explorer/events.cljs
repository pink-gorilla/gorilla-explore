(ns pinkgorilla.explorer.events
  (:require
   [re-frame.core :refer [reg-event-fx trim-v dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.explore.events] ; side-effects
   [pinkgorilla.document.events] ; side-effects
)) ; side effects


(reg-event-fx
 :explorer/init
 (fn [_ [_ config]]
   (info "explorer init ..")
   (dispatch [:explore/init config])
   (dispatch [:document/init config])))