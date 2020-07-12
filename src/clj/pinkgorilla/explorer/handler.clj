(ns pinkgorilla.explorer.handler
  (:require
   [webly.web.middleware :refer [wrap-api-handler]]
   [webly.web.handler :refer [add-ring-handler]]
   [pinkgorilla.document.handler :refer [notebook-load-handler notebook-save-handler]]
   [pinkgorilla.explore.handler :refer [handler-explore-async]]))

(add-ring-handler :api/explorer (wrap-api-handler handler-explore-async))
(add-ring-handler :api/notebook-load (wrap-api-handler notebook-load-handler))
(add-ring-handler :api/notebook-save (wrap-api-handler notebook-save-handler))
