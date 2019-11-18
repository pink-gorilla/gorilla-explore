(ns pinkgorilla.creds
  (:require
   [clojure.edn :as edn]))


;; creds.edn 
;; { :github "private-dev-token"}

(def creds
  (-> (slurp "/tmp/creds.edn")
      (edn/read-string)))