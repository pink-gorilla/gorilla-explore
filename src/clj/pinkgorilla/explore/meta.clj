(ns pinkgorilla.explore.meta
  (:require
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.notebook.hydration :refer [notebook-load]]
   [pinkgorilla.storage.protocols :refer [create-storage storageformat]]))

(defn random-edit-date []
  (fmt/unparse (:date fmt/formatters)
               (-> (rand-int 500) t/days t/ago)))

(defn add-meta [tokens entry]
  (let [;_ (println "adding meta for entry" entry)
        storage (create-storage entry)
        format (storageformat storage)]
    (case format
      :jupyter (assoc entry :meta {:tags "jupyter" :tagline "jupyter notebook"})
      :gorilla (let [;_ (println "loading notebook " storage)
                     nb (notebook-load storage tokens)]
                 (if (nil? nb)
                   entry
                   (let [meta (if (= (:version nb) 1)
                                {:tags "legacy" :tagline "legacy notebook"}
                                (:meta nb))]
                     (assoc entry :meta meta))))
      (do (println "unknown storage format for:" format "for storage: " storage)
          entry))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
