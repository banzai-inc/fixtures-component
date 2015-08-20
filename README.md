# fixtures-component

Clojure library for loading and tearing down fixtures within a [component-based app](https://github.com/stuartsierra/component).

The library does one thing only – ensures setup and teardown is done in sync with the rest of the system. It's *totally* naive to *how* you load your fixtures. Whether you're loading a SQL, NoSQL, Postgres, or MySQL store, it's your responsibility to define setup and teardown procedures.

## Usage

Because we're dealing with fixtures – sample data – it's assumed you'll be applying them to either a development or test system.

First, define two functions: `setup`, and `teardown`, each capable of receiving one argument representing your database:

```clojure
(defn setup [store]
  (println "Setting up fixtures!")
  (comment "Do your thing: add records to your database using yesql, plain JDBC, whatever..."))

(defn teardown [store]
  (println "Tearing down fixtures")
  (comment "Delete your data"))
```

Add your `setup` and `teardown` functions to a config map, explicitly setting the *setup* and *teardown* keys:

```clojure
(def config {:fixtures {:setup setup
                        :teardown teardown}}
```

Declare your db component, and pass it as a dependency to the constructor function, `fixtures`, using the `store` key.

```clojure
(require '[com.stuartsierra.component :as c])

(c/system-map
  :db (...)
  :fixtures (c/using (fixtures (:fixtures config)))
              {:store :db})
```

## License

Copyright © 2015 Banzai Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
