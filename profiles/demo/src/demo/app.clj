(ns demo.app
  (:require
   [clojure.tools.logging :refer [info]]
   [shadow.cljs.devtools.api :as shadow]
    ;; [shadow.cljs.devtools.server.nrepl :as shadow-nrepl]
   [shadow.cljs.devtools.server :as shadow-server]
   ;demo
   [pinkgorilla.explore.default-config]
   [demo.routes]
   [demo.explore-handler :refer [explore-directories-start]]
   ))

(defn -main
  {:shadow/requires-server true}
  [& args]
  (info "Starting with args: " args)
 
  (explore-directories-start)
  (shadow-server/start!)
  (shadow/watch :demo {:verbose true}))


(comment
  (-main)
  
  )