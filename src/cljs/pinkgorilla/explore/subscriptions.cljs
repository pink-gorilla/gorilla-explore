(ns pinkgorilla.explore.subscriptions
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [reg-sub subscribe]]
   [pinkgorilla.meta.tags :refer [meta->tags]]
   [pinkgorilla.meta.filter :refer [filter-notebooks]]
   [pinkgorilla.explore.unsaved :refer [unsaved-notebooks]]))

(reg-sub
 :explorer/config
 (fn [db _]
   (get-in db [:explorer :config])))

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

(reg-sub
 :explorer/notebooks-all
 (fn [db _]
   (let [data (get-in db [:explorer :notebooks])
         roots (keys data)
         notebooks (reduce (partial notebooks-root data) [] roots)
         unsaved (unsaved-notebooks db)]
     (info "explorer notebooks: " notebooks)
     notebooks
     (concat notebooks unsaved))))

(reg-sub
 :explorer/search-options
 (fn [db _]
   (get-in db [:explorer :search])))

(reg-sub
 :explorer/notebooks-filtered
 (fn [_]
   ;; return a vector of subscriptions
   [(subscribe [:explorer/notebooks-all])
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

