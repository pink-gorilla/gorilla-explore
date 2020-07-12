(ns pinkgorilla.explorer.default-config
  (:require
   #?(:cljs [webly.config]) ; from webly
   [pinkgorilla.document.default-config]  ; from encoding

   ; todo: move direct storage to encoding
  ; storage.direct works only in cljs
   #?(:cljs [pinkgorilla.storage.direct.file])
   #?(:cljs [pinkgorilla.storage.direct.gist])
   #?(:cljs [pinkgorilla.storage.direct.repo])
   #?(:cljs [pinkgorilla.storage.direct.bitbucket])
   #?(:cljs [pinkgorilla.storage.unsaved])

   ; document
   #?(:cljs [pinkgorilla.document.component])
   #?(:cljs [pinkgorilla.save-dialog.component])

   ; explorer (explore + document)
   #?(:cljs [pinkgorilla.explorer.events])
   #?(:cljs [pinkgorilla.save-dialog.events])))

; ROUTES:
; route definitions can be composed with bidi. 
; Therefore it does make sense that default route config is
; exported here. 

(def explorer-routes-app
  {"explorer"     :ui/explorer
   "notebook"     :ui/notebook})

(def explorer-routes-api
  {"explorer"  {:get  :api/explorer}
   "notebook"  {:get  :api/notebook-load
                :post :api/notebook-save}})

(def config-server
  {:exclude       #{".git" ".svn"}
   :roots {:gorilla-notebook "../gorilla-notebook/notebooks"}})

(def config-client
  {:repositories
   [{:name "local" :save true :url "/api/explorer"}
    {:name "public" :url "https://raw.githubusercontent.com/pink-gorilla/gorilla-explore/master/resources/list.json"}]})


