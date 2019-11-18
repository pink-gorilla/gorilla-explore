(ns pinkgorilla.explore.print)


(defn shorten-field-one- 
  "caps description (so table view is still nice)"
  [field max-length gist]
  (let [d (field gist)
        d (if (nil? d)
            nil
            (subs d 0 (min (count d) max-length)))]
    (assoc gist field d)))



(defn shorten-field- [field max-length items]
  (map (partial shorten-field-one- field max-length) items))

(defn print-gist [gists]
  (let [gists-short (shorten-field- :description 20 gists)]
    (clojure.pprint/print-table [:user :id :description :gist-fn] gists-short)))
  ; :files

(defn print-repo [repos]
  (let [repos-short (shorten-field- :repo-path 40 repos)
        repos-short (shorten-field- :repo 25 repos-short)
        ]
  (clojure.pprint/print-table [:user :repo :repo-path ] repos-short)))
; :repo-fn
; :id

(comment 
  ;; Print GISTS/REPOS from db 
  (use '[universe.db :refer [gists repos]])
  (print-gist (gists))
  (print-repo (repos))

  )

