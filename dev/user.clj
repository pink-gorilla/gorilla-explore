(ns user
  (:require 
   [gorillauniverse.db :refer [load-db]])
  )

(println "dev-mode - loading db.")

(load-db "resources/universe.json")
