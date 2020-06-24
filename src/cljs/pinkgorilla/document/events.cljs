(ns pinkgorilla.document.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx]]
   [ajax.core :as ajax]
   [taoensso.timbre :refer-macros [info]]
   [bidi.bidi :as bidi]
   [pinkgorilla.storage.protocols :refer [storagetype]]))

(reg-event-db
 :bidi/init
 (fn [db [_ bidi-config]]
   (let [db (or db {})]
     (info "bidi init ..")
     (assoc db :bidi bidi-config))))


(reg-event-db
 :documents/init
 (fn [db [_]]
   (let [db (or db {})]
     (info "document init ..")
     (assoc db :documents {}))))



;; Load File (from URL Parameters) - in view or edit mode

;; http://localhost:3449/worksheet.html#/view?source=github&user=JonyEpsilon&repo=gorilla-test&path=ws/graph-examples.clj
;; http://localhost:3449/worksheet.html#/view?source=gist&id=2c210794185e9d8c0c80564db581b136&filename=new-render.clj

(reg-event-fx
 :document/load
 (fn [{:keys [db]} [_ storage]]
   (let [secrets (:secrets db)
         api-routes (get-in db [:bidi :api])
         url  (bidi/path-for api-routes :api/notebook-load)
         stype (storagetype storage) 
         _ (info "loading storage:" stype storage)
         params (assoc storage
                       :storagetype stype ; (keyword (:storagetype storage))
                       :tokens secrets)]
     ;(dispatch [:ga/event :notebook :load])
     {:db         (assoc-in db [:documents storage] :document/loading) ; notebook view on loading
      ;; :ga/event [:notebook-load]
      :http-xhrio {:method          :get
                   :uri             url
                   :params          params
                   :timeout         15000
                   :response-format (ajax/transit-response-format) 
                   ;(ajax/json-response-format {:keywords? true})
                   :on-success      [:document/load-success storage]
                   :on-failure     [:document/load-error storage]}})))

(reg-event-db
 :document/load-error
 (fn
   [db [_ storage response-body]]
   (let [_ (info "Load Response Error for\n" storage)
         _ (info "Load Response Error:\n" response-body)
         content (:content response-body)
         _ (info "Content Only:\n" content)]
     (assoc-in db [:documents storage]
               {:error response-body}))))

(reg-event-db
 :document/load-success
 (fn
   [db [_ storage notebook]]
   (let [_ (info "Load Response:\n" notebook)]
     (assoc-in db [:documents storage] notebook))))

;; SAVE File

#_(reg-event-db
   :save-notebook
   [standard-interceptors]
   (fn [db _]
     (if-let [storage (get-in db [:storage])]
       (dispatch [:save-storage storage]) ; just save to storage if we have a storage
       (dispatch [:app:saveas])) ;otherwise open save dialog
     db))

;; save using the storage protocol
#_(reg-event-fx
   :save-storage
   (fn [{:keys [db]} [_ storage]]
     (let [stype (storagetype storage)
           _ (info "notebook saving to storage " stype)
           notebook (save-notebook-hydrated (:worksheet db))
           tokens (:settings db)
           url  (str (:base-path db) "save")
           params (assoc storage
                         :storagetype stype
                         :notebook notebook
                         :tokens tokens)]
       {:db         (assoc-in db [:dialog :save] false)
        :http-xhrio {:method          :post
                     :uri             url
                     :params          params
                     :timeout         5000                     ;; optional see API docs

                   ;; awb99: transit request does not work - possibly missing dependency?
                   ;; awb99: url-request format does not work because server has problem decoding token maps
                     :format       (ajax/json-request-format {:keywords? true}) ; (ajax/transit-request-format) ;  (ajax/url-request-format) ; request encoding POST body url-encoded
                     :response-format (ajax/json-response-format {:keywords? true}) ;(ajax/transit-response-format) ;; response encoding TRANSIT
                     :on-success      [:after-save-success storage]
                     :on-failure      [:notification-add (notification :warning "save-notebook ERROR!!")]}})))

#_(defn hack-gist [storage result db]
    (if (and (= (:id storage) nil)
             (= :gist (storagetype storage)))
      (do
        (routes/nav! (gorilla-path (assoc storage :id (:id result))))
        (assoc-in db [:storage :id] (:id result)))
      db))

;; display success message when saving was successful
#_(reg-event-db
   :after-save-success
   [standard-interceptors]
   (fn [db [_ storage result]]
     (info "Storage is:" storage ", result is: " result)
     (add-notification (notification :info "Notebook saved."))
     (hack-gist storage result db))   ;(routes/nav! (str "/edit?source=local&filename=" filename))
   )







