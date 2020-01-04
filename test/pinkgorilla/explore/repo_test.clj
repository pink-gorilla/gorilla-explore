(ns pinkgorilla.explore.repo-test
  (:require

   ; dependencies needed to be in cljs bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]

   [pinkgorilla.storage.storage :refer [create-storage]]
   [pinkgorilla.notebook.core :refer [notebook-load]]

   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.explore.print :refer [print-repo]]
   [pinkgorilla.explore.repo :refer [gorilla-repos]]))

(comment

 ; (print-gist-response (load-gists "awb99" (:github-token creds)))


  (gorilla-repos "pink-gorilla")

  (print-repo (gorilla-repos "martinhynar"))

  (let [tokens {}
        storage (create-storage {:type :repo,
                                 :user "pink-gorilla",
                                 :repo "sample-notebooks",
                                 :filename "samples/html-image.cljg"})]
    (notebook-load storage tokens))

  (let [tokens {}
        storage (create-storage {:type :repo
                                 :user "pink-gorilla"
                                 :repo "sample-notebooks"
                                 :filename "samples/uiplugin/gorillaplot/central-limit.cljg"})]
    (notebook-load storage tokens)))