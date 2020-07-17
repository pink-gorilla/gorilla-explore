(ns pinkgorilla.explore.subscriptions
  (:require
   [re-frame.core :refer [reg-sub subscribe]]
   [pinkgorilla.meta.tags :refer [meta->tags]]
   [pinkgorilla.meta.filter :refer [filter-notebooks]]))

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
 :explorer/notebooks-all
 (fn [db _]
   (let [data (get-in db [:explorer :notebooks])
         roots (keys data)]
     (reduce (partial notebooks-root data) [] roots))))

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

