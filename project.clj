(defproject org.pinkgorilla/gorilla-explore "0.2.2-SNAPSHOT"
  :description "Explore PinkGorilla notebooks (private and public) on github."
  :url "https://github.com/pink-gorilla/gorilla-explore"
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]

  :target-path  "target/jar"
  :source-paths ["src/clj"] ; "test"
  ;:test-paths ["test"]
  :resource-paths  ["resources" ; not from npm
                    #_"target/node_modules"] ; css png resources from npm modules

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.1.582"]
                 [org.clojure/tools.cli "1.0.194"]
                 [com.taoensso/timbre "4.10.0"] ; cljs logging

                 [clojure.java-time "0.3.2"]
                  ; dependencies used for discovery:
                 [irresponsible/tentacles "0.6.6"] ; github api  ; https://github.com/clj-commons/tentacles
                 [com.cemerick/url "0.1.1"]  ; url query-strings
                 [clj-http "3.10.0"]  ; http requests
                 [cheshire "5.10.0"]  ; JSON parsings
                 [throttler "1.0.0" ; api rate-limits ; has very old core.async
                  :exclusions  [[org.clojure/clojure]
                                [org.clojure/core.async]]]
                 ;[org.pinkgorilla/throttler "1.0.2"] ; throtteling
                 [org.clojure/data.json "1.0.0"]
                 [clj-time "0.15.2"]  ; datetime
                 [net.java.dev.jna/jna "5.2.0"] ; excluded from hawk, fixes tech.ml.dataset issue
                 [hawk "0.2.11" ; file watcher
                  :exclusions [[net.java.dev.jna/jna]]] ; this breaks tech.ml.dataset and libpythonclj
                 [org.pinkgorilla/notebook-encoding "0.1.2"] ; notebook encoding
                 [bidi "2.1.6"]]


  :profiles {:index {; rebuilds the index
                     :main ^:skip-aot index.main
                     :source-paths ["profiles/index/src" "test"]}

             :demo  {:source-paths ["src/cljs"
                                    "profiles/demo/src"
                                    "test"]
                     :dependencies [[org.clojure/clojure "1.10.1"]
                                   ; shadow-cljs MAY NOT be a dependency in lein deps :tree -> if so, bundeler will fail because shadow contains core.async which is not compatible with self hosted clojurescript
                                    [thheller/shadow-cljs "2.8.81"]
                                    [thheller/shadow-cljsjs "0.0.21"]
                                    [org.clojure/clojurescript "1.10.773"]
                                    [reagent "0.10.0"
                                     :exclusions [org.clojure/tools.reader
                                                  cljsjs/react
                                                  cljsjs/react-dom]]
                                    [re-frame "0.10.9"]
                                    [cljs-ajax "0.8.0"] ; needed for re-frame/http-fx
                                    [day8.re-frame/http-fx "0.1.6"] ; reframe based http requests

                                    [clj-commons/pushy "0.3.10"]
                                    [ring/ring-core "1.8.1"]
                                    [ring/ring-defaults "0.3.2"
                                     :exclusions [javax.servlet/servlet-api]]
                                    [ring-middleware-format "0.7.4"] ; replaced by muuntaja
                                    [metosin/muuntaja "0.6.7"] ; 30x faster than ring-middleware-format
                                    [ring/ring-json "0.5.0"]
                                    [bk/ring-gzip "0.3.0"] ; from oz
                                    [clj-http "3.10.1"]
                                    [re-com "2.8.0"]      ; reagent reuseable ui components
                                    ]}


             :dev {:source-paths ["dev" "test"]
                   :dependencies [[clj-kondo "2020.03.20"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]]
                   :aliases      {"clj-kondo" ["run" "-m" "clj-kondo.main"]}
                   :cloverage    {:codecov? true
                                  ;; In case we want to exclude stuff
                                  ;; :ns-exclude-regex [#".*util.instrument"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                  }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                   :cljfmt       {:indents {as->                [[:inner 0]]
                                            with-debug-bindings [[:inner 0]]
                                            merge-meta          [[:inner 0]]
                                            try-if-let          [[:block 1]]}}}}
  :plugins [[lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]]

  :aliases {"bump-version"
            ["change" "version" "leiningen.release/bump-version"]

            "build-index" ^{:doc "Rebuild the notebook index"}
            ["with-profile" "index" "run" "-m" "index.main"]

            "demo"  ^{:doc "Runs UI components via webserver."}
            ["with-profile" "cljs" "shell" "shadow-cljs" "watch" "demo"]

            "demo2"  ^{:doc "Runs UI components via webserver."}
            ["with-profile" "demo" "run" "-m" "demo.app"]})
