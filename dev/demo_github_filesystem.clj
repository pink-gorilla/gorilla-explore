(ns demo-github-filesystem
  (:require 
    [gorillauniverse.github.core :refer [username]]
    [gorillauniverse.github.filesystem :refer [connect browse-files load-file]]
   ))
 
(defn demo [token]
  ; Check if the token works; username returns git user-name of the token
  (println "username associated to token is:" (username token))
  ; List Gists
  ; https://gist.github.com/clojurewb/    
  (let [connection (connect token)]
    (println "personal gorilla gist filenames:" (browse-files connection))
    (println "content of clocks.cljw: " (load-filename connection "clocks.cljw")))
  )


(comment
  ; Create a personal access token to access your gists
  ; https://github.com/settings/tokens
  
  
  (demo "675d5d56c4a677e7da8e76adbfae5f2c45b2aa3a")
  
  )
