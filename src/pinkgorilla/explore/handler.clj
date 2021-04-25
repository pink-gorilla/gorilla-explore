(ns pinkgorilla.explore.handler
  " API endpoint for file-system exploration
 This returns not only filenames, but full meta-data"
  (:require
   [taoensso.timbre :as timbre :refer [tracef debug debugf info infof warnf errorf info]]
   [ring.util.response :as res]
   [pinkgorilla.explore.github-helper :refer [options-oauth2]]
   [pinkgorilla.explore.service :refer [explore-directories start notebooks]]
   [pinkgorilla.explore.discover :refer [discover-github]]))

#_(defn handler-explore-sync
    [_]
    (res/response {:data (explore-directories (:exclude config-server) (:roots config-server))}))

;; Async

(defn explore-directories-start [config-server]
  (info "exploring setting: " config-server)
  (start (:excludes config-server)
         (:roots config-server)
         (:resource-root-path config-server)))

(defn handler-explore-async
  [req]
  (info "handler-explore-async")
  (let [n (notebooks)]
    (info "explorer index - nb count: " (count n))
    (res/response {:data n})))


; my github notebooks


(defn handler-my-github-notebooks
  [req]
  (let [params (:params req)
        {:keys [token-github user]} params
        opts (options-oauth2 token-github)]
    (debug "my github notebooks user: " user " tokens" (pr-str params) "opts: " opts)
    (res/response {:data  (discover-github opts user)})))


