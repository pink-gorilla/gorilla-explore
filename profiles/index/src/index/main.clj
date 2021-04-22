(ns index.main
  (:require
   [taoensso.timbre :as timbre :refer [tracef debugf infof warnf errorf info]]
   [cheshire.core :as cheshire]
   [pinkgorilla.explore.discover :refer [discover-github]]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.explorer.default-config] ; side effects
   [pinkgorilla.creds :refer [creds]] ; from test folder
   )
  (:gen-class))

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn- save [filename data]
  (let [my-pretty-printer (cheshire/create-pretty-printer
                           (assoc cheshire/default-pretty-print-options
                                  :indent-arrays? true))]
    (spit filename (cheshire/generate-string {:data data} {:pretty my-pretty-printer}) :append false)))


(defn is-excluded? [storage]
  (cond
    (= (:repo storage) "notebook-encoding") true
    (= (:filename storage) "meta1.cljg") true
    (= (:filename storage) "unittest-meta1.cljg") true
    (and (:filename storage) (.contains (:filename storage) "broken/")) true
    :else false))

(defn remove-excluded [storages]
  (remove is-excluded? storages))

(defn discover-users [tokens user-names]
  (info "discovering for " (count user-names) "users")
  (let [notebook-list (map #(discover-github tokens %) user-names)
        notebooks (->> notebook-list
                       (reduce concat [])
                       (filter remove-excluded))]
    (save "resources/list.json" notebooks)
    (info "FINISHED discovering users" (count user-names) " notebooks: " (count notebooks))))


(defn discover-user [username tokens]
  (let [notebooks (discover-github tokens username)
        f (str "profiles/index/data/" username ".json")
        ]
    (info "user data: " (pr-str notebooks))
    (save f notebooks)
    (info "user data saved to: " f)

    ))

(defn -main [mode]
  (let [tokens (creds)
        ;users (usernames)
        users ["awb99" "pink-gorilla" "deas"]]
    (info "re-building index of pink-gorilla notebooks ..")

    (case mode
      "index"
      (discover-users users tokens)

      "user"
      (discover-user "awb99" tokens))))
