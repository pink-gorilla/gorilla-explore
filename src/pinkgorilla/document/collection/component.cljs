(ns pinkgorilla.document.collection.component
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [subscribe]]
   [pinkgorilla.document.collection.sidebar :refer [sidebar]]
   [pinkgorilla.document.collection.notebook :refer [notebook-box]]
   [pinkgorilla.document.collection.subscriptions] ; side effects
   [pinkgorilla.document.collection.events])) ; side effects

;; new version:
;; https://github.com/braveclojure/open-source-2/blob/master/src/frontend/open_source/components/project/list.cljs


(defn notebook-explorer
  [open-notebook]
  (let [roots (subscribe [:explorer/notebooks-root])
        unsaved (subscribe [:explorer/notebooks-unsaved])
        notebooks      (subscribe [:explorer/notebooks-filtered])
        tags-available (subscribe [:explorer/tags-available])
        search         (subscribe [:explorer/search-options])]
    (fn []
      (let [roots-all (assoc @roots "unsaved" @unsaved)]
        [:div
         [:link {:rel "stylesheet" :href "/r/explorer/explorer.css"}]
         [:div.flex.w-screen.h-screen ; .w-100 ; separation for main/sidebar
          [:div  {:style {:width "10em"}
                  :class "h-100" ;"w-1/4"
} ; sidebar 
           [sidebar roots-all search @tags-available]]
          (into [:div.flex.flex-wrap {:class "w-full h-full overflow-scroll m-0 p-4"}] ; notebook grid left, 3/4 of width
         ; [ui/ctg {:transitionName "filter-survivor" :class "listing-list"}
                (map (fn [notebook]
                       [notebook-box open-notebook (:tags @search) notebook])
                     @notebooks))

         ; ]
          ]]))))
