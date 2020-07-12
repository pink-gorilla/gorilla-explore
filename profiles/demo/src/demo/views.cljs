(ns demo.views
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.web.handler :refer [reagent-page]]
   [webly.web.routes :refer [query-params]]
   [pinkgorilla.explore.component :refer [notebook-explorer]]
   [pinkgorilla.document.component :refer [document-page]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]
   [demo.save-dialog-demo :refer [save-dialogs]]))


(defn main []
  [:div
   [:h1 "demo - explorer"]
   [:a.bg-green-300 {:href "/explorer"} "explorer"]
   [:a.bg-red-300 {:href "/demo/save"} "save-as dialog demo"]])

(defmethod reagent-page :demo/main [& args]
  [main])

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
  [document-page @query-params document-view-dummy])

(defn open-notebook [nb]
  (info "load-notebook-click" nb)
  (goto-notebook! (:storage nb)))

(defmethod reagent-page :ui/explorer [& args]
  [notebook-explorer open-notebook])

(defmethod reagent-page :demo/save [& args]
  [save-dialogs])



