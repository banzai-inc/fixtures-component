(defproject fixtures-component "0.2.2"
  :description "Development fixtures component for the reloadable pattern"
  :url "https://github.com/banzai-inc/fixtures-component"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aliases {"migrate"  ["run" "-m" "user/migrate"]
            "rollback" ["run" "-m" "user/rollback"]}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.stuartsierra/component "0.2.3"]
                 [org.clojure/java.jdbc "0.4.1"]
                 [org.clojure/tools.logging "0.3.1"]]
  :profiles {:dev {:source-paths ["dev"]
                   :repl-options {:init-ns user}
                   :resource-paths ["dev/resources"]   
                   :dependencies [[ragtime "0.5.1"]
                                  [com.h2database/h2 "1.3.170"]]}})
