(ns demo.app
  (:require
   ;[clojure.tools.logging :refer [info]]
   [taoensso.timbre :as timbre :refer [info]]
   [shadow.cljs.devtools.api :as shadow]
    ;; [shadow.cljs.devtools.server.nrepl :as shadow-nrepl]
   [shadow.cljs.devtools.server :as shadow-server]
   [pinkgorilla.explore.default-config] ; side effects
   [pinkgorilla.explore.handler :refer [explore-directories-start]]
   [demo.routes]
   [demo.config :refer [config-server]]))

;(timbre/set-level! :trace) ; Uncomment for more logging
;  (timbre/set-level! :debug)
(timbre/set-level! :info)

(defn -main
  {:shadow/requires-server true}
  [& args]
  (info "Starting with args: " args)

  (explore-directories-start config-server)
  (shadow-server/start!)
  (shadow/watch :demo {:verbose true}))


(comment
  (-main))