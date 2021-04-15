(ns demo.pages.main
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.web.handler :refer [reagent-page]]
))


(defn main []
  [:div
   [:h1 "demo - explorer !!"]
   [:a.bg-green-300 {:href "/explorer"} "explorer"]
   [:a.bg-red-300 {:href "/demo/save"} "save-as dialog demo"]])

(defmethod reagent-page :demo/main [{:keys [route-params query-params handler]}]
  [main])