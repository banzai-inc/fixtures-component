(ns fixtures.adapters.jdbc
  (:require [fixtures.protocols :as protocols]
            [clojure.java.jdbc :refer [insert-multi! delete!]]))

(defn- load-table! [spec table records]
  (insert-multi! spec table records))

(defn- clear-table! [spec table]
  (delete! spec table []))

(defn- unload* [spec data]
  (println "DEBUG" "unload*; before try")
  (try
    (doseq [[table _] (reverse data)]
      (println "DEBUG" "unload*" table)
      (clear-table! spec table))
    (catch Exception e
      (println "DEBUG" "unload*" "fail" e)
      (println (.getMessage e)))))

(defrecord JDBCAdapter []
  fixtures.protocols.Loadable
  (load! [adapter {:keys [spec]} data]
    (try
      (unload* spec data) ;; in case the system failed to unload properly on shutdown
      (doseq [[table records] data]
        (when (not (empty? records))
          (load-table! spec table records)))
      (catch Exception e
        (println (.getMessage e)))))

  (unload! [_ {:keys [spec]} data]
    (println "DEBUG" "unload!")
    (unload* spec data)))

(defn jdbc-adapter
  "Constructor function for a Postgres adapter"
  []
  (map->JDBCAdapter {}))
