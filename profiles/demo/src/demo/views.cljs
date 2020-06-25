(ns demo.views
  (:require
   [reagent.core :as r]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
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


(defn save-dialogs []
  [:div
   [:h1 "Save Dialog Test"]
   [:div.grid.grid-cols-2

    [:div
     [:p "file"]
     [save-dialog {:storage (create-storage {:type :file :filename "test.cljg"})
                   :on-save on-save
                   :on-cancel on-cancel}]]
    [:div
     [:p "repo"]
     [save-dialog {:storage (create-storage {:type :repo
                                             :user "pink-gorilla"
                                             :repo "gorilla-notebook"
                                             :filename "resources/notebooks/blue.ipynb"})
                   :on-save on-save
                   :on-cancel on-cancel}]]

    [:div
     [:p "gist"]
     [save-dialog {:storage (create-storage {:type :gist
                                             :id "8204fd0b2aba27f06c04dffcb4fd0a24"
                                             :user "awb99"
                                             :filename "test.cljg"})
                   :on-save on-save
                   :on-cancel on-cancel}]]
    
      [:div
       [:p "empty"]
       [save-dialog {:storage nil
                     :on-save on-save
                     :on-cancel on-cancel}]]
    
    
    ]])

(defn app []
  [:div
   [:link {:rel "stylesheet" :href "/tailwindcss/dist/tailwind.css"}]
   ;[:h1 "explorer-ui"]
   ;[:p (str "route: " (pr-str @current))]
   (case (:handler @current)
     :ui/explorer [notebook-explorer open-notebook]
     :ui/notebook [document-page document-view-dummy]
     :demo/save [save-dialogs]
     ;  :system [system (:system-id route-params)]
     [:div
      [:h1 "route handler not found: "]
      [:p @current]])])


