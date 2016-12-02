(ns mutcl.core
  (:gen-class)
  (:require [clojure.string :as str])
  (:use [mutcl.alg :refer :all]
        [mutcl.fh :refer :all]))


(defn -main
  [& args]
  (println "0")
  (i-shell "sample" 0))
