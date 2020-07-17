(ns pinkgorilla.explore.events-fetch
  "load list of explored notebooks"
  (:require
   [taoensso.timbre :as timbre :refer-macros [debug info error]]
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [pinkgorilla.storage.protocols :refer [create-storage]]))

(reg-event-fx
 :explorer/fetch-index
 (fn [{:keys [db]} [_ repository]]
   {:db         db
    :http-xhrio {:method          :get
                 :uri             (:url repository)
                 :timeout         10000                    ;; optional
                 :response-format (ajax/json-response-format {:keywords? true}) ; (ajax/transit-response-format) ;; IMPORTANT!: You must provide this.
                 :on-success      [:explorer/fetch-success (:name repository)]
                 :on-failure      [:explorer/fetch-error (:url repository)]}}))

(reg-event-db
 :explorer/fetch-error
 (fn [db [_ url response]]
   (error "Explorer fetch-error: " url " response: " response)
   (dispatch [:notification/show
              (str "Explorer fetch error url: " url)
               ; Error: " (:status-text response) " (" (:status response) ")")
              :warning])
   db))

(defn remove-repo-id [item]
  (if (= (:type item) :repo)
    (dissoc item :id)
    item))

(defn add-storage [item]
  (assoc item :storage (create-storage item)))

(defn as-keyword [i]
  (if (:keywords? i) i (keyword i)))

(defn preprocess-item [start-index idx item]
  (-> item
      (assoc :type (as-keyword (:type item)) :index (+ start-index idx))
      (remove-repo-id)
      (add-storage)))

(defn preprocess-list [start-index response]
  (let [list (:data response)]
    (vec (map-indexed (partial preprocess-item start-index) list))))

(reg-event-db
 :explorer/fetch-success
 (fn [db [_ name response]]
   (debug "index response: " response)
   (let [start-index 0
         new-data (preprocess-list start-index response)]
     (-> (assoc-in db [:explorer :notebooks name] new-data)))))

(reg-event-fx
 :explorer/fetch-indices
 (fn [{:keys [db]}]
   (let [events (->> (get-in db [:explorer :config :repositories])
                     (map (fn [repo] (conj [:explorer/fetch-index] repo)))
                     vec)]
     {:db         db
      :dispatch-n events})))
