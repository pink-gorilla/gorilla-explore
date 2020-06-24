(ns pinkgorilla.explore.notebook
  (:require
   ;[clojure.string :as str] ;:refer [subs]   ; subs should exist, but does not.
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.storage.protocols :refer [external-url]]
   [pinkgorilla.meta.tags :refer [tag-box meta->tags]]
   [pinkgorilla.meta.view :refer [tagline]]))

#_(defn project-name-old [notebook]
  (let [name (or (:filename notebook) "?")]
    {:name (str/replace name #"^(.+?)/+$" "$1")}))

#_(defn project-path-name
  "extracts the only the name of the file, without extension and path"
  [l]
  (let [full-file-name (:filename l)
        ;file-name (or (:filename notebook) "?")
        ; the regex returns [full-hit name-only]
        name (re-find #"(.+?)([\w-]*).(cljg|ipynb)*$" (or full-file-name ""))]
    name))

#_(defn notebook-name [l]
  (nth (project-path-name l) 2))

#_(defn subs2 [s start]
  (.substring s start (count s)))

#_(defn project-path [l]
  (let [p (second (project-path-name l))
        root-len (count (:root-dir l))
        ;_ (.log js/console root-len)
        ]
    (if (= (:type l) :file)
      (if (nil? p)
        nil
        (if (nil? root-len)
          p
          (subs2 p root-len))); for local files remove the root dir (we have :repo so dont need full rot path)
      p)))

(defn storage-link [notebook]
  (let [storage (:storage notebook)]
    (if (nil? storage)
      ""
      (external-url storage))))


; github stars are not yet included in the view.
; [:div;.stars
;  (:stars l)]



(def border " border-r border-b border-l border-gray-400")
(def lg " lg:border-l-0 lg:border-t lg:border-gray-400 lg:rounded-b-none lg:rounded-r")

(defn notebook-box [open-notebook selected-tags notebook]
  (let [file-info (split-filename (:filename notebook))
        _ (info "nb file-info: " file-info)]
  [:div {:on-click #(open-notebook notebook)
         :class (str "h-48 bg-green-400 w-1/2 rounded-b  p-4 flex flex-col justify-between leading-normal hover:bg-orange-400" border lg)}

   [:div.mb-8

    ;; project storage location - click opens github web page or the file browser
    [:a {:href (storage-link notebook) :target "_blank" :rel "noopener noreferrer"}
     [:div.px-0.py-0.bg-White
      [:span {:class "pg-storage-prop mr-1"}  (:type notebook)]
      [:span {:class "pg-storage-prop mr-1"} (:repo notebook)]
      [:span {:class "pg-storage-prop"} (:path file-info)]]]

    ;; project name - click opens the notebook in pink-gorilla
    [:a {:on-click #(open-notebook notebook)}
     [:div {:class "text-white font-bold text-xl mb-2"} (:name file-info)]]

    [:p {:class "text-white text-base h-8 overflow-hidden"}
     (tagline (:meta notebook))]]

   [:div.flex.items-center
    [:img {:class "w-10 h-10 bg-white rounded-full mr-4" :src "./pink-gorilla-32.png" :alt "Avatar"}]
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

