(ns user
  (:require 
   [pinkgorilla.explore.db :refer [load-db]])
  )

(println "dev-mode - loading db.")

;(load-db "resources/universe.json")

(load-db "resources/explorer.json")