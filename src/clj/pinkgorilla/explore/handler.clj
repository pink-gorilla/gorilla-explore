(ns pinkgorilla.explore.handler
  " API endpoint for file-system exploration
 This returns not only filenames, but full meta-data"
  (:require
   [taoensso.timbre :as timbre :refer [tracef debugf info infof warnf errorf info]]
   [ring.util.response :as res]
   [pinkgorilla.explore.service :refer [explore-directories start notebooks]]))

#_(defn handler-explore-sync
    [_]
    (res/response {:data (explore-directories (:exclude config-server) (:roots config-server))}))

;; Async

(defn explore-directories-start [config-server]
  (info "exploring setting: " config-server)
  (start (:excludes config-server) (:roots config-server)))

(defn handler-explore-async
  [req]
  (info "handler-explore-async")
  (let [n (notebooks)]
    (info "explorer index - nb count: " (count n))
    (res/response {:data n})))



