(ns mutcl.core
  (:gen-class)
  (:require [clojure.string :as str]
            [me.raynes.fs :as fs])
  (:use [mutcl.alg :refer :all]
        [mutcl.fh :refer :all]))

(defn i-shell
  "programmatic imperative shell that executes entire mutation and data generation process
  n -> iterations"
  [proj-path n]
  (let [proj-obj (fs/absolute proj-path)
        proj-name (fs/name proj-obj)
        clone-name (str proj-name "_CLONE")] ;; TODO: guarantee avoiding naming conflict
    (do
      (copy-file proj-path clone-name)
      ;; copy project; original proj file spared of modification; all mutations performed on a clone
        (let [clone-obj (fs/absolute clone-name)
              clone-src-obj (src-dir proj-name clone-name)
              src-temp-name "SRC_TEMP"]
          (do
            (create-src-backup src-temp-name clone-src-obj)
            (for [x (range n)]
              (do
                (println "foo")
                (let [rand-src-path (choose-rand-src-file clone-src-obj)]
                  (overwrite-file rand-src-path (mutate-src-file rand-src-path)))
                ;; run tests & output data
                (refresh-src clone-src-obj src-temp-name)))
            (delete-file-recur src-temp-name)
            ;;(delete-file-recur clone-obj)
            )))))

(defn -main
  [& args]
  (println "0")
  (i-shell "sample" 0))
