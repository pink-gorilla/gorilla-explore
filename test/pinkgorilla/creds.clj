(ns pinkgorilla.creds
  (:require
   [clojure.java.io :as io]
   [clojure.edn :as edn]))

(defn creds
  "Get creds from environment or file. See sample-creds.edn for an example"
  []
  (if-let [github-token (System/getenv "GITHUB_TOKEN")]
    {:github-token github-token
     :gist-id      (System/getenv "GIST_ID")}
    (-> (io/resource "creds.edn")
        (slurp)
        (edn/read-string))))
;; (comment (creds))