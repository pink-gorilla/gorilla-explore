(ns pinkgorilla.document.meta.meta
  (:require
   [taoensso.timbre :refer [debug info error]]
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.notebook.persistence :refer [load-notebook]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.document.meta.notebook :refer [notebook-meta]]))

(defn random-edit-date []
  (fmt/unparse (:date fmt/formatters)
               (-> (rand-int 500) t/days t/ago)))

; from resources
#_(defn get-meta [entry]
    (let [tokens {}
          storage (create-storage entry)
          nb (->> (storage-load storage tokens)
                  (decode :gorilla))]
      (select-keys nb [:meta])))

(defn add-meta [tokens entry]
  (debug "adding meta for entry" entry)
  (let [file-info (split-filename (:filename entry))
        format (:encoding file-info)]
    ;(debug "format: " format)
    (if-not (= format :gorilla)
      entry
      (let [storage (create-storage entry)
            ; notebook (decode encoding-type content)[
            nb (load-notebook storage tokens)
            ;_ (info "loading notebook " storage)
            ]
        (if (nil? nb)
          (assoc entry :meta {:tags "error"})
          (assoc entry :meta (notebook-meta nb)))))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
