(ns fixtures.protocols)

(defprotocol Loadable
  (load! [adapter store data])
  (unload! [adapter store data]))
