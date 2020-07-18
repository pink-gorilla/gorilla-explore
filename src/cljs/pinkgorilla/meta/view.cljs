(ns pinkgorilla.meta.view
  (:require
   [pinkgorilla.meta.tags :refer [tag-box meta->tags]]))

(defn tag-box-meta [meta]
  [:div
   [:link {:rel "stylesheet" :href "/r/explorer/explorer.css"}]
   [tag-box (meta->tags meta) #{}]])

(defn tagline [meta]
  (let [tl (:tagline meta)]
    (or tl "No Tagline provided")))



