(ns pinkgorilla.explore.sidebar
  (:require
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.meta.tags :refer [tag-box]])
  (:require-macros [pinkgorilla.explore.macros :refer [tv]]))

(defn repo [[name notebooks]]
  [:div.h-12.flex.flex-row
   [:span.w-20.text-blue-800.text-lg name]
   [:span.border.rounded-full.bg-pink-300.p-1 (str (count notebooks))]])

(defn root-box [roots]
  [:div
   [:h3.w-full.bg-blue-300.p-2 "Locations"]
   (into [:div]
         (map repo roots))])

(defn sidebar
  "sidebar on the right (search keyword / tags / etc)"
  [roots search tags-available]
  [:div.mt-12

   [root-box @roots]

   [:h3.w-full.bg-blue-300.p-2 "Search Text"]
   [:input.mb-5
    {:value (:text @search)
     :on-change #(dispatch [:explorer/search-text (tv %)])
     :placeholder "Search text ... "}]

   #_[:div
      [:label.label {:for "stargazers-count"}]
      [:span
       [:i.fa.fa-star]
       "min github stars"]
      [:div
       [:input.input.stargazers-count {:type "number"
                                       :label "span,i.fa.fa-star, min github stars"
                                       :id "stargazers-count"
                                       :value "5"}]]]

   #_[:div
      [:label.label {:for "days-since-push"}
       [:span
        [:i.fa.fa-clock-o]
        "most days since last edit"]]
      [:div
       [:input.input.days-since-push {:type "number"
                                      :label "span,i.fa.fa-clock-o, most days since last push"
                                      :id "days-since-push"
                                      :value "368"}]]]

   [:h3.w-full.bg-blue-300.p-2 "Tags"]
   [:div.section.tags
    [tag-box tags-available (:tags @search)]]])
