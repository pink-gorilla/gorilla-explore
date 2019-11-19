(ns pinkgorilla.explore.repo
  (:require 
   [pinkgorilla.explore.github-helper :refer [search-code]]
   [pinkgorilla.explore.print :refer [print-repo]]
   ))

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
  (clojure.string/ends-with? (:filename repo) "cljg")
  )

(defn tap-ignore [repos]
  (let [ignored (remove gorilla-extension? repos)]
    (println "pinkgorilla-notebooks ignored: " (count ignored))
    (println ignored)
    repos))

(defn tap [repos]
  (println "pinkgorilla-notebooks found: " (count repos))
  repos
  )

(defn gorilla-repos [user & [options]]
  (let [_ (println "discovering pinkgorilla notebooks in github repos for user " user)
        search-options {:in "file"
                        ;:language "clj"
                        :user user}
        r (search-code "gorilla-repl fileformat = 2" (merge search-options options) options)
        items (:items r)
        _ (println "potential gorilla-workbooks found: " (count items))]

    (->> (map (partial repo-data user) items)
         ;(tap-ignore)
         (filter gorilla-extension?)
         (tap)
         )))
