(ns pinkgorilla.explore.file
  "Utility functions to help with scanning for and loading gorilla files."
  (:import java.util.Date)
  (:require
   [clojure.string :as str]
   [clojure.java.io]
     ; dependencies needed to be in bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
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

(defn- notebook-file?
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

(defn- file-info [file]
  {:filename (. file getPath)
   :edit-date (Date. (.lastModified file))})

(defn gorilla-files-in-directory
  "get all pink-gorilla filenames in a directory.
   Works recursively, so sub-directories are included."
  [excludes directory]
  (->> (clojure.java.io/file directory)
       (excluded-file-seq excludes)
       (filter  notebook-file?)
       (map file-info)))

(defn date->str [date]
  (-> (java.text.SimpleDateFormat. "yyyy-MM-dd")
      (.format (.getTime date))
      str))

(defn explore [excludes directory]
  (let [tokens {} ; file-storage does not need tokens
        storage {:type :file
                 :user "_file"}]
    (->> (gorilla-files-in-directory excludes directory)
         (map #(assoc storage
                      :filename (:filename %)
                      :id (:filename %)
                      :edit-date (date->str (:edit-date %))
                      ;:x (:edit-date %)
                      ))
         (map (partial add-meta tokens)))))

;; Old Api that only needs names

(defn gorilla-filepaths-in-current-directory
  [excludes]
  (map #(str/replace-first (. % getPath) "./" "")
       (filter  notebook-file? (excluded-file-seq
                                (clojure.java.io/file ".")
                                excludes))))

(comment

  (def excludes #{".git"})

  (excluded-file-seq
   excludes
   (clojure.java.io/file
    "/home/andreas/Documents/quant/trateg/notebooks"))

  (gorilla-filepaths-in-current-directory excludes)

  (gorilla-files-in-directory
   excludes
   "/home/andreas/Documents/quant/trateg/notebooks")

  (explore
   excludes
   "/home/andreas/Documents/quant/trateg/notebooks")

  ; comment end
  )