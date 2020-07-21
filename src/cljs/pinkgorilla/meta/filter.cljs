(ns pinkgorilla.meta.filter
  (:require
   [clojure.string :as str]
   [pinkgorilla.meta.tags :refer [notebook-tags->set]]))

(defn- filter-notebooks-text
  [notebooks text]
  (if (empty? text)
    notebooks
    (let [text (str/lower-case text)]
      (filter (fn [notebook]
                (not= -1
                      (.indexOf (->> (vals notebook)
                                     (filter string?)
                                     (str/join " ")
                                     (str/lower-case))
                                text)))
              notebooks))))

(defn- filter-notebooks-tags
  [notebooks tags-set]
  (if (empty? tags-set)
    notebooks
    (filter #(every? (notebook-tags->set %) tags-set)
            notebooks)))

(defn filter-notebooks [notebooks-all search-options]
  (let [{:keys [tags text]} search-options]
    (-> notebooks-all
        (filter-notebooks-text text)
        (filter-notebooks-tags tags))))