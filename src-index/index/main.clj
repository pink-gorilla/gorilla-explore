(ns index.main
  (:require
   [cheshire.core :as cheshire]
    ; dependencies needed to be in bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]
  ;Gorilla Explore
   [pinkgorilla.explore.db :refer [load-db clear all]]
   [pinkgorilla.explore.discover :refer [discover-github-users]]
  ; from test folder
   [pinkgorilla.creds :refer [creds]])
  (:gen-class))


(defn generate-list [users tokens]
  (let [filename "resources/list.json"
        my-pretty-printer (cheshire/create-pretty-printer
                           (assoc cheshire/default-pretty-print-options
                                  :indent-arrays? true))]
    (clear)
    (discover-github-users :gist tokens users)
    (discover-github-users :repo tokens users)
    (spit filename (cheshire/generate-string {:data (all)} {:pretty my-pretty-printer}) :append false)
    (println "generate-list finished.")))

(defn -main [& args]
  (println "re-building index of pink-gorilla notebooks ..")
  (load-db "resources/explorer.json") ; hACK - otherwise db gets saved to just universe.json
  (generate-list ["awb99" "pink-gorilla" "deas"] (creds)))
