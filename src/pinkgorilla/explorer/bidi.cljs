(ns pinkgorilla.explorer.bidi
  (:require
   [taoensso.timbre :refer-macros [debug info error]]
   [re-frame.core :refer [dispatch]]
   [pinkgorilla.storage.protocols :refer [storage->map]]))

(defn goto-notebook! [storage-or-id]
  (let [query-params (if (:id storage-or-id)
                       {:id (name (:id storage-or-id))}
                       (storage->map storage-or-id))]
    (info "goto-notebook query params: " query-params)
    (dispatch [:bidi/goto :ui/notebook :query-params query-params])))




