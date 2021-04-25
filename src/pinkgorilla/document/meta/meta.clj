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

(defn load-meta [tokens storage]
  (let [nb (load-notebook storage tokens)]
    (if nb
      (notebook-meta nb)
      (do (error "meta for storage could not be loaded: " storage)
          {:tags "nb-load-failed"}))))

(defn add-meta [tokens entry]
  (debug "adding meta for entry" entry)
  (let [file-info (split-filename (:filename entry))
        format (:encoding file-info)]
    (if (= format :gorilla)
      (let [storage (create-storage entry)]
        (assoc entry :meta (load-meta tokens storage)))
      (assoc entry :meta {:tags "no-gorilla"}))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
