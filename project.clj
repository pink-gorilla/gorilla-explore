(defproject gorillauniverse "0.1.0-SNAPSHOT"
  :description "Discover the Universe of Gorilla Workbooks. Load Gorilla Workbooks from github."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/tools.cli "0.4.2"]
                 [clojure.java-time "0.3.2"]

                 
                 ; dependencies used for discovery:
                 [tentacles "0.5.1"] ; github api
                 [com.cemerick/url "0.1.1"]  ; url query-strings
                 [clj-http "3.10.0"]  ; http requests
                 [cheshire "5.8.1"]  ; JSON parsings
                 [throttler "1.0.0"] ; api rate-limits
                 [org.clojure/data.json "0.2.6"]
                 ]

  :min-lein-version "2.8.3"
  :source-paths ["src"]
  :resource-paths ["resources"]
  :target-path "target/%s/"

  :main ^:skip-aot gorillauniverse.main
  :plugins []


:profiles
  {:uberjar 
    {:omit-source true
     :aot :all
     :uberjar-name "gorillauniverse.jar"}
   :dev 
     {:source-paths ["dev"]}
   
   }
)
