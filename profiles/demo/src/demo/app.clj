(ns demo.app
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.config :refer [load-config! get-in-config]]
   [webly.profile :refer [server?]]
   [webly.user.app.app :refer [webly-run!]]
   [pinkgorilla.explore.handler :refer [explore-directories-start]]
   ; side-effects 
   [pinkgorilla.explorer.handler]
   [pinkgorilla.explorer.default-config]
   [demo.routes]))

(defn -main
  [profile-name]
  (load-config!)
  (let [config-explorer-server (get-in-config [:explorer :server])]
    (when (server? profile-name)
      (explore-directories-start config-explorer-server))
    (webly-run! profile-name)))