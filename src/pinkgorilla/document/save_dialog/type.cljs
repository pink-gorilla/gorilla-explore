(ns pinkgorilla.document.save-dialog.type
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [re-com.core :refer [input-text radio-button]]))

(defn storage-type [state change!]
  [:div.m-2.w-16
   [:p "Storage"]
   [radio-button
    :label       "file"
    :value       :file
    :model       (:storage-type @state)
    :on-change   #(change! :storage-type %)]
   [radio-button
    :label       "gist"
    :value       :gist
    :model       (:storage-type @state)
    :on-change   #(change! :storage-type %)]
   [radio-button
    :label       "repo"
    :value       :repo
    :model       (:storage-type @state)
    :on-change   #(change! :storage-type %)]])


