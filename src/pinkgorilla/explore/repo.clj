(ns pinkgorilla.explore.repo
  (:require 
   [pinkgorilla.explore.github-helper :refer [search-code]]
   [pinkgorilla.print :refer [print-repo]]
   ))

(defn repo-data [user repo]
  {:user user
   :type "repo"
   :id (:sha repo) ; (:node_id repo) ; :sha might be bettwer
   :repo (get-in repo [:repository :name])
   :repo-fn (:name repo)
   :repo-path (:path repo)
   :stars (:stargazers_count repo)})

(defn gorilla-repos [user & [options]]
  (let [_ (println "discovering gorilla-repos for user " user)
        search-options {:in "file"
                        :language "clj"
                        :user user}
        r (search-code "gorilla-repl fileformat = 2" (merge search-options options) options)
        items (:items r)
        _ (println "potential gorilla-workbooks found: " (count items))]

    (->> (map (partial repo-data user) items)
         (filter #(clojure.string/ends-with? (:repo-fn %) "clj")))))
