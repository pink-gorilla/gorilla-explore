(defproject org.pinkgorilla/gorilla-explore "0.2.53-SNAPSHOT"
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
  :source-paths ["src"]
  :test-paths ["test"]
  :resource-paths  ["resources"] ; gorilla-explore resources
  :prep-tasks ["md"]


  :dependencies [; dependency conflict resolution
                 [org.clojure/tools.namespace "1.1.0" ; encoding/marginalia + webly/ring-develop
                    :exclusions [[org.clojure/tools.reader]] ; old version
                  ] 
                 [ring/ring-codec "1.1.3"]  ; webly + ring-mock
                 [org.apache.httpcomponents/httpcore "4.4.14"] ; webly + clj-ajax

                 [org.pinkgorilla/webly "0.2.11"]
                 [org.clojure/clojure "1.10.3"]
                 [org.clojure/core.async "1.3.610"]
                 [com.taoensso/timbre "5.1.2"] ; clj/cljs logging
                 [clojure.java-time "0.3.2"]
                  ; dependencies used for discovery:
                 [irresponsible/tentacles "0.6.8"] ; github api  ; https://github.com/clj-commons/tentacles
                 [com.cemerick/url "0.1.1"]  ; url query-strings
                 [clj-http "3.12.1"]  ; http requests
                 [cheshire "5.10.0"]  ; JSON parsings
                 [throttler "1.0.0" ; api rate-limits ; has very old core.async
                  :exclusions  [[org.clojure/clojure]
                                [org.clojure/core.async]]]
                 [org.clojure/data.json "2.1.0"]
                 [clj-time "0.15.2"]  ; datetime
                 [net.java.dev.jna/jna "5.8.0"] ; excluded from hawk, fixes tech.ml.dataset issue
                 [hawk "0.2.11" ; file watcher
                  :exclusions [[net.java.dev.jna/jna]]] ; this breaks tech.ml.dataset and libpythonclj
                 ; [resauce "0.2.0"] ; resources
                 [clojure-interop/java.net "1.0.5"]
                 [clojure-interop/java.io "1.0.5"]

                 [cljs-ajax "0.8.3"] ; needed for re-frame/http-fx
                 [day8.re-frame/http-fx "0.2.3" ;  reframe based http requests
                  :exclusions [[re-frame]]] ; a more modern reframe comes from webly
                 [re-com "2.13.2"]      ; reagent reuseable ui components
                 ; pinkgorilla
                 [org.pinkgorilla/notebook-encoding "0.1.31"] ; notebook encoding
                 ]


  :profiles {:index {; rebuilds the index
                     :main ^:skip-aot index.main
                     :source-paths ["profiles/index/src"
                                    "test" ; so we have  test creds
                                    ]}

             :demo  {:source-paths ["profiles/demo/src"]
                     :resource-paths ["target/webly" ; bundle
                                      "profiles/demo/resources"]}

             :dev {:source-paths ["test"]
                   :dependencies [[ring/ring-mock "0.4.0"]
                                  [clj-kondo "2021.03.31"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]
                                  [lein-ancient "0.6.15"]
                                  [lein-shell "0.5.0"]]
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

  :aliases {"bump-version"
            ["change" "version" "leiningen.release/bump-version"]

            "md"  ^{:doc "Copies markdown files to resources"}
            ["shell" "./scripts/copy-md.sh"]

            "lint"  ^{:doc "Lint for dummies"}
            ["clj-kondo"
             "--config" "clj-kondo.edn"
             "--lint" "src"]

            ; Single User notebooks
            "user" ^{:doc "Rebuild the notebook index"}
            ["with-profile" "index" "run" "-m" "index.main" "user"]

            ;; INDEXER 
            "build-index" ^{:doc "Rebuild the notebook index"}
            ["with-profile" "index" "run" "-m" "index.main" "index"]

            ;; APP
            "demo"
            ["with-profile" "+demo" "run" "-m" "demo.app"]}

  ;
  )
