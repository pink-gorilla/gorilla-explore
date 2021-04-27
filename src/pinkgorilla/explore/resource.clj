(ns pinkgorilla.explore.resource
  (:require
   [taoensso.timbre :as timbre :refer [info warn error]]
   [resauce-hack.core :as resauce]
   [pinkgorilla.document.default-config] ; side-efffects
   [pinkgorilla.storage.filename-encoding :refer [split-filename]]
   [pinkgorilla.document.meta.meta :refer [add-meta]]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.explore.excluded :refer [excluded?]]))

(defn- ends-with
  [string ending]
  (if (< (count string) (count ending))
    false
    (let [l (count string)
          le (count ending)]
      (= (subs string (- l le) l) ending))))

(defn- cljg-file?
  [f]
  ;(info "cljg-file? " file)
  (try
    (ends-with f ".cljg")
    (catch Exception e
      (do (error "cljg-file " f " exception: " (.getMessage e))
          false))))

(defn join-path [base f]
  (str base "/"  f))

(defn file-infos [filename]
  (let [name-no-notebook-dash (subs filename 10 (count filename))
        entry {:type :res
               :filename name-no-notebook-dash
               :user "_resource"
               :id name-no-notebook-dash
               :edit-date nil
               :encoding :gorilla}
        tokens {}]
    (add-meta tokens entry)
    ;entry
    ))

(defn explore-resources [resource-root-path]
  (->>  (resauce/resource-dir-names-tree resource-root-path)
        (filter cljg-file?)
        (distinct) ; for a strange reason, there are MANY duplicates in notebook-ui
        (sort)
        (map file-infos)
        (remove excluded?)))

(comment

  ; load res
  ;(load-res "public/webly/dialog.css")
  ;(load-res "notebooks/explorer/specs.cljg")

  ; public fucks up - possibly too big?
  ;(resauce/resource-dir-names-tree "public")
  (resauce/resource-dir-names-tree "notebooks")
  (explore-resources "notebooks")

  (create-storage {:type :res
                   :filename "explorer/specs.cljg"})

  (split-filename "explorer/specs.cljg")
  (add-meta {}  {:type :res
                 :filename "explorer/specs.cljg"})
;
  )


