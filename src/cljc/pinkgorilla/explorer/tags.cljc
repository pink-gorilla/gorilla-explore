(ns pinkgorilla.explorer.tags
  (:require
   [clojure.string :refer [blank? lower-case trim split]]))

; this is used by explorer kernel detection.
; frontend still uses old meta/tags

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

