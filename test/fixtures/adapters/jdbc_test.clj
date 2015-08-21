(ns fixtures.adapters.jdbc-test
  (:require [user :as user]
            [clojure.test :refer :all]
            [fixtures.protocols :refer [load! unload!]]
            [fixtures.adapters.jdbc :refer [jdbc-adapter]]
            [clojure.java.jdbc :refer [query delete!]]))

(def store {:spec (-> (user/load-config) :datastore :db-spec)})
(def data [[:users [{:id 1}
                    {:id 2}]]])

(defn clear [spec]
  (delete! spec :users []))

(deftest load-test
  (let [spec (:spec store)]
    (clear spec)
    (load! (jdbc-adapter) store data)
    (= 2 (count (query (:spec store) ["select * from users"])))))

(deftest unload-test
  (let [spec (:spec store)
        adapter (jdbc-adapter)]
    (clear spec)
    (load! adapter store data)
    (unload! adapter store data)
    (= 0 (count (query (:spec store) ["select * from users"])))))
