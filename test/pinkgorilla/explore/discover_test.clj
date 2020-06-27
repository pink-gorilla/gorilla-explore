(ns pinkgorilla.explore.discover-test
  (:require
   [cheshire.core :refer :all]
   [pinkgorilla.explorer.default-config] ;side effects
   [pinkgorilla.explore.db :refer [clear all]]
   [pinkgorilla.explore.discover :refer [discover-github-users]]
   [pinkgorilla.creds :refer [creds]]))

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

(defn generate-list [users tokens]
  (let [filename "resources/list.json"
        my-pretty-printer (create-pretty-printer
                           (assoc default-pretty-print-options
                                  :indent-arrays? true))]
    (clear)
    (discover-github-users :gist tokens users)
    (discover-github-users :repo tokens users)
    (spit filename (generate-string {:data (all)} {:pretty my-pretty-printer}) :append false)
    (println "generate-list finished.")))

(comment

  (future
    (generate-list ["awb99" "pink-gorilla" "deas"] (creds)))

   ;; Add GISTs from one git-user to db
  (discover-github :gist creds "lambdaforg")
  (discover-github-users :gist creds (shuffle seed))

  (future (discover-github-users :test creds (shuffle seed)))
  (future (discover-github-users :repo creds (shuffle seed)))
  (future (discover-github-users :gist creds (shuffle seed)))

  ; comment
  )