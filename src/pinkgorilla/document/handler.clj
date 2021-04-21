(ns pinkgorilla.document.handler
  (:require
   [taoensso.timbre :refer [debug info error]]
   [ring.util.response :as res]
   [pinkgorilla.storage.protocols :refer [create-storage]]
   [pinkgorilla.notebook.persistence :refer [load-notebook save-notebook]]
   [pinkgorilla.explore.github-helper :refer [options-oauth2]]))

(defn params->storage [params]
  (let [type (:type params)
        type (if (string? type) (keyword type) type) ; json supports no keywords
        storage-params (->  params
                            (dissoc :notebook :tokens) ; notebook-content is too big for logging.
                            (assoc :type type))]
    (create-storage storage-params)))

(defn notebook-save-handler
  [req]
  (let [_ (info "save request: " req)
        params (:body-params req) ; was: (:params req)
        {:keys [token-github storage notebook]} params
        tokens (options-oauth2 token-github)
        pstorage storage
        storage (params->storage pstorage)]
    (if (nil? storage)
      (res/bad-request {:error (str "Cannot save to storage - storage is nil! " pstorage)})
      (do
        (info "Saving: storage: " storage " creds: " (keys tokens)) ; keys only: dont log secrets 
        (error "tokens: " tokens)
        (let [save-result (save-notebook storage tokens notebook)]
          (res/response save-result))))))

(defn notebook-load-handler
  [req]
  (let [params (:params req)
        _ (debug "document load params " (pr-str params))
        {:keys [token-github]} params
        tokens (options-oauth2 token-github)
        storage (params->storage params)]
    (if (nil? storage)
      (res/bad-request {:error "storage is nil"})
      (do
        (info "Loading from storage: " storage)
        (if-let [notebook (load-notebook storage tokens)]
          (do
            (info "notebook successfully loaded! size: " (count notebook))
            (res/response notebook))
          (res/bad-request {:error "notebook loading failed."}))))))


