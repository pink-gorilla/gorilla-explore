(ns demo.views
  (:require
   [reagent.core :as r]
   [pinkgorilla.explore.component :refer [notebook-explorer]]))

(defn open-notebook [nb]
  (println "load-notebook-click" nb))


(defn app []
  [:div
   [:link {:rel "stylesheet" :href "tailwindcss/dist/tailwind.css"}]
   [:h1 "explorer-ui"]
   [notebook-explorer open-notebook]])
