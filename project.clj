(defproject org.pinkgorilla/explore "0.1.3"
  :description "Explore PinkGorilla notebooks (private and public) on github."
  :url "http://example.com/FIXME"
   ;:deploy-repositories [["releases" :clojars]]
  :repositories [["clojars" {:url "https://clojars.org/repo"
                             :username "pinkgorillawb"
                             :sign-releases false}]]
  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [org.clojure/core.async "0.4.500"]
   [org.clojure/tools.cli "0.4.2"]
   [clojure.java-time "0.3.2"]
  ; dependencies used for discovery:
   [irresponsible/tentacles "0.6.6"] ; github api  ; https://github.com/clj-commons/tentacles
   
   [com.cemerick/url "0.1.1"]  ; url query-strings
   [clj-http "3.10.0"]  ; http requests
   [cheshire "5.8.1"]  ; JSON parsings
   [throttler "1.0.0"] ; api rate-limits
   [org.clojure/data.json "0.2.6"]
   [clj-time "0.15.2"]  ; datetime
   
   [org.pinkgorilla/encoding "0.0.18"]         ; notebook encoding
   ]

  :min-lein-version "2.8.3"
  :source-paths ["src"]
  :test-paths ["test"]
  :resource-paths ["resources"]
  :target-path "target/%s/"

  :main ^:skip-aot gorillauniverse.main
  :plugins []


  :profiles
  {:uberjar
   {:omit-source true
    :aot :all
    :uberjar-name "gorilla-explore.jar"}
   :dev
   {:source-paths ["dev" "test"]}})
