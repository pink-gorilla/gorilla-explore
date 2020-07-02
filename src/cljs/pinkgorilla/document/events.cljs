(ns pinkgorilla.document.events
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [ajax.core :as ajax]
   [bidi.bidi :as bidi]
   [pinkgorilla.storage.protocols :refer [storagetype gorilla-path]]
   [pinkgorilla.storage.unsaved :refer [StorageUnsaved]]
   [pinkgorilla.notebook.hipster :refer [make-hip-nsname]]
   [pinkgorilla.notebook.template :refer [new-notebook]]
   [pinkgorilla.notebook.hydration :refer [hydrate]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]))

(reg-event-db
 :document/init
 (fn [db [_ explorer-routes-api]]
   (let [db (or db {})]
     (info "document init .. " explorer-routes-api)
     (assoc db :document
            {:documents {}
             :routes explorer-routes-api}))))

(defn link [db handler]
  (info "link for handler:" handler)
  (let [routes (get-in db [:document :routes])
        ; _ (info "routes: " routes)
        url (bidi/path-for routes handler)]
    (info "bidi link url: " url)
    url))

;; Load File (from URL Parameters) - in view or edit mode

(defn get-secrets [db]
  (:secrets db))

(reg-event-fx
 :document/load
 (fn [{:keys [db]} [_ storage]]
   (let [secrets (get-secrets db)
         url  (link db :api/notebook-load)
         storage-type (storagetype storage)
         _ (info "loading storage:" storage-type storage)
         params (assoc storage
                       :storagetype storage-type ; (keyword (:storagetype storage))
                       :tokens secrets)]
     ;(dispatch [:ga/event :notebook :load])
     (info "loading :" params)
     {:db         (assoc-in db [:document :documents storage] :document/loading) ; notebook view on loading
      ;; :ga/event [:notebook-load]
      :http-xhrio {:method          :get
                   :uri             url
                   :params          params
                   :timeout         15000
                   :response-format (ajax/transit-response-format)   ;(ajax/json-response-format {:keywords? true})
                   :on-success      [:document/load-success storage]
                   :on-failure      [:document/load-error storage]}})))

(reg-event-db
 :document/load-error
 (fn
   [db [_ storage response-body]]
   (error "Load Response Error for\n" storage)
   (error "Load Response Error:\n" response-body)
   (let [content (:content response-body)
         _ (error "Content Only:\n" content)]
     (assoc-in db [:document :documents storage]
               {:error response-body}))))

(reg-event-db
 :document/load-success
 (fn
   [db [_ storage notebook]]
   (let [_ (debug "Document Load Response:\n" notebook)]
     (assoc-in db [:document :documents storage] notebook))))

;; SAVE File

(reg-event-fx
 :document/save
 (fn [{:keys [db]} [_ storage]]
   (let [secrets (get-secrets db)
         url  (link db :api/notebook-save)
         storage-type (storagetype storage)
         _ (info "notebook saving to storage " storage-type)
         notebook (get-in db [:document :documents storage])
         params {:storage-params (into {} storage)
                 :storagetype storage-type
                 :notebook notebook
                 :tokens secrets}]
     (info "save params: " params)

     (if (= storage-type :unsaved)
       (do
         (dispatch [:document/save-as storage])
         {:db db})
       {:db         db ; (assoc-in db [:dialog :save] false)
        :http-xhrio {:method          :post
                     :uri             url
                     :params          params
                     :timeout         5000                     ;; optional see API docs
                     :format          (ajax/transit-request-format)
                                    ; (ajax/json-request-format {:keywords? true})
                                    ; (ajax/url-request-format) ; request encoding POST body url-encoded
                                    ; url-request format does not work because server has problem decoding token maps
                     :response-format (ajax/transit-response-format)
                                     ;(ajax/json-response-format {:keywords? true}) 
                     :on-success      [:document/save-success storage]
                     :on-failure      [:document/save-error]}}))))


;; display success message when saving was successful


(reg-event-db
 :document/save-success
 (fn [db [_ storage result]]
   (info "Save to storage " storage " result : " result)
     ;(add-notification (notification :info "Notebook saved."))
   db))

(reg-event-db
 :document/save-error
 (fn
   [db [_ storage response-body]]
   (error "Save Response Error for\n" storage)
   (error "Save Response Error:\n" response-body)
   ;(dispatch [:notification-add (notification :warning "save-notebook ERROR!!")])
   (let [content (:content response-body)
         _ (error "Content Only:\n" content)]
     ;(assoc-in db [:document :documents storage]
     ;          {:error response-body})
     db)))

; NEW Document

(reg-event-db
 :document/new
 (fn [db [_]]
   (let [id (make-hip-nsname)
         _ (info "creating document:" id)
         storage (StorageUnsaved. id)
         document-dehydrated (new-notebook id)
         document (hydrate document-dehydrated)]
     (goto-notebook! storage)
     (assoc-in db [:document :documents storage] document))))

; EDIT Document

(reg-event-db
 :document/update-meta
 (fn [db [_ storage meta]]
   (assoc-in db [:document :documents storage :meta] meta)))




