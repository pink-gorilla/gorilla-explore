(ns pinkgorilla.document.component
  (:require
   [clojure.string]
   [clojure.walk]
   [taoensso.timbre :refer-macros [debugf info warn error]]
   [re-frame.core :as rf :refer [subscribe dispatch]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.document.events] ; side effects
   [pinkgorilla.document.subscriptions] ; side effects
   [pinkgorilla.document.header :refer [document-view-with-header]]))

(defn show-id [document-view header-menu-left storage doc-id]
  ; bug: this does not resubscribe.
  (let [doc-id-kw (keyword doc-id)
        _ (warn "subscribing doc id: " doc-id-kw)
        d (rf/subscribe [:document/view doc-id-kw])]
    (fn [document-view header-menu-left storage doc-id]
      (info "doc-id: " doc-id)
      [document-view-with-header document-view header-menu-left storage @d])))


;; DOC LOADER


(defn err [storage document message]
  [:div.m-6.p-6.bg-red-300.border-solid
   [:h1 message]
   (when storage
     [:p "storage: " (pr-str storage)])
   (when document
     [:p (pr-str (:error document))])])

(defn loader-status [document-view header-menu-left storage document]
  [:div
       ;[:h1 "Document Viewer"]
       ;[:p (pr-str params)]
   (cond

     (not storage)
     [err storage nil "storage parameter are bad!"]

     (nil? document)
     [err storage document "requesting document"]

     (:error document)
     [err storage document "Error Loading Document"]

     (:status document)
     [err storage document "Loading .."]

     (and document (:id document))
     [show-id document-view header-menu-left storage (:id document)]

     :else
     [err storage document "????????????"])])

(defn doc-load [document-view header-menu-left storage]
  (let [document (when storage (subscribe [:document/get storage]))]
    (fn [document-view header-menu-left storage]
      (when (and storage (not @document))
        (dispatch [:document/load storage]))
      [loader-status document-view header-menu-left storage @document])))

;;; query params

(defn create-storage-kparams [kparams]
  (let [type (:type kparams)
        type (if (string? type) (keyword (clojure.string/replace type ":" "")) type)
        kparams (assoc kparams :type type)
        storage (create-storage kparams)]
    storage))

(defn get-storage [params]
  (let [kparams (clojure.walk/keywordize-keys params)
        _ (debugf "document page kw params: %s" kparams)
        id (:id params)
        storage (if id nil (create-storage-kparams kparams))]
    {:storage storage
     :id id}))

(defn document-page [document-view header-menu-left params]
  (debugf "rendering document-page params: %s" params)
  (let [{:keys [storage id]} (get-storage params)]
    (warn "storage: " storage "id: " id)
    (if storage
      [doc-load document-view header-menu-left storage]
      [show-id  document-view header-menu-left nil id])))





