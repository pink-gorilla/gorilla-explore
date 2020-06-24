(ns pinkgorilla.meta.view
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [storagetype]]
   [pinkgorilla.meta.tags :refer [tag-box meta->tags]]))

#_(defn notebook-page [& args]
  (println "e/nb! args: " args)
  [:h1 "Notebook Viewer"])

(defn tag-box-meta [meta]
  [tag-box (meta->tags meta) #{}])

(defn tagline [meta]
  (let [tl (:tagline meta)]
    (or tl "No Tagline provided")))

(defn header [file-info meta storage]
  [:div.bg-blue-100.flex.flex.col.justify-between
     ;top-row
   [:div
    [:span.m-2.p-1.border.border-solid (storagetype storage)]    
    [:span.m-2.text-xl (:name file-info)]
    [:span.m-2.italic (or (:tagline meta) "No tagline entered...")]]
     ;second row
   [:div.ml.5
    [:div.inline-block.m-1 [tag-box-meta meta]]
    [:span.p-2 (:path file-info)]]])

(defn document-view-meta [storage document document-view]
  (let [d @document
        meta (:meta d)
        file-info (split-filename (:filename storage))
        _ (debug "nb file-info: " file-info)]
    [:div
     [header file-info meta storage]
     [document-view storage document]]))

