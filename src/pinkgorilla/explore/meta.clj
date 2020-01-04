(ns pinkgorilla.explore.meta
  (:require
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.notebook.core :refer [notebook-load]]
   [pinkgorilla.storage.storage :refer [create-storage]]))

(defn random-edit-date []
  (fmt/unparse (:date fmt/formatters)
               (-> (rand-int 500) t/days t/ago)))

(defn add-meta [tokens entry]
  (let [_ (println "adding meta for entry" entry)
        storage (create-storage entry)
        ;tokens {}
        _ (println "loading notebook " storage)
        nb (notebook-load storage tokens)
        ;_ (println "notebook loaded!")
        meta (:meta nb)
        meta (if (= (:version nb) 1)
               {:tags "legacy" :tagline "legacy notebook"}
               meta)]
    (if (nil? nb)
      nil
      (assoc entry
             :meta meta))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
