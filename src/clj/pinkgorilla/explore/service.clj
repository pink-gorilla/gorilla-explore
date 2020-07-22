(ns pinkgorilla.explore.service
  (:require
   [taoensso.timbre :refer [debug info error]]
   [clojure.java.io]
   [clojure.core.async :refer [thread]]
   [hawk.core :as hawk]
   [pinkgorilla.explore.file :refer [explore-directory notebook-file? file-info]]))

(defn- explore-dir [excludes [name dir]]
  (info "exploring notebooks for repo " name " in " dir)
  (->> (explore-directory excludes dir)
       (map #(assoc % :repo name :root-dir dir))
       (vec)))

(defn explore-directories [excludes roots]
  (reduce concat [] (map (partial explore-dir excludes) roots)))

(def data (atom {}))

(defn- delete-notebook [file]
  (let [filename (.getPath file)]
    (->> (remove #(= filename (:filename-canonical %)) (:notebooks @data))
         (swap! data assoc :notebooks))))

(defn add-notebook [file]
  (->> (conj (:notebooks @data) (file-info file))
       (swap! data assoc :notebooks)))

(defn process-file-change [ctx {:keys [kind file] :as e}]
  (info "watcher-action: event: " e)
  (info "watcher-action: context: " ctx)
  (when (notebook-file? file)
    (case kind
      :create (add-notebook file)
      :delete (delete-notebook file)
      :modify (do (delete-notebook file)
                  (add-notebook file))))
  ctx)

(defn to-canonical [path]
  (->>
   (clojure.java.io/file path)
   (.getCanonicalFile)
   (.getPath))
  path)

(defn exists? [path]
  (->> path clojure.java.io/file .exists))

(defn existing-roots [roots]
  (into {} (filter (fn [[name-kw path]]
            ;(println name-kw path)
                     (when (exists? path)
                       [name-kw path])) roots)))

(defn start [excludes roots]
  (let [roots (existing-roots roots)
        watch-paths (into [] (map to-canonical (vals roots)))
        c {:excludes excludes
           :roots roots
           :notebooks []
           :watching watch-paths}]
    (reset! data c)
    (thread
      (->> (explore-directories excludes roots)
           (swap! data assoc :notebooks))
      (info "initial exploration finished: " (count (:notebooks @data)) " notebooks discovered."))
    (info "starting watcher: "  watch-paths)
    (hawk/watch! {:watcher :polling}
                 [{:paths watch-paths
                   :handler process-file-change}]))
  nil)

(defn notebooks []
  (:notebooks @data))

(comment

  @data

  ;; sync mode
  (explore-directories #{".git"} {:gorilla-ui "../gorilla-ui/notebooks"
                                  :gorilla-plot "../gorilla-plot/notebooks"
                                  :backtest "../../quant/backtest/notebooks"
                                  :trateg "../../quant/trateg/notebooks"})

  ;; service mode
  (start #{".git"} {:gorilla-ui "../gorilla-ui/notebooks"
                    :gorilla-plot "../gorilla-plot/notebooks"
                    :backtest "../../quant/backtest/notebooks"
                    :trateg "../../quant/trateg/notebooks"})

  (notebooks)
  (first (notebooks))
  (count (notebooks))

  (->> (clojure.java.io/file  "../gorilla-ui/notebooks")
       ;(.isDirectory)
       (.getCanonicalFile)
       (.getPath))

; comment end
  )

