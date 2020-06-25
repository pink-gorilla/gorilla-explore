(ns demo.views
  (:require
   [reagent.core :as r]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.explore.component :refer [notebook-explorer]]
   [pinkgorilla.document.component :refer [document-page]]
   [pinkgorilla.save-dialog.component :refer [save-dialog]]
   [pinkgorilla.bidi :refer [goto! goto-notebook! current]]
  ; [demo.routes]
   ))

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
  (println "load-notebook-click" nb)
  (goto-notebook! nb))


(defn on-save [data]
  (js/alert (pr-str data)))

(defn on-cancel [data]
  (js/alert "cancel!"))

(defn app []
  [:div
   [:link {:rel "stylesheet" :href "/tailwindcss/dist/tailwind.css"}]
   ;[:h1 "explorer-ui"]
   ;[:p (str "route: " (pr-str @current))]
   (case (:handler @current)
     :ui/explorer [notebook-explorer open-notebook]
     :ui/notebook [document-page document-view-dummy]
     :demo/save [save-dialog {:on-save on-save
                              :on-cancel on-cancel}]
     ;  :system [system (:system-id route-params)]
     [:div
      [:h1 "route handler not found: "]
      [:p @current]])])


