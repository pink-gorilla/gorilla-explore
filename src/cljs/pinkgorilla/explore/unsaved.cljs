(ns pinkgorilla.explore.unsaved
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [pinkgorilla.storage.protocols :refer [storagetype]]
   [pinkgorilla.explorer.notebook-meta :refer [notebook-meta]]))

(defn unsaved? [[storage document]]
  (info "checking: " storage)
  (= (storagetype storage) :unsaved))

(defn add-meta [[storage nb]]
  (->   {:type :unsaved
         :id (:id storage)
         :edit-date ""
         :storage storage
         :filename (str "./" (:id storage) ".cljg")}
        (assoc :meta (notebook-meta nb))))

(defn unsaved-notebooks [db]
  (let [documents (get-in db [:document :documents])
        docs-unsaved (into {} (filter unsaved? documents))
        explorer-unsaved  (into [] (map add-meta docs-unsaved))]
    (info "docs unsaved: " docs-unsaved)
    (info "explorer unsaved: " explorer-unsaved)
    explorer-unsaved))

