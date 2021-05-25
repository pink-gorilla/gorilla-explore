(ns pinkgorilla.document.save-dialog.events
  (:require
   [clojure.set]
   [taoensso.timbre :refer-macros [info]]
   [re-frame.core :refer [reg-event-db dispatch]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]
   [pinkgorilla.document.save-dialog.component :refer [save-dialog]]))

;; Save-Dialog Open/Close

(reg-event-db
 :document/save-as
 (fn [db [_ doc-id storage]]
   (info "save-as doc-id: " doc-id)
   (dispatch [:modal/open [save-dialog {:id doc-id
                                        :storage storage
                                        :on-cancel #(dispatch [:modal/close])
                                        :on-save (fn [old storage]
                                                   (dispatch [:document/save-as-storage doc-id storage])
                                                   (dispatch [:modal/close]))}]
              :medium])
   db))

;; Respond to Events from Save-Dialog

(reg-event-db
 :document/save-as-storage
 (fn [db [_ doc-id storage]]
   (let [_ (info "doc save-as id: " doc-id " storage: " storage)]
     (goto-notebook! storage)
     (dispatch [:document/save doc-id storage])
     (assoc-in db [:document :storages storage] {:id doc-id}))))


