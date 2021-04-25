(ns pinkgorilla.document.meta.view
  (:require
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.document.meta.tags :refer [meta->tags]]))

; tagline

(defn tagline [meta]
  (let [tl (:tagline meta)]
    (or tl "No Tagline provided")))

; tag box

(defn tag-view
  [tags tag]
  [:span.tag-container
   [:span.tag {:class (when (get tags tag) "active")
               :data-prevent-nav true
               :on-click #(do (.stopPropagation %)
                              (.preventDefault %)
                              (dispatch [:explorer/toggle-tag tag]))} tag]])

(defn tag-box [tags selected-tags]
  [:div
   (for [tag tags]
     ^{:key (gensym)} [tag-view selected-tags tag])])

(defn tag-box-meta [meta]
  [:div
   [tag-box (meta->tags meta) #{}]])
