(ns pinkgorilla.explore.meta
  (:require
   [taoensso.timbre :refer [debug info error]]
   [clojure.string :as str]
   [clojure.set :as set]
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.notebook.hydration :refer [load-notebook]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.explorer.tags :refer [notebook-tags]]))

(defn random-edit-date []
  (fmt/unparse (:date fmt/formatters)
               (-> (rand-int 500) t/days t/ago)))

(defn add-only [kw]
  (let [n (name kw)
        n-only (str n "-only")]
    (keyword n-only)))

(defn kernel-tags [notebook]
  (let [segments (vals (:segments notebook))
        kernels (->> (filter #(:kernel %) segments)
                     (group-by :kernel) ; returns ma, keys = kernel
                     keys)
        ;_ (debug "kernels: " kernels)
        tags (case (count kernels)
               0 #{}
               1 #{(first kernels)
                   (add-only (first kernels))}
               (into #{} kernels))]
    ;(debug "nb kernel tags: " tags)
    tags))

(defn add-kernel-tags [meta notebook]
  (let [k-tags (kernel-tags notebook)]
    (if (empty? k-tags)
      meta
      (let [merged-tags (->> (notebook-tags notebook)
                             (set/union k-tags)
                             (map name)
                             (str/join ","))]
        #_(debug "notebook tags: " (notebook-tags notebook)
                 "merged tags: " merged-tags)
        (assoc meta :tags merged-tags)))))

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
          (let [meta (if (= (:version nb) 1)
                       {:tags "legacy" :tagline "legacy gorilla notebook"}
                       (-> (:meta nb)
                           (dissoc :name :description) ; remove old meta data
                           (add-kernel-tags nb)))]
            (debug "adding meta data: " meta)
            (assoc entry :meta meta)))))))

(defn add-random [tokens entry]
  (assoc entry
         :stars (rand-int 100)
         :edit-date (random-edit-date)))
