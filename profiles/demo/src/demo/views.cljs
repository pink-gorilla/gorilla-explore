(ns demo.views
  (:require
   [reagent.core :as r]
   [pinkgorilla.storage.protocols :refer [gorilla-path]]
   [pinkgorilla.explore.component :refer [notebook-explorer]]
   [pinkgorilla.document.component :refer [notebook-page]]
   [pinkgorilla.meta.view :refer [document-view-meta]]
   [demo.routes :refer [current]]))

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
  (nav! (notebook-link nb)))

 


(defn app []
  [:div
   [:link {:rel "stylesheet" :href "tailwindcss/dist/tailwind.css"}]
   [:h1 "explorer-ui"]
   [:p (str "route: " (pr-str @current))]
   (case (:handler @current)
     :ui/explorer [notebook-explorer open-notebook]
     :ui/notebook [notebook-page document-view-meta]
     ;  :system [system (:system-id route-params)]
     [:h1 "route handler not found: "])])


