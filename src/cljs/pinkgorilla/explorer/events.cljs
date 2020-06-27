(ns pinkgorilla.explorer.events
  (:require
   [re-frame.core :refer [reg-event-fx trim-v dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.explore.events] ; side-effects
   [pinkgorilla.document.events] ; side-effects
)) ; side effects


(reg-event-fx
 :explorer/init
 (fn [_ [_ config explorer-routes-api]]
   (info "explorer init ..")
   (dispatch [:explore/init config])
   (dispatch [:document/init explorer-routes-api])))