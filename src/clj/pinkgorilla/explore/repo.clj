(ns pinkgorilla.explore.repo
  (:require
   [clojure.string]
   [pinkgorilla.explore.github-helper :refer [search-code]]
   [pinkgorilla.explore.print :refer [print-repo]]))

(defn repo-data [user repo]
  {:type :repo
   :user user
   :id (:sha repo) ; (:node_id repo) ; :sha might be better ; this is just for dbmanagement purpose
   :repo (get-in repo [:repository :name])
   ;:repo-fn (:name repo)
   :filename (:path repo)
   ;:stars (:stargazers_count repo)
   })

(defn gorilla-extension? [repo]
  (clojure.string/ends-with? (:filename repo) "cljg"))

(defn tap-ignore [repos]
  (let [ignored (remove gorilla-extension? repos)]
    (println "pinkgorilla-notebooks (bad extension): " (count ignored))
    (println ignored)
    repos))

(defn tap [text repos]
  (println text (count repos))
  repos)


; :total_count - The total number of found items.
;:incomplete_results - true if query exceeds the time limit.
;:items


(defn gorilla-repos [user & [options]]
  (let [_ (println "discovering pinkgorilla notebooks in github repos for user " user)
        keywords "gorilla-repl fileformat = 2"
        query {:in "file"
                        ;:language "clj"
               :user user}
        options {:per_page 100}
        r (search-code keywords query options); (merge search-options options) options)
        items (:items r)
        _ (println "total found: " (:total_count r)  " incomplete results: " (:incomplete_results r))]

    (->> (map (partial repo-data user) items)
         (tap "search results (from code-search): ")
         ;(tap-ignore) ; this are mainly encoding files that contain "fileformat = 2""
         (filter gorilla-extension?)
         (tap "search results (with .cljg extension): "))))
