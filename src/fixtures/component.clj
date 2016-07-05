(ns fixtures.component
  (:require [com.stuartsierra.component :as c]
            [fixtures.core :as f]))

;; NOTE: Untested since version 0.4.0

(defrecord Fixtures [setup teardown adapter data]
  c/Lifecycle
  (start [component]
    (f/start (:db component) data {:adapter adapter :setup setup})
    (assoc component :loaded true))

  (stop [component]
    (f/stop (:db component) data {:adapter adapter :teardown teardown})
    (-> (assoc component :loaded false)
        (dissoc :setup :teardown :adapter :data))))

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
