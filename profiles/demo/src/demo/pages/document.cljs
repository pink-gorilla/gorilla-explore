(ns demo.pages.document
  (:require
   [taoensso.timbre :refer-macros [debugf info warn error]]
   [re-frame.core :as rf]
   [webly.web.handler :refer [reagent-page]]
   [pinkgorilla.document.component :refer [document-page]]))

(defn header-icon [fa-icon rf-dispatch]
  [:a {:on-click #(rf/dispatch rf-dispatch)
       :class "hover:text-blue-700 mr-1"}
   [:i {:class (str "fa fa-lg pl-1 " fa-icon)}]])

(defn header-menu-left [storage doc]
  [:<>
   [header-icon "fa-question-circle" [:bidi/goto :demo/main]]
   [header-icon "fa-th-large" [:bidi/goto :ui/explorer]]
   [header-icon "fa-plus" [:document/new]]
   [header-icon "fa-save" [:document/save (get-in doc [:meta :id]) storage]]
   [header-icon "fa-save" [:document/save-as (get-in doc [:meta :id]) storage]]
   [header-icon "fa-stream" [:palette/show]]])


(defn document-view-dummy [storage doc]
      [:div.m-16.bg-orange-400
       [:div.bg-red-500 "dummy document viewer!"]
       [:div.bg-red-500 " storage: " (pr-str storage)]
       [:div.bg-red-500 "document id: " (get-in doc [:meta :id])]
       ;[header-menu-left storage (get-in doc [:meta :id])]
       (if doc
         [:div.bg-blue-300  (pr-str doc)]
         [:div.bg-red-700 "no document..."])])

(defmethod reagent-page :ui/notebook [{:keys [route-params query-params handler] :as route}]
  (info "doc page args: " route)
  (if query-params
    [document-page document-view-dummy header-menu-left query-params]
    [:div "error. no query params route-p:" (pr-str route)]))