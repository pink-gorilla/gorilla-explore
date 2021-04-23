(ns pinkgorilla.document.subscriptions
  (:require
   [clojure.walk]
   [taoensso.timbre :as timbre :refer [debugf info]]
   [re-frame.core :refer [reg-sub]]
   [pinkgorilla.document.events]))

(reg-sub
 :document/get
 (fn [db [_ storage]]
   (debugf "document view for: %s" storage)
   (get-in db [:document :documents storage])))