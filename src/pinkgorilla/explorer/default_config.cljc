(ns pinkgorilla.explorer.default-config
  (:require
   [pinkgorilla.encoding.default-config]  ; from encoding

   ; todo: move direct storage to encoding
  ; storage.direct works only in cljs
   #?(:cljs [pinkgorilla.storage.direct.file])
   #?(:cljs [pinkgorilla.storage.direct.gist])
   #?(:cljs [pinkgorilla.storage.direct.repo])
   #?(:cljs [pinkgorilla.storage.direct.bitbucket])
   #?(:cljs [pinkgorilla.storage.unsaved])

   ; document
   #?(:cljs [pinkgorilla.document.component])
   #?(:cljs [pinkgorilla.document.save-dialog.component])

   ; explorer (explore + document)
   #?(:cljs [pinkgorilla.explorer.events])
   #?(:cljs [pinkgorilla.document.save-dialog.events])))

; ROUTES:
; route definitions can be composed with bidi. 
; Therefore it does make sense that default route config is
; exported here. 

(def routes-app
  {"explorer"     :ui/explorer
   "notebook"     :ui/notebook})

(def routes-api
  {"explorer"  {:get  :api/explorer}
   "my-github" {:get  :api/my-github}
   "notebook"  {:get  :api/notebook-load
                :post :api/notebook-save}})
