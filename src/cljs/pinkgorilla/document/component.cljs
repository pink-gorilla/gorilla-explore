(ns pinkgorilla.document.component
  (:require
   [clojure.walk]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [re-frame.core :refer [subscribe dispatch]]
   [pinkgorilla.storage.protocols :refer [query-params-to-storage]]
   [pinkgorilla.document.events] ; side effects
   [pinkgorilla.document.subscriptions] ; side effects
   [pinkgorilla.meta.view :refer [document-view-meta]]
   [pinkgorilla.bidi :refer [query-params]]))


(defn err [storage document message]
  [:div.m-6.p-6.bg-blue-300.border-solid
   [:h1 message]
   (when storage
     [:p "storage: " (pr-str storage)])
   (when document
     [:p (pr-str (:error document))])
   ])

(defn document-page [document-view]
  (let [params  @query-params
        kparams (clojure.walk/keywordize-keys params)
        _ (info "kw params: " kparams)
        stype (keyword (:source kparams))
        storage (query-params-to-storage stype kparams)
        document (if storage (subscribe [:document/get storage]))]
    (println "e/nb! qp: " params)
    (when (and storage (not @document))
      (info "loading document storage: " storage)
      (dispatch [:document/load storage]))
    (fn [document-view]
      [:div
       ;[:h1 "Document Viewer"]
       ;[:p (pr-str params)]
       (cond
         (not storage)
         [err storage @document "storage parameter are bad!"]

         (nil? @document)
         [err storage @document "requesting document"]

         (= :document/loading @document)
         [err storage @document "Loading .."]

         (:error @document)
         [err storage @document "Error Loading Document"]

         :else
         [document-view-meta storage document document-view])])))





