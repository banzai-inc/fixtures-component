(ns fixtures.mount
  (:require [fixtures.core :as f]))

(defn start
  "Config options include:
   - adapter: JDBC adapter, etc.
   - setup: Custom setup function"
  [db data config]
  (f/start db data config))
 
(defn stop
  "Config options include:
   - adapter: JDBC adapter, etc.
   - setup: Custom setup function"
  [db data config]
  (f/stop db data config))
