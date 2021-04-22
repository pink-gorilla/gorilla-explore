(ns pinkgorilla.document.meta.tags
  (:require
   [clojure.string :refer [blank? lower-case trim split]]))

; this is used by explorer kernel detection.

(defn tags-csv->list
  [tags-csv]
  (if (blank? tags-csv)
    []
    (map (comp keyword lower-case trim) (split tags-csv #","))))

(defn tags-csv->set [tags-csv]
  (-> tags-csv tags-csv->list set))

(defn notebook-tags [notebook]
  (let [tags-csv (get-in notebook [:meta :tags])]
    (tags-csv->set tags-csv)))

(defn meta->tags [meta]
  (let [tags-csv (:tags meta)]
    (tags-csv->list tags-csv)))
