(ns demo.explore-handler
" API endpoint for file-system exploration
 This returns not only filenames, but full meta-data"  
  (:require
   [clojure.tools.logging :refer [info]]
   [ring.util.response :as res]
   ;PinkGorilla Libraries
   [pinkgorilla.explore.explorer-service :refer[explore-directories start notebooks]]
   [demo.config :refer [config-server]]
   ))



(defn handler-explore-sync
  [_]
  (res/response {:data (explore-directories (:exclude config-server) (:roots config-server))}))

;; Async

(defn explore-directories-start []
    (info "exploring setting: " config-server)
    (start (:excludes config-server) (:roots config-server)))

(defn handler-explore-async
  [_]
  (println "handler-explore-async")
  (let [r (res/response {:data (notebooks)})]
    ;(println "r: " r)
  r
  ))



