(ns pinkgorilla.explore.meta
  (:require
   [taoensso.timbre :refer [debug info error]]
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.notebook.persistence :refer [load-notebook]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.explorer.notebook-meta :refer [notebook-meta]]))

(defn random-edit-date []
  (fmt/unparse (:date fmt/formatters)
               (-> (rand-int 500) t/days t/ago)))

(defn add-meta [tokens entry]
  (let [;_ (info "adding meta for entry" entry)
        file-info (split-filename (:filename entry))
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
          entry
          (assoc entry :meta (notebook-meta nb)))))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
