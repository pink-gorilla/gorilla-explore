(ns pinkgorilla.document.save-dialog.component
  (:require
   [taoensso.timbre :refer-macros [info warn error]]
   [reagent.core :as r]
   [re-com.core :refer [input-text radio-button]]
   [pinkgorilla.storage.filename-encoding :refer [split-filename encoding->extension]]
   [pinkgorilla.storage.protocols :refer [storagetype determine-name create-storage]]
   [pinkgorilla.document.save-dialog.format :refer [storage-format]]
   [pinkgorilla.document.save-dialog.type :refer [storage-type]]
   [pinkgorilla.document.save-dialog.storage-type.github-repo :refer [github]]
   [pinkgorilla.document.save-dialog.storage-type.file :refer [file filename]]))

(def empty-form
  {; storage-type to create storage
   :storage-type :file
   :filename "/my-new-notebook.cljg"
   :name "my-new-notebook"
   :path "/"
   ; query-params for document-save
   ;:description ""
   :user ""
   :repo ""

   ;encoding format
   :format :gorilla
   :explorer-root nil ; the keyword corresponding to the selected explorer root directory
   :file-directory "" ; directory corresponding to selected explorer root
   })

(defn calc-filename [state]
  (let [{:keys [format name path]} @state
        filename (str path name "." (encoding->extension format))]
    (swap! state assoc :filename filename)))

(defn make-storage [state]
  (let [storage-type (keyword (:storage-type state))
        _ (info "saving as storage type: " storage-type)
        storage (create-storage (assoc state :type storage-type))]
    (when-not storage
      (error "save-dialog could not make storage for: " state))
    storage))

(defn storage->form [storage]
  (if storage
    (let [storage-type (storagetype storage)
          storage-seq (into {} storage)
          file-info (split-filename (:filename storage-seq))]
      (info "storage: " storage-seq)
      {:storage-type storage-type
       :filename (:full file-info)
       :format (or (:encoding file-info) :gorilla)
       :name (or (:name file-info) "")
       :path (or (:path file-info) "")})
    (do (warn "save-as called with empty storage")
        {})))

(defn save-dialog
  [{:keys [storage on-cancel on-save]}]
  (let [state (r/atom (merge empty-form (storage->form storage)))
        change! (fn [k v]
                  (swap! state assoc k v)
                  (calc-filename state))
        check-key (fn [form keycode]
                    (case keycode
                      27 (on-cancel) ; ESC
                      13 (on-save storage (make-storage @state))   ; Enter
                      nil))]
    (fn [{:keys [on-cancel on-save]}]
      [:div.bg-blue-300.m-5.border-solid.inline-block
       {:display {:width "200px"}
        :on-key-down   #(check-key @state (.-which %))}
       [:div.flex.flex-row.justify-start
        [storage-format state change!]
        [storage-type state change!]]
       (case (:storage-type @state)
         :file  [file state change!]
         :repo  [github state change!]
         :gist  [github state change!]
         [:div.bg-red-700 "unknown storage source: " (:storage-type @state)])
       [filename state change!]
       [:div.flex.flex-row.justify-between
        [:div.bg-red-700.m-2.w-16.p-1.text-center
         {:on-click #(on-cancel)}
         "Cancel"]
        [:div.bg-green-700.m-2.w-16.p-1.text-center
         {:on-click #(on-save storage (make-storage @state))}
         "Save"]]])))
