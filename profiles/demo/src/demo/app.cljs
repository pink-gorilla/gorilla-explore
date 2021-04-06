(ns demo.app
  (:require
   [reagent.dom]
   [taoensso.timbre :as timbre :refer [info]]
   [webly.user.app.app :refer [webly-run!]]
   [demo.routes :refer [routes-api routes-app]]
   ; side-effects
   [demo.views]
   [demo.events]))

(defn ^:export start []
  (webly-run! routes-api routes-app))



