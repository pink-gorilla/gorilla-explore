(ns demo.events
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [re-frame.core :refer [reg-event-db dispatch]]))

(def config-hydration {})

(reg-event-db
 :demo/start
 (fn [db [_]]
   (info "explorer demo starting ..")
   (dispatch [:explorer/init config-hydration])
   db))