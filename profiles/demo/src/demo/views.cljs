(ns demo.views
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.ui.ui.dialog :refer [modal-container]]
   [pinkgorilla.explore.component :refer [notebook-explorer]]
   [pinkgorilla.document.component :refer [document-page]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]
   [demo.save-dialog-demo :refer [save-dialogs]]
   [demo.bidi :refer [goto! current query-params]]))

(defn document-view-dummy [storage document]
  [:div

   [:div.m-3.bg-blue-300
    [:a {:on-click #(goto! :ui/explorer)}
     "explorer"]]

   [:div.m-16.bg-orange-400
    [:h1 "Document Meta Info - Replace me with your document viewer!"]
    [:p " storage: " (pr-str storage)]]
   [:div.m-16.bg-blue-300 "document: " @document]])

(defn open-notebook [nb]
  (info "load-notebook-click" nb)
  (goto-notebook! (:storage nb)))

(defn app []
  [:div
   [:link {:rel "stylesheet" :href "/tailwindcss/dist/tailwind.css"}]
   [modal-container]
   ;[:h1 "explorer-ui"]
   ;[:p (str "route: " (pr-str @current))]
   (case (:handler @current)
     :ui/explorer [notebook-explorer open-notebook]
     :ui/notebook [document-page @query-params document-view-dummy]
     :demo/save [save-dialogs]
     ;  :system [system (:system-id route-params)]
     [:div
      [:h1 "route handler not found: "]
      [:p @current]])])


