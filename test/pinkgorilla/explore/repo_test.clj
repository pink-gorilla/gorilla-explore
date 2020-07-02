(ns pinkgorilla.explore.repo-test
  (:require
   [pinkgorilla.explorer.default-config] ; side-effects
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.hydration :refer [load-notebook]]
   [pinkgorilla.explore.print :refer [print-repo]]
   [pinkgorilla.explore.repo :refer [gorilla-repos]]
   [pinkgorilla.creds :refer [creds]]))

(comment

 ; (print-gist-response (load-gists "awb99" (:github-token creds)))

  (gorilla-repos "pink-gorilla")

  (print-repo (gorilla-repos "martinhynar"))

  (let [tokens {}
        storage (create-storage {:type :repo
                                 :user "pink-gorilla"
                                 :repo "sample-notebooks"
                                 :filename "samples/html-image.cljg"})]
    (load-notebook storage tokens))

  (let [tokens {}
        storage (create-storage {:type :repo
                                 :user "pink-gorilla"
                                 :repo "sample-notebooks"
                                 :filename "samples/uiplugin/gorillaplot/central-limit.cljg"})]
    (load-notebook storage tokens)))