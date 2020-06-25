(ns pinkgorilla.explore.default-config
  (:require
   [bidi.bidi :as bidi] ; for tests (see bottom)

   ; from encoding:
   [pinkgorilla.document.default-config]

   ; todo: move direct storage to encoding
  ; storage.direct works only in cljs
   #?(:cljs [pinkgorilla.storage.direct.file])
   #?(:cljs [pinkgorilla.storage.direct.gist])
   #?(:cljs [pinkgorilla.storage.direct.repo])
   #?(:cljs [pinkgorilla.storage.direct.bitbucket])

   ; this project
   #?(:cljs [pinkgorilla.document.component])
   #?(:cljs [pinkgorilla.save-dialog.component])
   #?(:cljs [pinkgorilla.bidi])))

; ROUTES:
; route definitions can be composed with bidi. 
; Therefore it does make sense that default route config is
; exported here. 

(def explorer-routes-ui
  {"explorer"  :ui/explorer
   "notebook"  :ui/notebook
   "demo/save" :demo/save})

(def explorer-routes-frontend
  ["/" explorer-routes-ui])

(def explorer-routes-api
  ["" {"/" explorer-routes-ui
       "/api/" {"explorer"  {:get  :api/explorer}
                "notebook"  {:get  :api/notebook-load
                             :post :api/notebook-save}}}])

    ;"section-a"            {"" :section-a
    ;                        ["/item-" :item-id] :a-item}
    ;"section-b"            :section-b
    ;true                   :four-o-four
    ;["" :id]               :bongo  


(comment

  (bidi/path-for explorer-routes-frontend  :ui/explorer)
  (bidi/path-for explorer-routes-frontend  :ui/notebook)
  (bidi/path-for explorer-routes-frontend  :ui/unknown)

  (bidi/path-for explorer-routes-api  :api/explorer)
  (bidi/path-for explorer-routes-api  :api/notebook-load)
  (bidi/path-for explorer-routes-api  :ui/explorer)

  ;
  )