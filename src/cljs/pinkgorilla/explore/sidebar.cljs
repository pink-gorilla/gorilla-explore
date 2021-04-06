(ns pinkgorilla.explore.sidebar
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [pinkgorilla.meta.tags :refer [tag-box]])
  (:require-macros [pinkgorilla.explore.macros :refer [tv]]))

(defn repo [root [name notebooks]]
  [:div.h-12.flex.flex-row
   [:span.w-20.text-blue-800.text-lg
    {:class (if (= name root) " bg-orange-400" "")
     :on-click (fn [e] (dispatch [:explorer/set-search-root name]))}
    name]
   [:span.border.rounded-full.bg-pink-300.p-1 (str (count notebooks))]])

(defn root-box [roots root]
  (let [notebooks-all (subscribe [:explorer/notebooks-all])]
    (fn [roots root]
      [:div
       [:h3.w-full.bg-blue-300.p-2 "Locations"]
        ; individual root
       (for [r roots]
         ^{:key (first r)} [repo root r])
       ; all
       [repo root ["all" @notebooks-all]]])))

(defn sidebar
  "sidebar on the right (search keyword / tags / etc)"
  [roots search-options tags-available]
  (let [{:keys [text tags root]} @search-options]
    [:div.mt-12

     [root-box roots root]

     [:h3.w-full.bg-blue-300.p-2 "Search Text"]
     [:input.mb-5
      {:value text
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
      [tag-box tags-available tags]]]))
