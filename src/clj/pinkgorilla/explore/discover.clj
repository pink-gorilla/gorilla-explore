(ns pinkgorilla.explore.discover
  (:require
   [taoensso.timbre :as timbre :refer [tracef debugf infof warnf errorf info]]
   [clj-time.core :as t]
   [clj-time.format :as fmt]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.explore.gist :refer [gorilla-gists]]
   [pinkgorilla.explore.repo :refer [gorilla-repos]]
   [pinkgorilla.explore.meta :refer [add-meta add-random]]
   [pinkgorilla.explore.db :refer [users add add-list usernames save-db]]
 ; [gorillauniverse.google :refer [discover-google]]
   [pinkgorilla.explore.print :refer [print-gist]]))

#_(defn add-google []
    (->> (discover-google)
       ;(clojure.pprint/print-table [:user :gists])
         (add-list)
         (vals)
         (clojure.pprint/print-table [:user :gists])))

(defn log [val name]
  (info name ": " val)
  val)

(defn github-action [type user]
  (info "discovering github " type " for user " user)
  (case type
    :gist (gorilla-gists user)
    :repo (gorilla-repos user)
    :test []))

(defn is-excluded? [storage]
  (cond
    (= (:repo storage) "notebook-encoding") true
    (= (:filename storage) "meta1.cljg") true
    (= (:filename storage) "unittest-meta1.cljg") true
    (and (:filename storage) (.contains (:filename storage) "broken/")) true
    :else false))

(defn remove-excluded [storages]
  (remove is-excluded? storages))

(defn discover-github
  "adds the gists of a github-user to the db
   It is safe to call it when github has rate-limited the ip"
  [type tokens user-name]
  (do (->> (github-action type user-name)
           (remove-excluded)
           (map (partial add-meta tokens))
           (map (partial add-random tokens))
           (remove nil?)
           (add-list))
      (save-db)))

(defn discover-github-users [type tokens user-names]
  (info "discovering for " (count user-names) "users of type:" type)
  (doseq [user-name user-names]
    (discover-github type tokens user-name))
  (info "FINISHED discovering " (count user-names) " users for type:" type))

(defn discover-github-all [type tokens]
  (discover-github-users type tokens (usernames)))




