(ns pinkgorilla.explore.gist
  (:require
   [clojure.string]
   [taoensso.timbre :as timbre :refer [tracef debugf infof warnf errorf info]]
   [tentacles.gists]
   [pinkgorilla.explore.github-helper :refer [user-gists specific-gist]]
   [pinkgorilla.explore.print :refer [print-gist]]))

;; GITHUB WRAPPER

(defn load-gist-all [gist-id & [tokens]]
  (tentacles.gists/file-contents
   (if (nil? tokens)
     (tentacles.gists/specific-gist gist-id)
     (tentacles.gists/specific-gist gist-id tokens))))

(defn load-gists [user & [options]]
  (let [gists (if (nil? options)
                (do
                  (info "loading gists without token")
                  (user-gists user))
                (do
                  (info "loading gists for user with token: " options)
                  (user-gists user options)))
        status (:status gists)]
    (if (nil? status)
      (do (info "User " user " has total " (count gists) "gists.")
          {:data gists})
      (do (info "error fetching gists for " user)
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
                 :user (get-in gist [:owner :login])}]
    (map #(assoc storage :filename %) files)))

(defn gorilla-gists [user & [token]]
  (let [gists (:data (load-gists user token))
        gists (if (nil? gists) [] gists)
        gists-gorilla (flatten (map gist-to-storage-list gists))
        _ (info user " total gists: " (count gists) " gorilla gists:"  (count gists-gorilla))]
    gists-gorilla))

;; Content Analysis

(defn gorilla-content? [content]
  (let [r (re-find #"gorilla-repl.fileformat = 2" content)]
    (not (nil? r))))

(defn find-gorilla-fn
  "returns the filename of the first valid gorilla filename, or nil"
  [gist-id & [token]]
  (let [content-map (load-gist-all gist-id token)
        ;_ (info "content map: " content-map)
        filenames (remove nil? (map (fn [[filename content]]
                                      (when (gorilla-content? content) filename)) content-map))]
    (first filenames)))

(defn gorilla-gists2 [user & [token]]
  (let [gists (gorilla-gists user token)
        gorilla-infos (atom [])]
    (doseq [gist gists]
      (let [gist-id (:id gist)
            _ (info "gorilla search in " user " gist: " gist-id)
            fn-gorilla (find-gorilla-fn gist-id token)]
        (if (not (nil? fn-gorilla))
          (swap! gorilla-infos conj {:user user
                                     :type "gist"
                                     :id gist-id
                                     :description (:description gist)
                                     :gist-fn fn-gorilla}))))
    (info "gorilla gists discovered: " (count @gorilla-infos))
    @gorilla-infos))

(defn print-gist-response [gist-response]
  (let [gists (:data gist-response)]
    (when-not (nil? gists)
      (print-gist gists))))

