(ns demo.app
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.config :refer [load-config! get-in-config]]
   [webly.user.app.app :refer [webly-run!]]
   [pinkgorilla.explore.handler :refer [explore-directories-start]]
   ; side-effects 
   [pinkgorilla.explorer.handler]
   [pinkgorilla.explorer.default-config]
   [demo.routes]))

(defn -main
  [mode]
  (load-config!)
  (let [mode (or mode "watch")
        config-explorer-server (get-in-config [:explorer :server])]
    (when (or (= mode "watch")
              (= mode "run"))
      (explore-directories-start config-explorer-server))
    (webly-run! mode)))