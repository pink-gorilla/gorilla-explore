(defproject org.pinkgorilla/gorilla-explore "0.2.45"
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
  :source-paths ["src/clj"
                 "src/cljc"
                 "src/cljs"] ; cljs has to go into jar too.
  :test-paths ["test"]

  :resource-paths  ["resources"] ; gorilla-explore resources

    :prep-tasks ["md"]

  :managed-dependencies [[joda-time "2.10.6"]
                         [clj-time "0.15.2"]
                         [com.fasterxml.jackson.core/jackson-core "2.11.2"]
                         [com.cognitect/transit-clj "1.0.324"]
                         [com.cognitect/transit-cljs "0.8.264"]

                         [com.cognitect/transit-java "1.0.343"]
                         [ring/ring-codec "1.1.2"] ; old dep from ring-mock
                         [com.google.javascript/closure-compiler-unshaded "v20200504"]
                         [com.google.code.findbugs/jsr305 "3.0.2"]

                         [org.clojure/clojurescript "1.10.773"]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.610"]
                 [com.taoensso/timbre "5.1.2"] ; clj/cljs logging
                 [clojure.java-time "0.3.2"]
                  ; dependencies used for discovery:
                 [irresponsible/tentacles "0.6.6"] ; github api  ; https://github.com/clj-commons/tentacles
                 [com.cemerick/url "0.1.1"]  ; url query-strings
                 [clj-http "3.10.1"]  ; http requests
                 [cheshire "5.10.0"]  ; JSON parsings
                 [throttler "1.0.0" ; api rate-limits ; has very old core.async
                  :exclusions  [[org.clojure/clojure]
                                [org.clojure/core.async]]]
                 [org.clojure/data.json "1.0.0"]
                 [clj-time "0.15.2"]  ; datetime
                 [net.java.dev.jna/jna "5.6.0"] ; excluded from hawk, fixes tech.ml.dataset issue
                 [hawk "0.2.11" ; file watcher
                  :exclusions [[net.java.dev.jna/jna]]] ; this breaks tech.ml.dataset and libpythonclj
                 [cljs-ajax "0.8.0"] ; needed for re-frame/http-fx
                 [day8.re-frame/http-fx "0.2.3" ;  reframe based http requests
                  :exclusions [[re-frame]]] ; a more modern reframe comes from webly
                 [re-com "2.13.2"]      ; reagent reuseable ui components
                 ; pinkgorilla
                 [org.pinkgorilla/notebook-encoding "0.1.18"] ; notebook encoding
                 ]

 ; :jvm-opts ["-Dtrust_all_cert=true" ; used when ssl certs are fucked up
 ;              ;"-Djavax.net.ssl.trustStore=/home/andreas/.keystore"
 ;            ]


  :profiles {:index {; rebuilds the index
                     :main ^:skip-aot index.main
                     :source-paths ["profiles/index/src" "test"]}

             :demo  {:source-paths ["profiles/demo/src"
                                    "test"]
                     :resource-paths ["target/webly" ; bundle
                                      "profiles/demo/resources"
                                      ]}

             :dev {:source-paths ["profiles/dev/src"
                                  "test"]
                   :dependencies [;[thheller/shadow-cljs "2.10.15"]
                                  [org.pinkgorilla/webly "0.1.12"]
                                  [ring/ring-mock "0.4.0"]
                                  [clj-kondo "2021.03.31"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]
                                  [lein-ancient "0.6.15"]
                                   [lein-shell "0.5.0"]
                                  ]
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


            ;; INDEXER 

            "build-index" ^{:doc "Rebuild the notebook index"}
            ["with-profile" "index" "run" "-m" "index.main"]

            ;; SHADOW testing

            ; this will be removed when shadow package.json issue is resolved.
            "build-shadow-ci"  ^{:doc "compiles bundle"}
            ["with-profile" "+demo" "run" "-m" "shadow.cljs.devtools.cli" "compile" "webly"]

            ;; APP

            "demo"  ^{:doc "Runs UI components via webserver."}
            ["with-profile" "+demo" "run" "-m" "demo.app" "watch"]

            "build-dev"  ^{:doc "compiles bundle via webly"}
            ["with-profile" "+demo" "run" "-m" "demo.app" "compile"]

            "build"  ^{:doc "compiles bundle via webly"}
            ["with-profile" "+demo" "run" "-m" "demo.app" "release"]

            "run-web"  ^{:doc "runs compiles bundle on shadow dev server"}
            ["with-profile" "+demo" "run" "-m" "demo.app" "run"]})
