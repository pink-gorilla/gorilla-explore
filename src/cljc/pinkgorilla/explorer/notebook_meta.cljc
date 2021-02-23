(ns pinkgorilla.explorer.notebook-meta
  (:require
   [clojure.string :as str]
   [clojure.set :as set]
   [taoensso.timbre :refer [debug info error]]
   [pinkgorilla.explorer.tags :refer [notebook-tags]]))

(defn add-only [kw]
  (let [n (name kw)
        n-only (str n "-only")]
    (keyword n-only)))

(defn kernel-tags [notebook]
  (let [segments (:segments notebook)
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

(defn notebook-meta [nb]
  (let [meta (if (= (:version nb) 1)
               {:tags "legacy" :tagline "legacy gorilla notebook"}
               (-> (:meta nb)
                   (dissoc :name :description) ; remove old meta data
                   (add-kernel-tags nb)))]
    meta))