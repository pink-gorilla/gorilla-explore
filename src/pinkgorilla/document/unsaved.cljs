(ns pinkgorilla.document.unsaved
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [pinkgorilla.storage.protocols :refer [storagetype]]
   [pinkgorilla.document.meta.notebook :refer [notebook-meta]]))

(defn unsaved? [[id document]]
  ;(info "checking: " storage)
  (nil? (:storage document)))

(defn add-meta [[storage nb]]
  (->   {:type :unsaved
         :id (get-in nb [:meta :id])
         :edit-date ""
         :storage nil
         :filename (str "./unsaved.cljg")}
        (assoc :meta (notebook-meta nb))))

(defn unsaved-notebooks [db]
  (let [documents (get-in db [:docs])
        docs-unsaved (into {} (filter unsaved? documents))
        explorer-unsaved  (into [] (map add-meta docs-unsaved))]
    ;(info "docs unsaved: " docs-unsaved)
    ;(info "explorer unsaved: " explorer-unsaved)
    explorer-unsaved))

