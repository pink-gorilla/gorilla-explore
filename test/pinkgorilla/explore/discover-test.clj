(ns pinkgorilla.explore.discover-test
  (:require

   [cheshire.core :refer :all]

    ; dependencies needed to be in cljs bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]

   [pinkgorilla.explore.db :refer [clear all]]
   [pinkgorilla.explore.discover :refer [discover-github-users]]))


(def seed ["lambdaforge" "ribelo" "k2n" "iantruslove" "rorokimdim" "ws"
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


(defn generate-list [users]
  (clear)
  (discover-github-users :gist users)
  (spit "resources/list.json" (generate-string {:data (all)}) :append false)
  (println "generate-list finished."))


(comment

  (print-gist @users)

  (add-google)

  ;; Add GISTs from one git-user to db
  (discover-github :gist "lambdaforg")


  (generate-list ["awb99" "pink-gorilla" "deas"])

  (discover-github-users :gist (shuffle seed))

  (future (discover-github-users :test (shuffle seed)))
  (future (discover-github-users :repo (shuffle seed)))
  (future (discover-github-users :gist (shuffle seed))))