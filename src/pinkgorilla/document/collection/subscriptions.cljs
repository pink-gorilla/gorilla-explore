(ns pinkgorilla.document.collection.subscriptions
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [reg-sub subscribe]]
   [pinkgorilla.document.meta.tags :refer [meta->tags]]
   [pinkgorilla.document.meta.filter :refer [filter-notebooks]]
   [pinkgorilla.document.unsaved :refer [unsaved-notebooks]]))

(reg-sub
 :explorer/config
 (fn [db _]
   (get-in db [:config :explorer])))

(defn notebooks-root [data notebooks root]
  (->> (get data root)
       (map (fn [nb] (assoc nb :root name)))
       (concat notebooks)))

(reg-sub
 :explorer/notebooks-root
 (fn [db _]
   (get-in db [:explorer :notebooks])))

(reg-sub
 :explorer/notebooks-unsaved
 (fn [db _]
   (unsaved-notebooks db)))

(defn notebooks-all [db]
  (let [data (get-in db [:explorer :notebooks])
        roots (keys data)
        notebooks (reduce (partial notebooks-root data) [] roots)
        unsaved (unsaved-notebooks db)]
     ;notebooks
    (concat notebooks unsaved)))

(reg-sub
 :explorer/notebooks-all
 (fn [db _]
   (notebooks-all db)))

(defn notebooks-root-all [db]
  (let [root (get-in db [:explorer :search :root])
        data (get-in db [:explorer :notebooks])]
    (debug "root-all: " root)
    ;(notebooks-all db)
    (case root
      "unsaved" (unsaved-notebooks db)
      "all"     (notebooks-all db)
      nil       (notebooks-all db)
      (get data root))))

(reg-sub
 :explorer/notebooks-root-all
 (fn [db _]
   (notebooks-root-all db)))

(reg-sub
 :explorer/search-options
 (fn [db _]
   (get-in db [:explorer :search])))

(reg-sub
 :explorer/notebooks-filtered
 (fn [_]
   ;; return a vector of subscriptions
   [(subscribe [:explorer/notebooks-root-all])
    (subscribe [:explorer/search-options])])
 (fn [[notebooks-all search-options]]
   (filter-notebooks notebooks-all search-options)))

(reg-sub
 :explorer/tags-available
 (fn [_]
   (subscribe [:explorer/notebooks-filtered]))
 (fn [notebooks-filtered]
   (->> notebooks-filtered
        (mapcat (comp meta->tags :meta))
        distinct
        sort)))

