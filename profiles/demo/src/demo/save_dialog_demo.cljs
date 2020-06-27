(ns demo.save-dialog-demo
  (:require
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.save-dialog.component :refer [save-dialog]]))

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
                   :on-cancel on-cancel}]]]])