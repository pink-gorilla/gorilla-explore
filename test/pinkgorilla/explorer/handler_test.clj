(ns pinkgorilla.explorer.handler-test
  (:require
   [bidi.bidi :as bidi]))

(comment
  (bidi/path-for demo-routes-api :demo/main)
  (bidi/path-for demo-routes-api :ui/explorer)
  (bidi/path-for demo-routes-api :ui/notebook)
  (bidi/path-for demo-routes-api :ui/unknown)

  (bidi/path-for demo-routes-api :api/explorer)
  (bidi/path-for demo-routes-api  :api/notebook-load)

  (bidi.bidi/match-route demo-routes-api "/explorer")
  (bidi.bidi/match-route demo-routes-api "/api/explorer" :request-method :get)

       ;TODO : make a unit test with this
  (def demo-load-request
    {:headers
     {"content-type" "application/edn"
      "accept" "application/transit+json"}
     :body nil
     :params {:token nil
              :storagetype :repo
              :user "pink-gorilla"
              :repo "gorilla-ui"
              :filename "notebooks/videos.cljg"}})

       ;TODO : make a unit test with this
  (notebook-load-handler demo-load-request)

       ;
  )