(ns fixtures.core
  (:require [fixtures.protocols :refer [load! unload!]]))

(defn start
  "Starts a component"
  [db data {:keys [adapter setup]}]
  (if adapter
    (load! (adapter) db data)
    (setup db)))

(defn stop
  "Stops a component"
  [db data {:keys [adapter teardown]}]
  (if adapter
    (unload! (adapter) db data)
    (teardown db)))
