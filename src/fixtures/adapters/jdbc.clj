(ns fixtures.adapters.jdbc
  (:require [fixtures.protocols :as protocols]
            [clojure.java.jdbc :refer [insert! delete!]]
            [clojure.tools.logging :refer [warn]]))

(defn- load-table! [spec table records]
  (apply (partial insert! spec table) records))

(defn- clear-table! [spec table]
  (delete! spec table []))

(defrecord JDBCAdapter []
  fixtures.protocols.Loadable
  (load! [adapter {:keys [spec]} data]
    (try
      (doseq [[table records] data]
        (load-table! spec table records))
      (catch Exception e
        (warn (.getMessage e)))))

  (unload! [adapter {:keys [spec]} data]
    (try
      (doseq [[table _] data]
        (clear-table! spec table))
      (catch Exception e
        (warn (.getMessage e))))))

(defn jdbc-adapter
  "Constructor function for a Postgres adapter"
  []
  (map->JDBCAdapter {}))
