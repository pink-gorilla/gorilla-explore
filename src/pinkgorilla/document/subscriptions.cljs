(ns pinkgorilla.document.subscriptions
  (:require
   [clojure.walk]
   [taoensso.timbre :as timbre :refer [debugf info]]
   [re-frame.core :refer [reg-sub]]
   [pinkgorilla.document.events]))

(reg-sub
 :document/get
 (fn [db [_ storage]]
   (debugf "document storage for: %s" storage)
   (get-in db [:docloader :storages storage])))

(reg-sub
 :document/view
 (fn [db [_ id]]
   (debugf "document view for: %s" id)
   (get-in db [:docs id])))