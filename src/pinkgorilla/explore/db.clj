(ns pinkgorilla.explore.db
  (:require 
   [cheshire.core :refer :all]))

;; in-memory db
;; gets saved and loaded from json file at startup/modification
;; A map (by user as a key) of lists of items
;; {:users [items]}
;; item: {:user "abc" :type :description :gist-id :gist-fn}


;; HELPER FUNCTIONS
;; STOLEN FROM: https://stackoverflow.com/questions/4830900/how-do-i-find-the-index-of-an-item-in-a-vector

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn positions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (for [[idx elt] (indexed coll) :when (pred elt)] idx))

;; DB

(defonce users (atom {}))







(defn update-item-vector [item items]
  (let [id (:id item)
        ;_ (println "gist-id" id)
        ;_ (println "existing items:" items)
        existing-ids (map :id items)
        ;_ (println "existing ids:" existing-ids)
        existing-index (first (positions #{id} existing-ids))
        _ (println "existing index:" existing-index)
        ]
    (if (nil? existing-index)
      (vec (conj items item)) 
      (assoc items existing-index item))))

  
(defn add [item]
  (if (or (nil? (:user item)) (nil? (:id item)))
    (println "Not adding Item with empty username or id")
    (let [user-name (:user item)
          user-key (keyword user-name)
          ; _ (println "user-key is:" user-key)
          items-existing (user-key @users)]
      (if (nil? items-existing)
        (swap! users assoc user-key [item])
        (swap! users update-in [user-key] (partial update-item-vector item))))))
  
(defn add-list
  "adds a list of items [{:user :gists []}] to the 
   db (map of users) {:user-name  {:user user-name :gists[gist-id]}}"
  [items]
  (doseq [item items] (add item))
  @users)

(defn all []
    (flatten (vals @users)))
      
(defn gists []
  (filter #(= "gist" (:type %)) 
      (flatten (vals @users))))

(defn repos []
  (filter #(= "repo" (:type %)) 
          (flatten (vals @users))))

(defn usernames []
  (let [user-keys (keys @users)
        user-names (map name user-keys)]
    user-names))


(defn print-status [db]
  {:users (count @users)
   :repos (count (repos))})

(def json-fn (atom "universe.json"))

(defn load-db [fn]
  (-> (reset! json-fn fn)
      (slurp)
      (parse-string true)
      (:data)
      (#(reset! users %))
      (print-status)))

(defn save-db []
  (spit @json-fn (generate-string {:data @users}) :append false)
  (print-status @users)
  )


(defn clear []
   (reset! users {}))



      
      
      
      