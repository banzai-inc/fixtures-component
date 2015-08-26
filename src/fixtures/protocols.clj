(ns fixtures.protocols)

(defprotocol Loadable
  (load! [adapter db data])
  (unload! [adapter db data]))
