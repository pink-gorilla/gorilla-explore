(defproject org.pinkgorilla/gorilla-explore "0.1.10-SNAPSHOT"
  :description "Explore PinkGorilla notebooks (private and public) on github."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.1"]
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

                 [org.pinkgorilla/notebook-encoding "0.0.23"]         ; notebook encoding
                 ]
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]

  :profiles {:dev {:source-paths ["dev" "test"]
                   :dependencies [[clj-kondo "2019.11.23"]]
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


  :aliases {"bump-version" ["change" "version" "leiningen.release/bump-version"]}

  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]])
