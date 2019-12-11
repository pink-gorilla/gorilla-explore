(ns gorillauniverse.google
  (:require
    [clojure.data.json :as json]
    [cemerick.url :refer (url url-encode)]
    [clj-http.client :as http]
    [clojure.string :as string]
    [clojure.pprint]
    [clojure.repl]
    ))

(def myconfig 
  {:google {
            :engine-id ""
            :api-key ""}})



;; Google Custom Search Api
;; https://developers.google.com/custom-search/v1/using_rest
;; https://moz.com/blog/the-ultimate-guide-to-the-google-search-parameters


;; UI Github search for gorilla-repl file format:
;; https://github.com/search?p=5&q=%22gorilla-repl.fileformat+%3D+1%22+extension%3Aclj&type=Code


;; ITEM data:
;; items[].link 	string 	The full URL to which the search result is pointing, e.g. http://www.example.com/foo/bar.
;; items[].displayLink 	string 	An abridged version of this search result’s URL, e.g. www.example.com.
;; items[].snippet 	string 	The snippet of the search result, in plain text.
;; items[].htmlSnippet

(defn print-items [page]
  (let [items (:items page)]
    (clojure.pprint/print-table [:link ] items) ;:snippet
  ))


(defn process-search-result
  "processes the body of a google-search response
   extracts nextStartIndex and the found items"
  [body]
  (let [next-page (get-in body [:queries :nextPage])
        ;_ (println "body-keys: " (keys body))
        ;_ (println "next-page: " next-page)
        items (get-in body [:items]) ]
      ;(clojure.pprint/print-table [:link ] items)
      {:items items
       :nextStartIndex  (:startIndex (first next-page)) }))

(defn search-google-page
  "Issue a query to google
   returns a map {:items :next-page}"
  [search-key start-index]
  (println "requesting page " start-index)
  (let [query-params {:num 10
                      :cx (get-in myconfig [:google :engine-id])
                      :key (get-in myconfig [:google :api-key])
                      :q search-key}
        query-params (if (> start-index 0)
                         (assoc query-params :start start-index)
                         query-params)
       ;_ (println "query params:" query-params)
                      ]
    (-> (http/get "https://www.googleapis.com/customsearch/v1"
          {:accept :json
           :query-params query-params})
        (:body)
        (cheshire.core/parse-string true)
        (process-search-result))))

(defn search
  "returns the items of a google-search.
   Will recurse through multiple pages if need be."
  ([search-key]
    (search search-key 0))
  ([search-key start]
    (let [result (search-google-page search-key start)
          nextStartIndex (:nextStartIndex result)]
      (if (nil? nextStartIndex)
          (:items result)
          (concat (:items result) (search search-key nextStartIndex))))))


;; PARSE GISTS

(defn git-gist-user [link]
  ;; https://gist.github.com/floybix
  (let [r (re-find #"gist.github.com\/([\w\-]+)$" link)]
     (when-not (nil? r) {:user (get r 1) :link link})))

(defn git-gist-user-query [link]
  ;; https://gist.github.com/Engelberg?direction=asc&sort=updated 
  ;; https://gist.github.com/simon-brooke?direction=asc&sort=created    ;; user can have -
  (let [r (re-find #"gist.github.com\/([\w\-]+)\?" link)]
    (when-not (nil? r) {:user (get r 1) :link link})))



(defn git-gist-starred [link]
  ;"https://gist.github.com/syou6162/starred"
  (let [r (re-find #"gist.github.com\/(\w+)\/starred$" link)]
    (when-not (nil? r) {:user nil :gist nil :link link})))


;; GIST DIRECT WITHOUT USER.
; 
(defn git-gist-id-only [link]
  ;"https://gist.github.com/7581bad771c3eef11df2"
  (let [r (re-find #"gist.github.com\/([a-f0-9]{20})$" link)]
    (when-not (nil? r) {:user nil :gist (get r 1) :link link})))

;(git-gist-id-only "https://gist.github.com/7581bad771c3eef11df2")




(defn git-gist-item [link]
  ;"https://gist.github.com/lspector/e0afea6bba84c1317a765b4da55ae0c6"
  (let [r (re-find #"gist.github.com\/(\w+)\/(\w+)" link)]
    (when-not (nil? r) {:user (get r 1) :gist (get r 2) :link link})))

(defn make-link [link]
  {:link link})
;
;; http://viewer.gorilla-repl.org/view.html?source=gist&id=e0afea6bba84c1317a765b4da55ae0c6


(defn process-link [link]
  (->> (map #(% link) [git-gist-id-only  ; must appear before git-gist-user
                       git-gist-user
                       git-gist-user-query
                       git-gist-starred  ; must appear before git-gist-item
                       git-gist-item
                       make-link])
       (remove nil?)
       (first)))
  
(defn process-items [items]
  (let [links (map :link items)]
    (map process-link links)))

(defn gist-2-gists [item]
  (-> item
      (assoc :gists (if (nil? (:gist item)) [] [(:gist item)]))
      (dissoc :gist :link)))

(defn discover-google []
  (->> (search "\"gorilla-repl.fileformat = 1\"")
       (process-items)
       (map gist-2-gists)
       ))
      
(comment

  (search-google-page "\"gorilla-repl.fileformat = 1\"" 0)
  
  ; Test Google Search.
  ; Valid parameter in the end is 0 11 21 ..
  (->> (search-google-page "\"gorilla-repl.fileformat = 1\"" 21)
       (:items)
       (clojure.pprint/print-table [:link]))


  (git-gist-user "https://nothing.com")
  (git-gist-user "https://gist.github.com/floybix")
  (git-gist-user-query "https://gist.github.com/Engelberg?direction=asc&sort=updated")

  (git-gist-item ")https://nothing.com")
  (git-gist-item "https://gist.github.com/lspector/e0afea6bba84c1317a765b4da55ae0c6")
  (process-link "https://gist.github.com/lspector/e0afea6bba84c1317a765b4da55ae0c6")


  (def t (search "\"gorilla-repl.fileformat = 1\""))
  (clojure.pprint/print-table [:link] t)
  (clojure.pprint/print-table [:user :gist :link] (process-items t))
  (process-items t)

  (consolidate-users [{:user "a" :gist "1"} {:user "b"}  {:user "a" :gist "2"}])
  (clojure.pprint/print-table [:user :gists] (vals (consolidate-users (process-items t))))

  (discover-google)
  
  )