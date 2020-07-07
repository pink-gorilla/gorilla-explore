(ns pinkgorilla.explorer.default-config
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
   #?(:cljs [pinkgorilla.storage.unsaved])

   #?(:cljs [pinkgorilla.bidi.events])

   ; document
   #?(:cljs [pinkgorilla.document.component])
   #?(:clj [pinkgorilla.document.handler])
   #?(:cljs [pinkgorilla.save-dialog.component])

  ; explore   
   #?(:clj [pinkgorilla.explore.handler :refer [handler-explore-async]])
   #?(:clj [pinkgorilla.explore.middleware :refer [wrap-api-handler]])

   ; explorer (explore + document)
   #?(:cljs [pinkgorilla.explorer.events])
   #?(:cljs [pinkgorilla.save-dialog.events])))

; ROUTES:
; route definitions can be composed with bidi. 
; Therefore it does make sense that default route config is
; exported here. 

(def explorer-routes-ui
  {"explorer"     :ui/explorer
   "notebook"     :ui/notebook})

(def explorer-routes-api
  {"/api/" {"explorer"  {:get  :api/explorer}
            "notebook"  {:get  :api/notebook-load
                         :post :api/notebook-save}}})

    ;"section-a"            {"" :section-a
    ;                        ["/item-" :item-id] :a-item}
    ;"section-b"            :section-b
    ;true                   :four-o-four
    ;["" :id]               :bongo  


(def config-server
  {:exclude       #{".git" ".svn"}
   :roots {:gorilla-notebook "../gorilla-notebook/notebooks"}})

(def config-client
  {:repositories
   [{:name "local" :save true :url "/api/explorer"}
    {:name "public" :url "https://raw.githubusercontent.com/pink-gorilla/gorilla-explore/master/resources/list.json"}]})

#?(:clj
   (do
     (def explore-handler
       (wrap-api-handler handler-explore-async))

     (def notebook-load-handler
       (wrap-api-handler pinkgorilla.document.handler/notebook-load-handler))

     (def notebook-save-handler
       (wrap-api-handler pinkgorilla.document.handler/notebook-save-handler))

    ;
     ))
