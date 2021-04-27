(ns pinkgorilla.explorer.route-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [bidi.bidi :as bidi]
   [pinkgorilla.explorer.default-config :as explorer]))

(def routes-backend
  ["/api/" explorer/routes-api])

(deftest handler->path []
  (is (= (bidi/path-for routes-backend :api/explorer) "/api/explorer"))
  (is (= (bidi/path-for routes-backend :api/notebook-load) "/api/notebook"))
  (is (= (bidi/path-for routes-backend :api/notebook-save) "/api/notebook")))

(defn GET [url]
  (bidi/match-route routes-backend url :request-method :get))

(defn POST [url]
  (bidi/match-route routes-backend url :request-method :post))

(deftest path->handler []
  (is (= (:handler (GET "/api/explorer")) :api/explorer))
  (is (= (:handler (GET "/api/notebook")) :api/notebook-load))
  (is (= (:handler (POST "/api/notebook")) :api/notebook-save)))

(comment
  (bidi/path-for routes-api :demo/main)
  (bidi/path-for demo-routes-api :ui/unknown)

  (bidi.bidi/match-route demo-routes-api "/explorer")
  (bidi.bidi/match-route routes-backend "/api/explorer" :request-method :get))