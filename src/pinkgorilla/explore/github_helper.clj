(ns pinkgorilla.explore.github-helper
  (:require
   [clojure.string]
   [clojure.pprint]
   [tentacles.repos]
   [tentacles.gists]
   [tentacles.search]
   [tentacles.core :only [api-call no-content?]]
   [tentacles.data]
   [tentacles.oauth]
   [tentacles.users]
   [throttler.core :refer [throttle-chan throttle-fn fn-throttler]]
   [webly.config :refer [get-in-config]]))


; For oauth use
;  :oauth-token <token> 

; for oauth2, include 
; :client-id <client_id> 
; :client-token <client_token> in the options map.


(defn options-oauth2 [token-github]
  (when token-github
    {;:client-id (get-in-config [:oauth2 :github :client-id])
   ;:client-token token-github
     :oauth-token token-github}))


;; gist url format:
;; https://gist.github.com/santisbon/2e1e9c69b58bdf4c9624219a44d40d83

;; SEARCH ON GITHUB;
;; The github api requires to SPECIFY A REPO or a USER.
;; https://api.github.com/search/code?q=addClass+in:file+language:js+repo:jquery/jquery"
;; The UI allows to search without specifying a REPO or USER:
;; https://github.com/search?l=Clojure&q=gorilla-repl+fileformat&type=Code


;; https://developer.github.com/v3/rate_limit/
;; https://developer.github.com/apps/building-github-apps/understanding-rate-limits-for-github-apps/

;                 requests/hour
;                 REST SEARCH
; authenticated   5000 30
; unauthenticated   60 10

; Override raw github fetch functions with throttled versions


(def throttle? false)

(if throttle?
  (do
    (def throttler-rest (fn-throttler 50 :hour))
    (def throttler-search (fn-throttler 8 :hour))
    ; REPO Throttling
    (def user-repos (throttler-rest tentacles.repos/user-repos))
    (def tags (throttler-rest tentacles.repos/tags))
    ; GIST Throttling
    (def user-gists (throttler-rest tentacles.gists/user-gists))
    (def specific-gist (throttler-rest tentacles.gists/specific-gist))
    ; SEARCH Throttling
    (def search-code (throttler-search tentacles.search/search-code)))
  (do ; UNTHROTTLED
    ; REPO
    (def user-repos tentacles.repos/user-repos)
    (def tags tentacles.repos/tags)
    ; GIST
    (def user-gists tentacles.gists/user-gists)
    (def specific-gist tentacles.gists/specific-gist)
    ; SEARCH
    (def search-code tentacles.search/search-code)
    ; OAUTH
    (def me tentacles.users/me)))

(defn username [token]
  (:login (me {:oauth-token token})))







