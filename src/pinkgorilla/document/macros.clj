(ns pinkgorilla.document.macros)

(defmacro tv
  [event-name]
  `(.. ~event-name -target -value))
