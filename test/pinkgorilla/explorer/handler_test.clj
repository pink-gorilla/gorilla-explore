(ns pinkgorilla.explorer.handler-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [ring.mock.request :refer [request json-body] :rename {request mock-request}]
   [bidi.bidi :as bidi]
   [webly.web.handler :refer [make-handler]]
   [webly.user.app.handler :refer [app-handler]]
   [pinkgorilla.explorer.default-config :as explorer]
   [pinkgorilla.explorer.handler] ; side-effects
   ))

(def routes-backend
  ["/api/" explorer/routes-api])

(def routes-frontend
  ["/" :demo/main])

(def handler (make-handler app-handler routes-backend routes-frontend))

(defn GET [url]
  (handler (mock-request :get url)))

(defn POST [url data]
  (handler (-> (mock-request :post url)
               (json-body data))))

(defn content-type [res]
  (get-in res [:headers "Content-Type"]))

(def load-notebook-data
  {:notebook    ";; gorilla-repl.fileformat = 2\n"
   :storage {:type :file
             :filename "target/test-save.cljg"}})

(deftest save-notebook []
  (let [response (POST "/api/notebook" load-notebook-data)
        _ (println response)]
    (is (= "application/json; charset=utf-8" (content-type response)))
    (is (= 200 (:status response)))))

(comment
  (bidi/path-for routes-api :demo/main)
  (bidi/path-for demo-routes-api :ui/unknown)

  (bidi.bidi/match-route demo-routes-api "/explorer")
  (bidi.bidi/match-route routes-backend "/api/explorer" :request-method :get)

       ;TODO : make a unit test with this


       ;TODO : make a unit test with this
  (notebook-load-handler demo-load-request)

       ;
  )