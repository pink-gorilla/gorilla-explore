(ns pinkgorilla.explore.file
  "Utility functions to help with scanning for and loading gorilla files."
  (:import java.util.Date)
  (:require
   [clojure.string :as str]
   [clojure.java.io]
   ;[pinkgorilla.storage.protocols :as storage]
   ;[pinkgorilla.storage.file]
   [pinkgorilla.explore.meta :refer [add-meta]]))

;; File is Notebook Format ??

(defn- ends-with
  [string ending]
  (if (< (count string) (count ending))
    false
    (let [l (count string)
          le (count ending)]
      (= (subs string (- l le) l) ending))))

(defn- clj-file?
  [file]
  (ends-with (.getName file) ".clj"))

(defn- cljg-file?
  [file]
  (ends-with (.getName file) ".cljg"))

(defn- jupyter-file?
  [file]
  (ends-with (.getName file) ".ipynb"))

(defn- gorilla-file?
  [file]
  (when (.isFile file)
    (with-open [r (java.io.BufferedReader. (java.io.FileReader. file))]
      (let [first-line (.readLine r)]
        (when (> (count first-line) 26)
          (let [header (subs first-line 0 26)]
            (= header ";; gorilla-repl.fileformat")))))))

(defn notebook-file?
  "Should  .cljg files, and .clj files with a Gorilla header
  are included."
  [file]
  (or (cljg-file? file)
      (and (clj-file? file) (gorilla-file? file))
      (jupyter-file? file)))

;; Exclude Files

(defn excluded-file-seq
  "
    excludes  #{\".git\"}
  "
  [excludes file]
  (tree-seq
   (fn [f] (and (.isDirectory f)
                (not (contains? excludes (.getName f)))))
   (fn [f] (.listFiles f))
   file))

(defn date->str [date]
  (-> (java.text.SimpleDateFormat. "yyyy-MM-dd")
      (.format (.getTime date))
      str))

(defn file-info [file]
  (let [tokens {}
        filename (.getPath file)
        filename-canonical (.getPath (.getCanonicalFile file))]
    (->>
     {:type :file
      :user "_file"
      :filename filename
      :filename-canonical filename-canonical
      :id filename
      :edit-date (date->str (Date. (.lastModified file)))}
     (add-meta tokens))))

(defn explore-directory
  "get all pink-gorilla filenames in a directory.
   Works recursively, so sub-directories are included."
  [excludes directory]
  (->> (clojure.java.io/file directory)
       (excluded-file-seq excludes)
       (filter  notebook-file?)
       (map file-info)))


;; Old Api that only needs names


(defn gorilla-filepaths-in-current-directory
  [excludes]
  (map #(str/replace-first (. % getPath) "./" "")
       (filter  notebook-file? (excluded-file-seq
                                excludes
                                (clojure.java.io/file ".")))))

(comment

  (def excludes #{".git"})
  (gorilla-filepaths-in-current-directory excludes)

  (excluded-file-seq
   excludes
   (clojure.java.io/file
    "/home/andreas/Documents/quant/trateg/notebooks"))

  (explore-directory
   excludes
   "/home/andreas/Documents/quant/trateg/notebooks")

  (explore-directory
   excludes
   "/home/andreas/Documents/quant/trateg/notebooks")

  (.getPath (clojure.java.io/file
             "/home/andreas/Documents/quant/trateg/notebooks/alphavantage.cljg"))

  ; comment end
  )