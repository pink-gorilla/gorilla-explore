(ns pinkgorilla.explore.default-config
  (:require

   ; todo: move the entire default-config to storage

   [pinkgorilla.encoding.jupyter] ; add jupyter encoding to bundle  

   [pinkgorilla.storage.file]
   [pinkgorilla.storage.gist]
   [pinkgorilla.storage.repo]
   [pinkgorilla.storage.bitbucket]
   [pinkgorilla.storage.github]


  ; storage.direct works only in cljs


   #?(:cljs [pinkgorilla.storage.direct.file])
   #?(:cljs [pinkgorilla.storage.direct.gist])
   #?(:cljs [pinkgorilla.storage.direct.repo])
   #?(:cljs [pinkgorilla.storage.direct.bitbucket])))