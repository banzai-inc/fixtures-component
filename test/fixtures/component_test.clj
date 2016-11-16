(ns fixtures.component-test
  (:require [clojure.test :refer :all]
            [com.stuartsierra.component :as c]
            [fixtures.component :refer [fixtures]]
            [fixtures.protocols :as p]))

(defrecord Adapter []
  p/Loadable
  (load! [adapter db data] (reset! db :loaded))
  (unload! [adapter db data] (reset! db :unloaded)))

(defn adapter [] (Adapter.))

(defn new-component []
  (fixtures {:adapter adapter
             :data []
             :db (atom nil)}))

(deftest startup-test
  (is (= :loaded @(:db (c/start (new-component))))))

(deftest shutdown-test
  (let [component (c/start (new-component))]
    (c/stop component)
    (is (= :unloaded @(:db component)))))
