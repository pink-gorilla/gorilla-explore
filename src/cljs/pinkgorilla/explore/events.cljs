(ns pinkgorilla.explore.events
  (:require
   [re-frame.core :refer [reg-event-db trim-v dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.explore.events-fetch])) ; side effects

;; DB

(reg-event-db
 :explore/init
 (fn [db [_ config]]
   (let [db (or db {})]
     (let [root-names (->> (:repositories config)
                           (map :name))
           add (fn [acc n] (assoc acc n []))
           notebooks (reduce add {} root-names)]
       (info "explore init roots:" root-names " notebooks " notebooks)
       (dispatch [:explorer/fetch-indices])
       (assoc db :explorer
              {:config config
               :notebooks notebooks
               :search {:tags #{}
                        :text ""
                        :root "all"}})))))

;; SEARCH


(reg-event-db
 :explorer/show
 [trim-v]
 (fn [db [tags]]
   (info ":explorer-show tags: " tags)
   (-> db
       (assoc-in [:explorer :search :tags] tags))))

(reg-event-db
 :explorer/search-text
 (fn [db [_ text]]
   (assoc-in db [:explorer :search :text] text)))

(reg-event-db
 :explorer/set-search-root
 (fn [db [_ root]]
   (assoc-in db [:explorer :search :root] root)))

(reg-event-db
 :explorer/toggle-tag
 [trim-v]
 (fn [db [tag]]
   (let [tags-set (get-in db [:explorer :search :tags])
         tags-set-new (if (tags-set tag) (disj tags-set tag) (conj tags-set tag))]
     (assoc-in db [:explorer :search :tags] tags-set-new))))




