(ns pinkgorilla.document.events
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [ajax.core :as ajax]
   [bidi.bidi :as bidi]
   [pinkgorilla.storage.protocols :refer [storage->map]]
   [pinkgorilla.storage.unsaved :refer [StorageUnsaved]]
   [pinkgorilla.notebook.hipster :refer [make-hip-nsname]]
   [pinkgorilla.notebook.template :refer [new-notebook]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]))

(defn hydrate-noop [nb]
  (info "hydrating / no-op")
  nb)

(defn dehydrate-noop [nb]
  (info "dehydrating / no-op")
  nb)

(reg-event-db
 :document/init
 (fn [db [_ config]]
   (let [db (or db {})
         {:keys [fn-hydrate fn-dehydrate]
          :or {fn-hydrate hydrate-noop
               fn-dehydrate dehydrate-noop}}
         config
         doc-config {:fn-hydrate fn-hydrate
                     :fn-dehydrate fn-dehydrate
                     :documents {}}]
     (info "document init .. " doc-config)
     (assoc db :document doc-config))))

;; Load File (from URL Parameters) - in view or edit mode

(defn get-secrets [db]
  (:secrets db))

(defn get-tokens [db]
  (let [github (get-in db [:token :github :access-token])]
    {:token-github github}))

(defn url-link [db route]
  (bidi/path-for (get-in db [:bidi :server]) route))

(reg-event-fx
 :document/load
 (fn [{:keys [db]} [_ storage]]
   (let [;secrets (get-secrets db)
         tokens (get-tokens db)
         url  (url-link db :api/notebook-load)
         _ (info "loading storage:" storage)
         params (merge (storage->map storage)
                       tokens ; secrets
                       )]
     (info "loading url: " url " params: "  params)
     {:db         (assoc-in db [:document :documents storage] :document/loading) ; notebook view on loading
      :dispatch [:ga/event {:category "notebook-storage" :action "load" :label 77 :value url}]
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
   (let [_ (debug "Document Load Response:\n" notebook)
         fn-hydrate (get-in db [:document :fn-hydrate])
         notebook (fn-hydrate notebook)]
     (assoc-in db [:document :documents storage] notebook))))

;; SAVE File

(reg-event-fx
 :document/save
 (fn [{:keys [db]} [_ storage]]
   (info "notebook saving to storage " storage)
   (let [;secrets (get-secrets db)
         tokens (get-tokens db)
         url  (url-link db :api/notebook-save)
         notebook (get-in db [:document :documents storage])
         fn-dehydrate (get-in db [:document :fn-dehydrate])
         notebook (fn-dehydrate notebook)
         m  (storage->map storage)
         params (merge {:storage m
                        :notebook notebook}
                       tokens)]
     (info "save params: " params)
     (if (= (:type m) :unsaved)
       (do
         (dispatch [:document/save-as storage])
         {:db db})
       {:db         db
        :dispatch [:ga/event {:category "notebook-storage" :action "save" :label 77 :value 13}]
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
   (info "Save success! storage: " storage " result: " result)
   (dispatch [:notification/show
              (str "Saved successully!")
              :success])
   db))

(reg-event-db
 :document/save-error
 (fn
   [db [_ storage response-body]]
   (error "Save Error " response-body " for \n" storage)
                  ; Error: " (:status-text response) " (" (:status response) ")")
   (dispatch [:notification/show
              (str "Save error! ")
              :danger])
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
         document (new-notebook id)
         fn-hydrate (get-in db [:document :fn-hydrate])
         notebook (fn-hydrate document)]
     (goto-notebook! storage)
     (assoc-in db [:document :documents storage] notebook))))

; EDIT Document

(reg-event-db
 :document/update-meta
 (fn [db [_ storage meta]]
   (assoc-in db [:document :documents storage :meta] meta)))




