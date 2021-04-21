(ns gorillauniverse.github.filesystem
  (:require 
   [gorillauniverse.github.core :refer [username]]
   [gorillauniverse.github.gist :refer [gorilla-gists load-gist]]
   ))

(defn load-gist-file- [repos file-name]
  (let [file-name-as-key (keyword file-name)
        gist-info (first (filter #(= (:gist-fn %) file-name-as-key) repos))
        gist (load-gist (:id gist-info))]
    (file-name-as-key gist)))


(defn workbooks-for-token [token]
  (let [user (username token)
        gists (gorilla-gists user token)]
    {:user user :data gists}))

(defn connect [token]
  (let [db (atom 
            {:token token
             :user (username token)
             :repos (get (workbooks-for-token token) :data)
             })]
    db))

(defn browse-files [connection]
  (map #(name (:gist-fn %)) (:repos @connection)))
  
(defn load-filename 
  [connection file-name] 
  (load-gist-file- (:repos @connection) file-name))



(comment 

  ; for sample how to use filesystem look at dev/demo_github_filesystem.clj
    
  (def token "")
  (def gist-id "4a06fe58b190b6f03010bfa6597bd0a7")
  (workbooks-for-token token)
  (load-gist gist-id token)
  
)
 
