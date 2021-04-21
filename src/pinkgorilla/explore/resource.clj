(ns pinkgorilla.explore.resource
  (:require
   [clojure.string :as str :refer [split]]
   [taoensso.timbre :as timbre :refer [info warn]]
   [clojure.java.io :as io]
   [resauce.core :as resauce]
   [jdk.net.URL :as url]
   [jdk.io.File :as file]
   [pinkgorilla.document.meta.meta :refer [add-meta]]))


; browse one folder


(defn res-folder-files [path]
  (->> path
       resauce/resource-dir
       (map io/as-file)))

; browse recursive

(defn- ends-with
  [string ending]
  (if (< (count string) (count ending))
    false
    (let [l (count string)
          le (count ending)]
      (= (subs string (- l le) l) ending))))

(defn- cljg-file?
  [file]
  (ends-with (file/get-name file) ".cljg"))

(defn dir? [f]
  (file/directory? f))

(defn join-path [base f]
  (str base "/" (file/get-name f)))

(defn res-folder-files-recursive
  ([]
   (res-folder-files-recursive "notebooks"))
  ([res-dir]
   (res-folder-files-recursive [] res-dir))
  ([acc res-dir]
   (let [files (res-folder-files res-dir)]
     (info "exploring res folder: " res-dir)
     (reduce (fn [items v]
               ;(info "v:" (file/get-name v))
               (if (dir? v)
                 (concat items (res-folder-files-recursive (join-path res-dir v)))
                 (if (cljg-file? v)
                   (conj items (join-path res-dir v))
                   items)))
             acc
             files))))

(defn file-infos [filename]
  (let [name-no-notebook-dash (subs filename 10 (count filename))
        entry {:type :res
               :filename name-no-notebook-dash
               :user "_resource"
               :id name-no-notebook-dash
               :edit-date nil
               :encoding :gorilla}
        tokens {}]
    (add-meta tokens entry)))

(defn explore-resources []
  (->>  (res-folder-files-recursive "notebooks")
        (map file-infos)))

(comment

  ; load res
  ;(load-res "public/webly/dialog.css")
  ;(load-res "notebooks/explorer/specs.cljg")

  (res-folder-files-recursive "public/webly")
  (res-folder-files-recursive "notebooks")

  (explore-resources)

  (defn log [f]
    (info "file: " (file/get-name f) "dir: " (file/directory? f) "file: " (file/file? f)))

  (->>  (res-folder-files "public/webly")
   ;(res-folder-files "notebooks/")
   ; first
   ; url/get-pathjar
   ;(map resauce/directory?)
        (map dir-file?)
  ;(map io/as-file)
  ;(map file/file?)
 ;(map file/get-path)
      ;(map log)
        )

  (defn res-io [name]
    (-> io/resource
        io/as-file))

;
  )


