(ns demo.pages.document
  (:require
   [re-frame.core :refer [subscribe dispatch]]
   [webly.web.handler :refer [reagent-page]]
   [webly.web.routes :refer [query-params]]
   [pinkgorilla.document.component :refer [document-page]]))

(defn header-icon [fa-icon rf-dispatch]
  [:a {:on-click #(dispatch rf-dispatch)
       :class "hover:text-blue-700 mr-1"}
   [:i {:class (str "fa fa-lg pl-1 " fa-icon)}]])

(defn header-menu-left [storage document]
  [:<>
   [header-icon "fa-question-circle" [:bidi/goto :demo/main]]
   [header-icon "fa-th-large" [:bidi/goto :ui/explorer]]
   [header-icon "fa-plus" [:document/new]]
   [header-icon "fa-save" [:document/save storage]]
   [header-icon "fa-stream" [:palette/show]]])

(defn document-view-dummy [storage document]
  [:div

   #_[:div.m-3.bg-blue-300
      [:a {:on-click #(goto! :ui/explorer)}
       "explorer"]]

   [:div.m-16.bg-orange-400
    [:h1 "Document Meta Info - Replace me with your document viewer!"]
    [:p " storage: " (pr-str storage)]]
   [:div.m-16.bg-blue-300 "document: " @document]])

(defmethod reagent-page :ui/notebook [& args]
  [document-page document-view-dummy header-menu-left @query-params])