(ns pinkgorilla.explore.default-config
  (:require

   [pinkgorilla.document.default-config]

   ; todo: move the entire default-config to storage

  ; storage.direct works only in cljs


   #?(:cljs [pinkgorilla.storage.direct.file])
   #?(:cljs [pinkgorilla.storage.direct.gist])
   #?(:cljs [pinkgorilla.storage.direct.repo])
   #?(:cljs [pinkgorilla.storage.direct.bitbucket])

   #?(:cljs [pinkgorilla.document.component])))