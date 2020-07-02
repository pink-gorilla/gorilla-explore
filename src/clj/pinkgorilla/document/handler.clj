(ns pinkgorilla.document.handler
  (:require
   [taoensso.timbre :refer [debug info error]]
   [ring.util.response :as res]
   [pinkgorilla.storage.protocols :refer [query-params-to-storage]]
   [pinkgorilla.notebook.hydration :refer [load-notebook save-notebook]]))

(defn notebook-save-handler
  [req]
  (let [_ (info "save request: " req)
        params (:body-params req) ; was: (:params req)
        {:keys [tokens storagetype storage-params notebook]} params
        ;storage-type (keyword storagetype)
        ;storage-params (dissoc params :notebook :storagetype :tokens) ; notebook-content is too big for logging.
        ;_ (info "Saving type: " stype " params: " storage-params)
        storage (query-params-to-storage storagetype storage-params)
        _ (info "Notebook: " notebook)]
    (if (nil? storage)
      (res/bad-request {:error (str "Cannot save to storage - storage is nil! " storagetype)})
      (do
        (info "Saving: storage: " storage " creds: " (keys tokens)) ; ; make sure we dont log secrets 
        (res/response (save-notebook storage tokens notebook))))))

(defn notebook-load-handler
  [req]
  (let [params (:params req)
        _ (debug "document load params " (pr-str params))
        {:keys [tokens storagetype]} params
        stype (if (keyword? storagetype) storagetype (keyword storagetype))
        storage-params (dissoc params :storagetype :tokens)
        storage (query-params-to-storage stype storage-params)]
    (if (nil? storage)
      (res/bad-request {:error "storage is nil"})
      (do
        (info "Loading from storage: " storage)
        (if-let [notebook (load-notebook storage tokens)]
          (do
            (info "notebook successfully loaded! ")
            (res/response notebook))
          (res/bad-request {:error "notebook loading failed."}))))))


