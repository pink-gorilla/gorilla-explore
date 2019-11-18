(ns pinkgorilla.explore.gist
  (:require
   [pinkgorilla.storage.github :refer [load-gist-all]]
   [pinkgorilla.explore.github-helper :refer [user-gists specific-gist]]
   [pinkgorilla.explore.print :refer [print-gist]]))

;; GITHUB WRAPPER

(defn load-gists [user & [token]]
  (let [gists (if (nil? token)
                (do
                  (println "loading gists without token")
                  (user-gists user))
                (do
                  (println "loading gists for user with token: " token)
                  (user-gists user {:oauth-token token})))
        status (:status gists)]
    (if (nil? status)
      (do (println "User " user " has total " (count gists) "gists.")
          {:data gists})
      (do (println "error fetching gists for " user)
          {:error gists}))))


;; Filter Gorilla Files

(defn file-gorilla?
  "Does git filename have a pinkgorilla extension"
  [file]
  (let [filename (:filename file)]
    (clojure.string/ends-with? filename ".cljg")))

(defn gorilla-files [gist]
  (let [files (vals (:files gist))]
    (->> (filter file-gorilla? files)
         (map :filename)
         (vec))))

(defn gist-to-storage-list [gist]
  (let [files (gorilla-files gist)
        storage {:type :gist
                 :id (:id gist) 
                 :description (:description gist) 
                 :user (get-in gist [:owner :login])}
        ]
    (map #(assoc storage :filename %) files)
    )
  )


(defn gorilla-gists [user & [token]]
  (let [gists (:data (load-gists user token))
        gists (if (nil? gists) [] gists)
        gists-gorilla (flatten (map gist-to-storage-list gists))
        _ (println user " total gists: " (count gists) " gorilla gists:"  (count gists-gorilla))]
    gists-gorilla))

;; Content Analysis

(defn gorilla-content? [content]
  (let [r (re-find #"gorilla-repl.fileformat = 2" content)]
    (not (nil? r))))

(defn find-gorilla-fn
  "returns the filename of the first valid gorilla filename, or nil"
  [gist-id & [token]]
  (let [content-map (load-gist-all gist-id token)
        ;_ (println "content map: " content-map)
        filenames (remove nil? (map (fn [[filename content]]
                                      (when (gorilla-content? content) filename)) content-map))]
    (first filenames)))

(defn gorilla-gists2 [user & [token]]
  (let [gists (gorilla-gists user token)
        gorilla-infos (atom [])]
    (doseq [gist gists]
      (let [gist-id (:id gist)
            _ (println "gorilla search in " user " gist: " gist-id)
            fn-gorilla (find-gorilla-fn gist-id token)]
        (if (not (nil? fn-gorilla))
          (swap! gorilla-infos conj {:user user
                                     :type "gist"
                                     :id gist-id
                                     :description (:description gist)
                                     :gist-fn fn-gorilla}))))
    (println "gorilla gists discovered: " (count @gorilla-infos))
    @gorilla-infos))


(defn print-gist-response [gist-response]
  (let [gists (:data gist-response)]
    (when-not (nil? gists)
      (print-gist gists))))

