(ns pinkgorilla.explore.events
  (:require
   [re-frame.core :refer [reg-event-db trim-v dispatch]]
   [taoensso.timbre :as timbre :refer [debug info warn error]]
   [pinkgorilla.explore.events-fetch]; side effects
   ))

(reg-event-db
 :explorer/init
 (fn [db [_ config]]
   (let [db (or db {})]
     (info "explorer init ..")
     (dispatch [:explorer/fetch-indices])
     (assoc db :explorer
            {:config config
             :notebooks []
             :search {:tags #{}
                      :text ""}}))))

(reg-event-db
 :explorer/show
 [trim-v]
 (fn [db [tags]]
   (println ":explorer-show tags: " tags)
   (-> db
       ;(assoc-in [:main] :explore)
       (assoc-in [:explorer :search :tags] tags))))

(reg-event-db
 :explorer/search-text
 (fn [db [_ text]]
   (assoc-in db [:explorer :search :text] text)))

(reg-event-db
 :explorer/toggle-tag
 [trim-v]
 (fn [db [tag]]
   (let [tags-set (get-in db [:explorer :search :tags])
         tags-set-new (if (tags-set tag) (disj tags-set tag) (conj tags-set tag))]
     (assoc-in db [:explorer :search :tags] tags-set-new))))




