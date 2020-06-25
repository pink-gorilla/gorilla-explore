(ns pinkgorilla.explore.macros)

(defmacro tv
  [event-name]
  `(.. ~event-name -target -value))
