(ns pinkgorilla.document.subscriptions
  (:require
   [clojure.walk]
   [taoensso.timbre :as timbre :refer [debug info]]
   [re-frame.core :refer [reg-sub dispatch]]
   [pinkgorilla.document.events]))

(reg-sub
 :document/get
 (fn [db [_ storage]]
   (info "document view for: " storage)
   (get-in db [:document :documents storage])))