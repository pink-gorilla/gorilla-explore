(ns pinkgorilla.document.save-dialog.format
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [reagent.core :as r]
   [re-frame.core :refer [subscribe]]
   [re-com.core :refer [input-text radio-button]]))

(defn storage-format [state change!]
  [:div.m-2.w-24
   [:p "Format"]
   [radio-button
    :label       "gorilla"
    :value       :gorilla
    :model       (:format @state)
    :on-change   #(change! :format %)]
   [radio-button
    :label       "jupyter"
    :value       :jupyter
    :model       (:format @state)
    :on-change   #(change! :format %)]
   [radio-button
    :label       "clj"
    :value       :clj
    :model       (:format @state)
    :on-change   #(change! :format %)]])