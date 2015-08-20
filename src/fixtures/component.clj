(ns fixtures.component
  (:require [com.stuartsierra.component :as c]))

(defrecord Fixtures [setup teardown]
  c/Lifecycle
  (start [component]
    (setup (:store component))
    (assoc component :loaded true))

  (stop [component]
    (teardown (:store component))
    (assoc component :loaded false)))

(defn fixtures
  "Creates a Fixtures component. Config options include:
    - setup:    setup function; receives one argument (store)
    - teardown: teardown function; receives one argument (store)

    Example:
    (fixtures {:setup (fn [store] (println \"Set up my db!\"))
               :teardown ...})"
  [config]
  (map->Fixtures config))
