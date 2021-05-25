(ns pinkgorilla.explorer.bidi
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.storage.protocols :refer [storage->map]]))

(defn goto-notebook! [storage]
  (let [query-params (if (:id storage)
                       {:id (name (:id storage))}
                       (storage->map storage))]
    (info "goto-notebook query params: " query-params)
    (dispatch [:bidi/goto :ui/notebook :query-params query-params])))




