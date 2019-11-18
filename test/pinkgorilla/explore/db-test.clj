(ns pinkgorilla.db-test
  (:require [pinkgorilla.explore.db :refer :all]))


(comment

  (positions #{2} [1 2 3 4 1 2 3 4])

  ;; LOAD/SAVE db
  (load-db)
  (save-db)

  ;; ADD individual gist
  (add {:user "awb99"
        :type "gist"
        :id "55b101d84d9b3814c46a4f9fbadcf2f8"
        :description "gorilla repl clocks"
        :gist-fn "clocks.cljw"})

  ;; Add List of REPOS.
  (add-list [{:user "ribelo", :type "repo", :id "1" :repo "trennwand", :repo-fn "playground.clj", :repo-path "notebooks/playground.clj"}
             {:user "ribelo", :type "repo", :id "2" :repo "trennwand", :repo-fn "percepton.clj", :repo-path "notebooks/percepton.clj"}
             {:user "ribelo", :type "repo", :id "3" :repo "machine", :repo-fn "machine_ploting"}, :repo-path "machine_ploting"])

  (usernames))