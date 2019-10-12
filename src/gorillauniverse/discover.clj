(ns gorillauniverse.discover
 (:require
  [gorillauniverse.db :refer [users add add-list usernames save-db]]
  [gorillauniverse.google :refer [discover-google]]
  [gorillauniverse.print :refer [print-gist]]
  [gorillauniverse.github.gist :refer [gorilla-gists]]
  [gorillauniverse.github.repo :refer [gorilla-repos]]
  ))

(defn add-google []
  (->> (discover-google)
       ;(clojure.pprint/print-table [:user :gists])
       (add-list)
       (vals)
       (clojure.pprint/print-table [:user :gists])))

(defn log [val name]
  (println name ": " val)
  val)


(defn github-action [type user]
  (println "discovering github " type " for user " user)
  (case type
    :gist (gorilla-gists user)
    :repo (gorilla-repos user)
    :test []))

(defn discover-github
  "adds the gists of a github-user to the db
   It is safe to call it when github has rate-limited the ip"
  [type user-name]
  (do (->> (github-action type user-name)
           (add-list))
      (save-db)))

(defn discover-github-users [type user-names]
  (println "discovering for " (count user-names) "users of type:" type)
  (doseq [user-name user-names] 
    (discover-github type user-name))
  (println "FINISHED discovering " (count user-names) type))

(defn discover-github-all [type]
  (discover-github-users type (usernames)))


(def seed [
  "lambdaforge" "ribelo" "k2n" "iantruslove" "rorokimdim" "ws"
  "bogdartysh" "decentstudio" "martinhynar" "hellonico" "lspector"
  "jguhlin"  "vegetka" "izubkov" "gavilcode"
  "rzh" "squest" "saulshanabrook" "benwiz" "valbyte96"
  "NikolaMandic" "probprog" "ksseono" "jaromil"
  "Commonfare-net" "log0ymxm" "lambdaforg" "rorokimdim"
  "bogdartysh" "htm-community" "JeffAtAtl"
  "quan-nh" "Apress" "Abhik1998" "fwood" "cognitect"
  "findmyway" "bakirillov" "whilo" "jonase"
  "rasmuserik" "dubiousdavid" "hongseok-yang"
  "kieranbrowne" "log0ymxm" "cujun" "phreed"
  "XingyuHe" "susverwimp" "deas" "JonyEpsilon"
  "NicMcPhee" "J-Atkinson" "light24bulbs" "mrcslws"])


(comment
  
  (print-gist @users)
  
  (add-google)
  
  ;; Add GISTs from one git-user to db
  (discover-github :gist "lambdaforg")
  
  (discover-github-users :gist (shuffle seed))
  
  (future (discover-github-users :test (shuffle seed)))
  (future (discover-github-users :repo (shuffle seed)))
  (future (discover-github-users :gist (shuffle seed)))
  
  )


  