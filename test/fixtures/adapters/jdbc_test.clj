(ns fixtures.adapters.jdbc-test
  (:require [user :as user]
            [clojure.test :refer :all]
            [fixtures.protocols :refer [load! unload!]]
            [fixtures.adapters.jdbc :refer [jdbc-adapter]]
            [clojure.java.jdbc :refer [query delete!]]))

(def db {:spec (-> (user/load-config) :datastore :db-spec)})
(def data [[:users [{:id 1} {:id 2}]]])
(def error-data [[:non_existent_table [{:id 1}]]])

(defn clear [spec]
  (delete! spec :users []))

(deftest load-test
  (let [spec (:spec db)]
    (clear spec)
    (load! (jdbc-adapter) db data)
    (= 2 (count (query (:spec db) ["select * from users"])))))

(deftest unload-test
  (let [spec (:spec db)
        adapter (jdbc-adapter)]
    (clear spec)
    (load! adapter db data)
    (unload! adapter db data)
    (= 0 (count (query (:spec db) ["select * from users"])))))

(deftest error-test
  (load! (jdbc-adapter) db error-data)
  (unload! (jdbc-adapter) db error-data))
