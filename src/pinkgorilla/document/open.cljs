(ns pinkgorilla.document.open
  (:require
   [taoensso.timbre :refer-macros [debug info warn error]]
   [pinkgorilla.storage.protocols :refer [storage->map storagetype]]
   [pinkgorilla.document.meta.notebook :refer [notebook-meta]]))

(defn unsaved? [[id document]]
  ;(info "checking: " storage)
  (nil? (:storage document)))

(defn add-nb [acc [k v]]
  (assoc acc (:id v) k))

(defn open-ids [db]
  (let [s (get-in db [:docloader :storages])
        x (reduce add-nb {} s)]
    (warn "x: " x)
    x))

(defn add-meta [open-ids [storage nb]]
  (let [id (get-in nb [:meta :id])
        s (get open-ids id)
        sm (if s
             (storage->map s)
             {:type :unsaved
              :filename (str "./unsaved.cljg")})
        s (or s {:id id})]
    (warn "storage for " id ": " s)
    (merge sm
           {:id id
            :edit-date ""
            :storage s
            :meta (notebook-meta nb)})))

(defn open-notebooks [db]
  (let [docs (get-in db [:docs])
        open-ids  (open-ids db)
        ;docs-unsaved (into {} (filter unsaved? docs))
        open  (into [] (map (partial add-meta open-ids) docs))]

    ;(info "docs unsaved: " docs-unsaved)
    ;(info "explorer unsaved: " explorer-unsaved)
    open))

