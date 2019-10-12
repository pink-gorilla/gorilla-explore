(ns gorillauniverse.github.gist
  (:require 
   [gorillauniverse.github.core :refer [user-gists specific-gist]]
   [gorillauniverse.print :refer [print-gist]]
   ))

;; GITHUB WRAPPER

(defn load-gists [user & [token]]
  (let [gists (if (nil? token)
                  (do
                     (println "loading gists anonymousely without token")
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

(defn load-gist [gist-id & [token]]
  (tentacles.gists/file-contents
   (if (nil? token)
     (specific-gist gist-id)
     (specific-gist gist-id {:oauth-token token}))))


;; Filter Clojure Files

(defn file-clojure?
  "Does git filename have a clojure extension"
  [file]
  (let [filename (:filename file)]
    (or (clojure.string/ends-with? filename ".clj")
        (clojure.string/ends-with? filename ".cljw"))))

(defn lor
  "logical or as a function (a macro cannot be reduced)"
  [a b]
  (or a b))

(defn gist-has-clojure-files? [gist]
  (let [_ (println "searching for clojure-files in gist: " (:html_url gist))
        ;_ (println gist)
        files (vals (:files gist))]
    (reduce lor false (map file-clojure? files))))

(defn clojure-gists [user & [token]]
  (let [gists (:data (load-gists user token))
        gists (if (nil? gists) [] gists)
        clojure-gists (filter gist-has-clojure-files? gists)
        _ (println user " total gists: " (count gists) " clojure gists:"  (count clojure-gists))]
    clojure-gists))

;; Content Analysis

(defn gorilla-content? [content]
  (let [r (re-find #"gorilla-repl.fileformat = 1" content)]
    (not (nil? r))))

(defn find-gorilla-fn
  "returns the filename of the first valid gorilla filename, or nil"
  [gist-id & [token]]
  (let [content-map (load-gist gist-id token)
        ;_ (println "content map: " content-map)
        filenames (remove nil? (map (fn [[filename content]]
                                      (when (gorilla-content? content) filename)) content-map))]
    (first filenames)))

(defn gorilla-gists [user & [token]]
  (let [gists (clojure-gists user token)
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
  
(comment  
  
  (keys (load-gist "55b101d84d9b3814c46a4f9fbadcf2f8"))
  (load-gist "55b101d84d9b3814c46a4f9fbadcf2f8")
  
  
  (print-gist-response (load-gists "awb99"))
  (print-gist-response (load-gists "awb99" "519fc32186567847a356a6a46277158958866bcd"))
  
  (print-gist (gorilla-gists "awb99"))
  (print-gist (gorilla-gists "awb99" "519fc32186567847a356a6a46277158958866bcd"))
  
  )