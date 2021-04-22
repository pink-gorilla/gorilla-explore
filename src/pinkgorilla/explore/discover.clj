(ns pinkgorilla.explore.discover
  (:require
   [taoensso.timbre :as timbre :refer [tracef debugf infof warnf errorf info]]
   [pinkgorilla.document.meta.meta :refer [add-meta add-random]]
   [pinkgorilla.explore.gist :refer [gorilla-gists]]
   [pinkgorilla.explore.repo :refer [gorilla-repos]]))

(defn github-action [type user token]
  (info "discovering github " type " for user " user)
  (case type
    :gist (gorilla-gists user token)
    :repo (gorilla-repos user token)
    []))

(defn discover-github-type
  "adds the gists of a github-user to the db
   It is safe to call it when github has rate-limited the ip"
  [type tokens user-name]
  (->> (github-action type user-name tokens)
       (map (partial add-meta tokens))
       (map (partial add-random tokens))
       (remove nil?)))

(defn discover-github [tokens user-name]
  (let [gists (discover-github-type :gist tokens user-name)
        repos (discover-github-type :repo tokens user-name)]
    (concat gists repos)))

