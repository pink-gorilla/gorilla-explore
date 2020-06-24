(ns pinkgorilla.meta.view
  (:require 
   [pinkgorilla.meta.tags :refer [tag-box meta->tags]]))


(defn notebook-page [& args]
  (println "e/nb! args: " args)
  [:h1 "Notebook Viewer"])

(defn tag-box-meta [meta]
  [tag-box (meta->tags meta) #{}])

(defn tagline [meta]
  (let [tl (:tagline meta)]
    (or tl "No Tagline provided")))

(defn document-view-meta [document]
  (let [d @document
        meta (:meta d)
        ]
    [:div
     [:h1 "Document Meta Info"]

     [:p " tags: " (pr-str (:tags meta))]
     [tag-box-meta meta]
     [:p " tagline: " (pr-str (:tagline meta))]
     [:hr]
     [:p " meta full: " (pr-str meta)]]))