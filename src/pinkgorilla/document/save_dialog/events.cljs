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
 (fn [db [_ storage]]
   (dispatch [:modal/open [save-dialog {:storage storage
                                        :on-cancel #(dispatch [:modal/close])
                                        :on-save (fn [old new]
                                                   (dispatch [:document/save-as-storage old new])
                                                   (dispatch [:modal/close]))}]
              :medium])
   db))

;; Respond to Events from Save-Dialog

(reg-event-db
 :document/save-as-storage
 (fn [db [_ storage-old storage-new]]
   (let [_ (info "saving storage " storage-old " to: " storage-new)
         documents (get-in db [:document :documents])]
     (goto-notebook! storage-new)
     (dispatch [:document/save storage-new])
     (assoc-in db [:document :documents]
               (clojure.set/rename-keys documents {storage-old storage-new})))))


