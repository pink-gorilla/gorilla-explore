(ns pinkgorilla.document.collection.notebook
  (:require
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.document.meta.tags :refer [meta->tags]]
   [pinkgorilla.document.meta.view :refer [tagline tag-box]]))

; github stars are not yet included in the view.
; [:div;.stars
;  (:stars l)]


(def border " border-r border-b border-l border-gray-400")
(def lg " lg:border-l-0 lg:border-t lg:border-gray-400 lg:rounded-b-none lg:rounded-r")

(defn notebook-box [open-notebook selected-tags notebook]
  (let [file-info (split-filename (:filename notebook))
        ;_ (debug "nb file-info: " file-info)
        ]
    [:div {:on-click #(open-notebook notebook)
           :style {:width "30em"}
           :class (str "h-48 bg-green-400 cursor-pointer rounded-b  p-4 flex flex-col justify-between leading-normal hover:bg-yellow-400" border lg)}

     [:div.mb-8

    ;; project storage location - click opens github web page or the file browser
      [:div.px-0.py-0.bg-White
       [:span {:class "pg-storage-prop mr-1"}  (:type notebook)]
       [:span {:class "pg-storage-prop mr-1"} (:repo notebook)]
       [:span {:class "pg-storage-prop"} (:path file-info)]]

    ;; project name - click opens the notebook in pink-gorilla
      [:a {:on-click #(open-notebook notebook)}
       [:div {:class "text-white font-bold text-xl mb-2 cursor-pointer"} (:name file-info)]]

      [:p {:class "text-white text-base h-8 overflow-hidden"}
       (tagline (:meta notebook))]]

     [:div.flex.items-center
      [:img {:class "w-10 h-10 bg-white rounded-full mr-4" :src "/r/pink-gorilla-32.png" :alt "Avatar"}]
      [:div.text-sm.mr-4
       [:p.text-white.leading-none (:user notebook)]
       [:p.text-gray-600 (:edit-date notebook)]]
      [:div.text-sm.mr-4
       [:div.px-6.py-4
        [tag-box (meta->tags (:meta notebook)) selected-tags]

      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"} "photography"]
      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700 mr-2"} "travel"]
      ;[:span {:class "inline-block bg-gray-200 rounded-full px-3 py-1 text-sm font-semibold text-gray-700"} "winter"]
        ]]]]))

