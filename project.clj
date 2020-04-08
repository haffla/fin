(defproject fin "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :main ^:skip-aot fin.handler
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/data.csv "1.0.0"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [hiccup "1.0.5"]]
  :plugins [[lein-ring "0.12.5"]
            [cider/cider-nrepl "0.24.0"]
            [lein-cljfmt "0.6.7"]]
  :ring {:handler fin.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
