(ns pinkgorilla.meta.view
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [storagetype]]
   [pinkgorilla.meta.tags :refer [tag-box meta->tags]]))

(defn tag-box-meta [meta]
  [tag-box (meta->tags meta) #{}])

(defn tagline [meta]
  (let [tl (:tagline meta)]
    (or tl "No Tagline provided")))



