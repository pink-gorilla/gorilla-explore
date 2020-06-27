(ns pinkgorilla.explore.meta
  (:require
   [taoensso.timbre :refer [debug info error]]
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.notebook.hydration :refer [notebook-load]]
   [pinkgorilla.storage.protocols :refer [determine-encoding]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename decode-storage-using-filename]]
   [pinkgorilla.storage.protocols :refer [create-storage]]))

(defn random-edit-date []
  (fmt/unparse (:date fmt/formatters)
               (-> (rand-int 500) t/days t/ago)))

(defn add-meta [tokens entry]
  (let [;_ (info "adding meta for entry" entry)
        file-info (split-filename (:filename entry))
        format (:encoding file-info)]
    (debug "format: " format)
    (if-not (= format :gorilla)
      entry
      (let [storage (create-storage entry)
            ; notebook (decode encoding-type content)[
            nb (notebook-load storage tokens)
            ;_ (info "loading notebook " storage)
            ]
        (if (nil? nb)
          entry
          (let [meta (if (= (:version nb) 1)
                       {:tags "legacy" :tagline "legacy notebook"}
                       (dissoc (:meta nb) :name :description))] ; ignor previously used :name meta data
            (debug "adding meta data: " meta)
            (assoc entry :meta meta)))))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
