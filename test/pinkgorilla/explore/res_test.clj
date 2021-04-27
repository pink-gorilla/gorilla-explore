(ns pinkgorilla.explore.res-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [pinkgorilla.explore.resource :refer [explore-resources]]))

(deftest res-browse []
  (let [nbs (explore-resources "notebooks")
        files (map :filename nbs)
        _ (println files)]
    (is (= files '("document/specs-doc.cljg"
                   "explorer/specs.cljg")))
    ;(is (= 200 (:status response)))
    ))