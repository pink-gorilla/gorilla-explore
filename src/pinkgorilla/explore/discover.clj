(ns pinkgorilla.explore.discover
  (:require

  ; dependencies needed to be in cljs bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]

   [pinkgorilla.storage.storage :refer [create-storage]]
   [pinkgorilla.notebook.core :refer [notebook-load]]
   [pinkgorilla.explore.db :refer [users add add-list usernames save-db]]
 ; [gorillauniverse.google :refer [discover-google]]
   [pinkgorilla.explore.print :refer [print-gist]]
   [pinkgorilla.explore.gist :refer [gorilla-gists]]
   [pinkgorilla.explore.repo :refer [gorilla-repos]]))

#_(defn add-google []
    (->> (discover-google)
       ;(clojure.pprint/print-table [:user :gists])
         (add-list)
         (vals)
         (clojure.pprint/print-table [:user :gists])))

(defn log [val name]
  (println name ": " val)
  val)


(defn github-action [type user]
  (println "discovering github " type " for user " user)
  (case type
    :gist (gorilla-gists user)
    :repo (gorilla-repos user)
    :test []))


(defn add-meta [tokens entry]
  (let [_ (println "adding meta for entry" entry)
        storage (create-storage entry)
        ;tokens {}
        _ (println "loading notebook " storage)
        nb (notebook-load storage tokens)]
    (if (nil? nb)
      nil
      (assoc entry :meta (:meta nb)))))

(defn discover-github
  "adds the gists of a github-user to the db
   It is safe to call it when github has rate-limited the ip"
  [type tokens user-name]
  (do (->> (github-action type user-name)
           (map (partial add-meta tokens))
           (remove nil?)
           (add-list))
      (save-db)))

(defn discover-github-users [type tokens user-names]
  (println "discovering for " (count user-names) "users of type:" type)
  (doseq [user-name user-names]
    (discover-github type tokens user-name))
  (println "FINISHED discovering " (count user-names) " users for type:" type))

(defn discover-github-all [type tokens]
  (discover-github-users type tokens (usernames)))




