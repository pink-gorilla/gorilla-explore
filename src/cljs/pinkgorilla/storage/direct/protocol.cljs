(ns pinkgorilla.storage.direct.protocol)

(defprotocol Direct
  (load-url [self base-path])
  (decode-content [self content])) ; happens after worksheet-content has been loaded via ajax/url fetch
  
