(ns pinkgorilla.document.handler
  (:require
   [clojure.tools.logging :refer [debug info]]
   [ring.util.response :as res]
   [pinkgorilla.storage.filename-encoding :refer [decode-storage-using-filename]]
   [pinkgorilla.storage.protocols :refer [query-params-to-storage storage-save storage-load]]))

(defn notebook-save-handler
  [req]
  (let [params (:params req)
        {:keys [tokens storagetype notebook]} params
        stype (keyword storagetype)
        storage-params (dissoc params :notebook :storagetype :tokens) ; notebook-content is too big for logging.
        ;_ (info "Saving type: " stype " params: " storage-params)
        storage (query-params-to-storage stype storage-params)
        message (str "params: " (dissoc params :notebook :tokens)
                     " tokens: " (keys tokens)) ; make sure we dont log secrets 
        ;_ (info "Notebook: " notebook)
        ]
    (if (nil? storage)
      (res/bad-request {:error (str "Cannot save to storage - storage is nil! "
                                    stype " " message)})
      (do
        (info "Saving: " message " storage: " storage)
        (res/response (storage-save storage notebook tokens))))))

(defn notebook-load-handler
  [req]
  (let [params (:params req)
        _ (debug "document load params " (pr-str params))
        {:keys [tokens storagetype]} params
        stype (if (keyword? storagetype) storagetype (keyword storagetype))
        storage-params (dissoc params :storagetype :tokens) ; notebook-content is too big for logging.
        ;_ (info "Saving type: " stype " params: " storage-params)
        storage (query-params-to-storage stype storage-params)]
    (if (nil? storage)
      (res/bad-request {:error "storage is nil"})
      (do
        (info "Loading from storage: " storage)
        (if-let [content (storage-load storage tokens)]
          (let [notebook (decode-storage-using-filename storage content)]
            (if notebook
              (do
                (info "notebook successfully loaded! ")
                (res/response notebook))
              (res/bad-request {:error "decoding failed."})))
          (res/bad-request {:error "content is empty"}))))))


