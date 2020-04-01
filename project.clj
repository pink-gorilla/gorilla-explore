(defproject org.pinkgorilla/gorilla-explore "0.1.19-SNAPSHOT"
  :description "Explore PinkGorilla notebooks (private and public) on github."
  :url "https://github.com/pink-gorilla/gorilla-explore"
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.1.582"]
                 [org.clojure/tools.cli "1.0.194"]
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
                 [hawk "0.2.11"] ; file watcher
                 [org.pinkgorilla/notebook-encoding "0.0.28"]         ; notebook encoding
                 ]


  :profiles {:index {; rebuilds the index
                     :main ^:skip-aot index.main
                     :source-paths ["src" "src-index" "test"]}

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

  :aliases {"bump-version" ["change" "version" "leiningen.release/bump-version"]
            "build-index" ^{:doc "Rebuild the notebook index"}
            ["with-profile" "index" "run" "-m" "index.main"]}

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]])
