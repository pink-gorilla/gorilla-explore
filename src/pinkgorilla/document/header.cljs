(ns pinkgorilla.document.header
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [reagent.core :as r]
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [storagetype determine-name]]
   [pinkgorilla.document.meta.view :refer [tag-box-meta]]))

(defn tagline [storage meta]
  (let [edit? (r/atom false)]
    (fn [storage meta]
      (if @edit?
        [:input {:value (:tagline meta)
                 :on-blur #(reset! edit? false)
                 :on-key-press #(when (= 13 (.-which %))
                                  (reset! edit? false))
                 :on-change #(dispatch [:document/update-meta storage
                                        (assoc meta :tagline (-> % .-target .-value))])}]
        [:span {:on-click #(reset! edit? true)}
         (or (:tagline meta) "No tagline entered...")]))))

(defn header [header-menu-left file-info document storage]
  (let [d @document
        meta (:meta d)]
    [:div.bg-blue-100.flex.flex-row.justify-between.w-100 ; screen
     ;left-col
     [:div
      [header-menu-left storage document]
      [:span.m-2.border.border-solid (storagetype storage)]
      [:span.m-2.text-xl (determine-name storage)] ; (:name file-info)]
      [:span.m-2.italic [tagline storage meta]]]
     ;right-col
     [:div ; .pl-5
      [:div.inline-block.m-1 [tag-box-meta meta]]
      [:span.m-2 (:path file-info)]
      #_[:span.border-solid.border.round.hover:bg-green-400.m-2
         {:on-click #(dispatch [:document/save storage])}
         "save"]
      #_[:span.border-solid.border.round.hover:bg-green-400.m-2
         {:on-click #(dispatch [:document/new])}
         "new"]]]))

(defn document-view-with-header [document-view header-menu-left storage document]
  (let [file-info (split-filename (:filename storage))
        _ (debug "nb file-info: " file-info)]
    [:div.w-screen.h-screen.flex.flex-col.overflow-hidden
     [header header-menu-left file-info document storage]
     [:div.h-full.overflow-y-scroll.overflow-x-hidden ; .overflow-scroll
      [document-view storage document]]]))