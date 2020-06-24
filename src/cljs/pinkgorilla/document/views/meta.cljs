(ns pinkgorilla.document.views.meta)


(defn notebook-page [& args]
  (println "e/nb! args: " args)
  [:h1 "Notebook Viewer"])


(defn document-view-meta [document]
  (let [d @document]
    [:div
     [:h1 "Document Meta Info"]

     [:p " storage " (pr-str (:storage d))]
     [:p " tagline " (pr-str (:tagline d))]

     [:p " meta " (pr-str (:meta d))]]))