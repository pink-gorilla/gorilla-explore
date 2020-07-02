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
   "notebook"     :ui/notebook
   "notebook/new" :ui/notebook-new
   "demo/save"    :demo/save})

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

(comment

  (bidi/path-for explorer-routes-frontend  :ui/explorer)
  (bidi/path-for explorer-routes-frontend  :ui/notebook)
  (bidi/path-for explorer-routes-frontend  :ui/unknown)

  (bidi/path-for explorer-routes-api  :api/explorer)
  (bidi/path-for explorer-routes-api  :api/notebook-load)
  (bidi/path-for explorer-routes-api  :ui/explorer)

       ;TODO : make a unit test with this
  (def demo-load-request
    {:headers
     {"content-type" "application/edn"
      "accept" "application/transit+json"}
     :body nil
     :params {:token nil
              :storagetype :repo
              :user "pink-gorilla"
              :repo "gorilla-ui"
              :filename "notebooks/videos.cljg"}})

     ;TODO : make a unit test with this
  (notebook-load-handler demo-load-request)
       ;

  ;
  )