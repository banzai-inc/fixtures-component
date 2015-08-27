(ns fixtures.component
  (:require [com.stuartsierra.component :as c]
            [fixtures.protocols :refer [load! unload!]]))

(defrecord Fixtures [setup teardown adapter data]
  c/Lifecycle
  (start [component]
    (let [db (:db component)]
      (if adapter
        (load! (adapter) db data)
        (setup db)))
    (assoc component :loaded true))

  (stop [component]
    (let [db (:db component)]
      (if adapter
        (unload! (adapter) db data)
        (teardown db)))
    (assoc component :loaded false)))

(defn fixtures
  "Creates a Fixtures component. Config options include:
    - adapter:  Constructor function for an adapter if we're data loading
                Required if setup/teardown are not provided.
                Ignored if setup/teardown are provided.
    - data:     Data expected by the adapter.
    - setup:    Setup function; receives one argument (db)
    - teardown: Teardown function; receives one argument (db)

    Example:
    (fixtures {:setup (fn [db] (println \"Set up my db!\"))
               :teardown ...})"
  [config]
  (map->Fixtures config))
