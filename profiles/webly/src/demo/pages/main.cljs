(ns demo.pages.main
  (:require
   [taoensso.timbre :as timbre :refer [info]]
    [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [webly.web.handler :refer [reagent-page]]
    [webly.user.oauth2.view :refer [tokens-view user-button]]
))


(defn main []
  [:div
   [:h1 "demo - explorer !!"]
   [:a.bg-green-300 {:href "/explorer"} "explorer"]
   [:a.bg-red-300 {:href "/demo/save"} "save-as dialog demo"]
   
   [:p "login to github to see your gists / repos"]
   [user-button :github]
   [:p {:on-click #(dispatch [:explorer/fetch-my-github])} "get my github"]
   ])

(defmethod reagent-page :demo/main [{:keys [route-params query-params handler]}]
  [main])