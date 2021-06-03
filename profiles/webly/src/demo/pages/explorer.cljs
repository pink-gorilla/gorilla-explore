(ns demo.pages.explorer
  (:require
   [taoensso.timbre :as timbre :refer [info]]
   [webly.web.handler :refer [reagent-page]]
   [pinkgorilla.document.collection.component :refer [notebook-explorer]]
   [pinkgorilla.explorer.bidi :refer [goto-notebook!]]))

(defn open-notebook [nb]
  (info "load-notebook-click" nb)
  (goto-notebook! (:storage nb)))

(defmethod reagent-page :ui/explorer [{:keys [route-params query-params handler]}]
  [notebook-explorer open-notebook])
