(ns demo.views
  (:require
   [reagent.core :as r]
   [pinkgorilla.storage.protocols :refer [gorilla-path]]
   [pinkgorilla.explore.component :refer [notebook-explorer]]
   [pinkgorilla.document.component :refer [document-page]]
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

(defn document-view-dummy [storage document]
  [:div.m-16.bg-orange-400
   [:h1 "Document Meta Info - Replace me with your document viewer!"]
   [:p " storage full: " (pr-str storage)]
   [:p " meta full: " (pr-str meta)]
   [:p " tags (string): " (pr-str (:tags meta))]]) 


(defn app []
  [:div
   [:link {:rel "stylesheet" :href "tailwindcss/dist/tailwind.css"}]
   ;[:h1 "explorer-ui"]
   ;[:p (str "route: " (pr-str @current))]
   (case (:handler @current)
     :ui/explorer [notebook-explorer open-notebook]
     :ui/notebook [document-page document-view-dummy]
     ;  :system [system (:system-id route-params)]
     [:h1 "route handler not found: "])])


