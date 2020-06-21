(ns demo.app
  (:require
   [clojure.tools.logging :refer [info]]
   [shadow.cljs.devtools.api :as shadow]
    ;; [shadow.cljs.devtools.server.nrepl :as shadow-nrepl]
   [shadow.cljs.devtools.server :as shadow-server]))

(defn -main
  {:shadow/requires-server true}
  [& args]
  (info "Starting with args: " args)
  ;; TODO: Should probably use the one started by shadow-cljs - which works for clj and cljs (connecting with Calva)
 
  (shadow-server/start!)
  (shadow/watch :demo {:verbose true})
  ;; (start "dev")
  )