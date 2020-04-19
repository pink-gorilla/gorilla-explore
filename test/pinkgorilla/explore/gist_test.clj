(ns pinkgorilla.explore.gist-test
  (:require
   [pinkgorilla.creds :refer [creds]]
   [pinkgorilla.explore.gist :refer [load-gists print-gist-response  gorilla-gists]]))

(comment

  (load-gists "awb99")
  (print-gist-response (load-gists "awb99"))
  (print-gist-response (load-gists "awb99" (:github-token creds)))

  (gorilla-gists "awb99")
  (gorilla-gists "awb99" (:github-token creds))

  ;comment
  )