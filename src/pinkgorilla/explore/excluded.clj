(ns pinkgorilla.explore.excluded)

(defn excluded? [storage]
  (println "is-excluded: " storage)
  (cond
    (= (:repo storage) "notebook-encoding") true
    (= (:repo storage) "notebook-clj") true
    (= (:repo storage) "gorilla-notebook") true
    (= (:repo storage) "kernel-cljs-shadow") true
    (= (:filename storage) "meta1.cljg") true
    (= (:filename storage) "unittest-meta1.cljg") true
    (and (:filename storage) (.contains (:filename storage) "broken/")) true
    :else false))

