(ns pinkgorilla.document.events
  (:require
   [taoensso.timbre :refer-macros [debug info warn error]]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [ajax.core :as ajax]
   [bidi.bidi :as bidi]
   [pinkgorilla.storage.protocols :refer [storage->map]]
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
                     :storages {}}]
     (info "document init .. " doc-config)
     (assoc db :document doc-config))))

;; Load File (from URL Parameters) - in view or edit mode

(defn get-secrets [db]
  (:secrets db))

(defn get-tokens [db]
  (let [token-github (get-in db [:token :github :access-token])]
    (if token-github
      {:token-github token-github}
      {})))

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
     {:db         (assoc-in db [:document :storages storage] {:status :document/loading}) ; notebook view on loading
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
   (error "Load Response Error for " storage " Error: " response-body)
   (let [content (:content response-body)]
     (assoc-in db [:document :storages storage]
               {:error response-body}))))

(reg-event-db
 :document/load-success
 (fn
   [db [_ storage notebook]]
   (let [_ (debug "Document Load Response:\n" notebook)
         fn-hydrate (get-in db [:document :fn-hydrate])
         notebook (fn-hydrate notebook)
         id (get-in notebook [:meta :id])]
     (-> db
         (assoc-in [:docs id] notebook)
         (assoc-in [:document :storages storage] {:id id})))))

;; SAVE File

(reg-event-fx
 :document/save
 (fn [{:keys [db]} [_ doc-id storage]]
   (info "saving notebook id: " doc-id "storage: " storage)
   (let [;secrets (get-secrets db)
         tokens (get-tokens db)
         url  (url-link db :api/notebook-save)
         doc-id (if (keyword? doc-id) doc-id (keyword doc-id))
         notebook (get-in db [:docs doc-id])
         fn-dehydrate (get-in db [:document :fn-dehydrate])
         notebook (fn-dehydrate notebook)
         nb-no-storage (dissoc notebook :storage)
         _ (warn "storage: " storage)
         m  (when storage (storage->map storage))
         params (merge {:storage m
                        :notebook nb-no-storage}
                       tokens)]
     (info "save params: " params)
     (if (not storage)
       (do
         (dispatch [:document/save-as doc-id storage])
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
                     :on-failure      [:document/save-error storage]}}))))


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
   [db [_ storage response]]
   (let [error-message (get-in response [:response :error-message])]
     (error "Save Error " response " for \n" storage)
     (dispatch [:notification/show
                (str "Save error! " error-message)
                :danger])
     db)))

; NEW Document

(reg-event-db
 :document/new
 (fn [db [_]]
   (info "creating new notebook:")
   (let [document (new-notebook)
         doc-id (get-in document [:meta :id])
         fn-hydrate (get-in db [:document :fn-hydrate])
         notebook (fn-hydrate document)]
     (warn "goto doc id: " doc-id)
     (goto-notebook! {:id doc-id})
     (assoc-in db [:docs doc-id] notebook))))

; EDIT Document

(reg-event-db
 :document/update-meta
 (fn [db [_ doc-id meta]]
   (assoc-in db [:docs doc-id :meta] meta)))




