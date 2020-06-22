(ns demo.views
  (:require
   [reagent.core :as r]
   [pinkgorilla.storage.storage :refer [gorilla-path]]
   [pinkgorilla.explore.component :refer [notebook-explorer]]))

(defn nav! [url]
(set! (.-location js/window) url))

(defn notebook-link [notebook]
    (let [storage (:storage notebook)]
      (println "storage: " storage)
      (if (nil? storage)
        ""
        (gorilla-path storage))))

(defn open-notebook [nb]
  (println "load-notebook-click" nb)
  (println "gorilla path: " (notebook-link nb))
  (nav! (notebook-link nb))
  )


(defn app []
  [:div
   [:link {:rel "stylesheet" :href "tailwindcss/dist/tailwind.css"}]
   [:h1 "explorer-ui"]
   [notebook-explorer open-notebook]])
