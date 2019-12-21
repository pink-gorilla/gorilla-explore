(ns pinkgorilla.explore.file
  "Utility functions to help with scanning for and loading gorilla files."
  (:import java.util.Date)
  (:require
   [clojure.string :as str]
     ; dependencies needed to be in cljs bundle: 
   [pinkgorilla.storage.storage :as storage]
   [pinkgorilla.storage.file]
   [pinkgorilla.explore.meta :refer [add-meta]]))

(defn ends-with
  [string ending]
  (if (< (count string) (count ending))
    false
    (let [l (count string)
          le (count ending)]
      (= (subs string (- l le) l) ending))))

(defn clj-file?
  [file]
  (ends-with (.getName file) ".clj"))

(defn cljg-file?
  [file]
  (ends-with (.getName file) ".cljg"))

(defn gorilla-file?
  [file]
  (when (.isFile file)
    (with-open [r (java.io.BufferedReader. (java.io.FileReader. file))]
      (let [first-line (.readLine r)]
        (if (> (count first-line) 26)
          (let [header (subs first-line 0 26)]
            (= header ";; gorilla-repl.fileformat")))))))

(defn excluded-file-seq
  [excludes file]
  (tree-seq
   (fn [f] (and (.isDirectory f) (not (contains? excludes (.getName f)))))
   (fn [f] (.listFiles f))
   file))

(defn include-file?
  "Should a file be included in the 'load file' list? Currently all .cljg files, and .clj files with a Gorilla header
  are included."
  [file]
  (or (cljg-file? file) (and (clj-file? file) (gorilla-file? file))))


(def excludes  #{".git"})


(defn file-info [file]
  {:filename (. file getPath)
   :edit-date (Date. (.lastModified file))})

(defn date->str [date]
  (-> (java.text.SimpleDateFormat. "yyyy-MM-dd")              ;thread-first macro
      (.format (.getTime date))
      str))

(defn gorilla-files-in-directory
  "get all pink-gorilla filenames in a directory.
   Works recursively, so sub-directories are included."
  [directory]
  (->> (clojure.java.io/file directory)
       (excluded-file-seq excludes)
       (filter include-file?)
       (map file-info)
      ; (map #(str/replace-first (. % getPath) "./" ""))
       ))

(defn explore [directory]
  (let [tokens {} ; file-storage does not need tokens
        storage {:type :file
                 :user "_file"}]
    (->> (gorilla-files-in-directory directory)
         (map #(assoc storage
                      :filename (:filename %)
                      :id (:filename %)
                      :edit-date (date->str (:edit-date %))
                      ;:x (:edit-date %)
                      ))
         (map (partial add-meta tokens)))))


(comment


  (gorilla-files-in-directory "/home/andreas/Documents/gorilla/sample-notebooks/samples")

  (explore "/home/andreas/Documents/gorilla/sample-notebooks/samples"))