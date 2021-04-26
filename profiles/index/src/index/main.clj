(ns index.main
  (:require
   [taoensso.timbre :as timbre :refer [tracef debugf infof warnf errorf info]]
   [cheshire.core :as cheshire]
   [pinkgorilla.explore.discover :refer [discover-github]]
   [pinkgorilla.document.default-config] ; side effects
   [pinkgorilla.explorer.default-config] ; side effects
   [pinkgorilla.creds :refer [creds]] ; from test folder
   [index.excluded :refer [excluded?]])
  (:gen-class))

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn- save [filename data]
  (let [my-pretty-printer (cheshire/create-pretty-printer
                           (assoc cheshire/default-pretty-print-options
                                  :indent-arrays? true))]
    (spit filename 
          (cheshire/generate-string 
           {:data data} 
           {:pretty my-pretty-printer}) 
          ;:append false
          )))


(defn discover-user [tokens username]
  (let [notebooks (discover-github tokens username)
        f (str "profiles/index/data/" username ".json")]
    (save f notebooks)
    (info "user data saved to: " f)
    notebooks))


(defn discover-users [tokens user-names]
  (info "discovering for " (count user-names) "users")
  (let [notebook-list (map #(discover-user tokens %) user-names)
        notebooks (->> notebook-list
                       (reduce concat [])
                       (remove excluded?))]
    (save "resources/list.json" notebooks)
    (info "FINISHED discovering users" (count user-names) " notebooks: " (count notebooks))))


(defn -main [mode]
  (let [tokens (creds)
        ;users (usernames)
        users ["awb99" "pink-gorilla" "deas"]]
    (info "re-building index of pink-gorilla notebooks ..")

    (case mode
      "index"
      (discover-users tokens users)

      "user"
      (->> (discover-user tokens "awb99")
           (info "user data: ")))))
