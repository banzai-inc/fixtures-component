# fixtures-component

Clojure library for loading and tearing down fixtures within a [component-based app](https://github.com/stuartsierra/component).

## Lein Installation

`[fixtures-component "0.3.0"]`

## Usage

Because we're dealing with fixtures – sample data – it's assumed you'll be applying them to either a development or test system.

There are two ways to use `fixtures-component`:

1. Using a fixtures adapter, tailored for your database, or
2. Using a plain-jane, custom `setup` function.

A JDBC adapter comes built-in for method #1.

### 1) Using an Adapter

We'll use the built-in JDBC adapter to demonstrate. Your adapter dictates the shape of the data you must pass to the fixtures component.

An adapter is a protocol containing two functions:

* `load!`: Called on component `start`. In the case of the JDBC adapter, this is when your records are "loaded" into the tables.
* `unload!`: Called on component `stop`. Your records are deleted from the tables.

To get started, we define a vector of vectors containing table names and maps representing records of data:

```clojure
(def data [[:users [{:id 1 :username "demo1@example.com"}
                    {:id 2 :username "demo2@example.com"}]]
           [:phones [{:id 1 :user_id 1 :text "555-383-9999"}
                     {:id 2 :user_id 2 :text "555-898-2222"}]]])
```

Our table suggests we have two SQL tables named "users" and "phones", each with two records, with their respective field names. Now, let's add our data and adapter to the configuration map which we'll pass to the component:

```clojure
(require '[fixtures.adapters.jdbc :refer [jdbc-adapter]])
 
(def config {:fixtures {:adapter jdbc-adapter
                        :data data}})
```

Finally, we need to add our component to the system map, including our datastore as a dependency:

```clojure
(require '[fixtures.component :refer [fixtures]])
 
(system-map
  :db (...)
  :fixtures (c/using (fixtures (:fixtures config))
              [:db]}))
```

**That's it!** Your done. Make sure your `fixtures` component is passed the datastore in the `:db` key. When you start your system, all your data should be bootstrapped into the database.

#### Build Your Own Adapter

An adapter is no more than an implementation of the `fixtures.protocols.Loadable` protocol. Protocol definition:

```clojure
(defprotocol Loadable
  (load! [adapter db data])
  (unload! [adapter db data]))
```

Implement your own adapter with the two methods, and you're set:

```clojure
(require '[fixtures.protocols :as p])

(defrecord MyFavoriteDataStore
  fixtures.protocols.Loadable
  (load! [adapter db data]
    ...)

  (unload! [adapter db data]
    ...))
```

See [here for a full example](https://github.com/banzai-inc/fixtures-component/blob/master/src/fixtures/adapters/jdbc.clj).

### 2) Basic Setup and Teardown

Leveraging an adapter is the preferred method for loading fixtures, however, if you're in a hurry, these two functions provide a quick and dirty way load your datastore.

This method does one thing only – ensures setup and teardown is done in sync with the rest of the system. It's *totally* naive to *how* you load your fixtures. Whether you're loading a SQL, NoSQL, Postgres, or MySQL db, it's your responsibility to define setup and teardown procedures.

First, define two functions: `setup`, and `teardown`, each capable of receiving one argument representing your database:

```clojure
(defn setup [db]
  (println "Setting up fixtures!")
  (comment "Do your thing: add records to your database using yesql, plain JDBC, whatever..."))

(defn teardown [db]
  (println "Tearing down fixtures")
  (comment "Delete your data"))
```

Add your `setup` and `teardown` functions to a config map, explicitly setting the *setup* and *teardown* keys:

```clojure
(def config {:fixtures {:setup setup
                        :teardown teardown}}
```

Keep in mind, you cannot mix method #1 and #2, so don't declare `setup` and `adapter`, or `setup` and `data`. Declare your db component, and pass it as a dependency to the constructor function, `fixtures`, using the `db` key.

```clojure
(require '[com.stuartsierra.component :as c])

(c/system-map
  :db (...)
  :fixtures (c/using (fixtures (:fixtures config)))
              [:db])
```

## License

Copyright © 2015 Banzai Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
