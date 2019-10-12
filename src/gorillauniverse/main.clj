(ns gorillauniverse.main
  (:require 
   [gorillauniverse.db :refer [load-db]])
  (:gen-class))


;(load-db)

(defn -main [& args]
    (println "GorillaUniverse loaded."))

