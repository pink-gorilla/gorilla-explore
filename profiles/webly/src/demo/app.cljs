(ns demo.app
  (:require
   ;[reagent.dom]
   [webly.user.app.app :refer [webly-run!]]
   [demo.routes :refer [routes-api routes-app]]
   ; side-effects
   [demo.events]
   [demo.pages.main]
   [demo.pages.explorer]
   [demo.pages.save-dialog]
   [demo.pages.document]))

(defn ^:export start []
  (webly-run! routes-api routes-app))



