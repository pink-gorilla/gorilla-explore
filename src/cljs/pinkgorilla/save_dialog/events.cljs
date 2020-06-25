(ns pinkgorilla.save-dialog.events
  (:require
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [pinkgorilla.storage.storage :refer [query-params-to-storage gorilla-path]]))


;; Save-Dialog Open/Close

(reg-event-db
 :app:saveas
 (fn [db _]
   (assoc-in db [:dialog :save] true)))

(reg-event-db
 :save-dialog-cancel
 (fn [db _]
   (assoc-in db [:dialog :save] false)))

;; Respond to Events from Save-Dialog

; change storage settings
(reg-event-db
 :save-as-storage
 (fn [db [_ params]]
   (let [storage-type (keyword (:source params))
         _ (info "saving as storage type: " storage-type)
         storage (query-params-to-storage storage-type params)]
     (assoc-in db [:storage] storage))))

; navigate to current storage settings
#_(reg-event-db
 :nav-to-storage
 [standard-interceptors]
 (fn [db [_ new-worksheet?]]
   (let [storage (:storage db)]
     (if new-worksheet?
       (routes/nav! "/new")
       (if (nil? storage)
         (routes/nav! "/edit")
         (routes/set-hash! (gorilla-path storage)))) ; 
     db)))





