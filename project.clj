(defproject mutcl "0.1.0-SNAPSHOT"
  :description "Mutation testing library in Clojure, for Clojure (Leiningen) projects. Quis custodiet ipsos custodes?"
  :url "https://github.com/ds2643/mutcl.git"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [me.raynes/fs "1.4.6"]
                 [environ "0.5.0"]]
  :plugins [[lein-midje "3.1.3"]]
  :main ^:skip-aot mutcl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
