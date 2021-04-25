(ns index.excluded)

(defn is-excluded? [storage]
  (cond
    (= (:repo storage) "notebook-encoding") true
    (= (:filename storage) "meta1.cljg") true
    (= (:filename storage) "unittest-meta1.cljg") true
    (and (:filename storage) (.contains (:filename storage) "broken/")) true
    :else false))

(defn remove-excluded [storages]
  (remove is-excluded? storages))