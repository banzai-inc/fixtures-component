(ns fixtures.component
  (:require [com.stuartsierra.component :as c]
            [fixtures.protocols :refer [load! unload!]]))

(defrecord Fixtures [setup teardown adapter data]
  c/Lifecycle
  (start [component]
    (let [store (:store component)]
      (if adapter
        (load! (adapter) store data)
        (setup store)))
    (assoc component :loaded true))

  (stop [component]
    (let [store (:store component)]
      (if adapter
        (unload! (adapter) store data)
        (teardown store)))
    (assoc component :loaded false)))

(defn fixtures
  "Creates a Fixtures component. Config options include:
    - adapter:  Constructor function for an adapter if we're data loading
                Required if setup/teardown are not provided.
                Ignored if setup/teardown are provided.
    - data:     Data expected by the adapter.
    - setup:    Setup function; receives one argument (store)
    - teardown: Teardown function; receives one argument (store)

    Example:
    (fixtures {:setup (fn [store] (println \"Set up my db!\"))
               :teardown ...})"
  [config]
  (map->Fixtures config))
