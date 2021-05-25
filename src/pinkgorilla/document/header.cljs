(ns pinkgorilla.document.header
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [reagent.core :as r]
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [storagetype determine-name]]
   [pinkgorilla.document.meta.view :refer [tag-box-meta]]))

(defn tagline [meta]
  (let [edit? (r/atom false)]
    (fn [meta]
      (if @edit?
        [:input {:value (:tagline meta)
                 :on-blur #(reset! edit? false)
                 :on-key-press #(when (= 13 (.-which %))
                                  (reset! edit? false))
                 :on-change #(dispatch [:document/update-meta (:id meta)
                                        (assoc meta :tagline (-> % .-target .-value))])}]
        [:span {:on-click #(reset! edit? true)}
         (or (:tagline meta) "No tagline entered...")]))))

(defn header [header-menu-left file-info document storage]
  (let [meta (:meta document)]
    [:div.bg-blue-100.flex.flex-row.justify-between.w-100 ; screen
     ;left-col
     [:div
      [header-menu-left storage document]
      [:span.m-2.border.border-solid (if storage (storagetype storage) "no storage")]
      [:span.m-2.text-xl (if storage (determine-name storage) "unsaved")] ; (:name file-info)]
      [:span.m-2.italic [tagline meta]]]
     ;right-col
     [:div ; .pl-5
      [:div.inline-block.m-1 [tag-box-meta meta]]
      [:span.m-2 (when file-info (:path file-info))]
      #_[:span.border-solid.border.round.hover:bg-green-400.m-2
         {:on-click #(dispatch [:document/save storage])}
         "save"]
      #_[:span.border-solid.border.round.hover:bg-green-400.m-2
         {:on-click #(dispatch [:document/new])}
         "new"]]]))

(defn document-view-with-header [document-view header-menu-left storage document]
  (let [file-info (when storage (split-filename (:filename storage)))]
    [:div.w-screen.h-screen.flex.flex-col.overflow-hidden
     [header header-menu-left file-info document storage]
     [:div.h-full.overflow-y-scroll.overflow-x-hidden ; .overflow-scroll
      [document-view storage document]]]))