(ns mutcl.fh
  (:require [clojure.string :as str]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:use [mutcl.alg :refer :all])
  (:import [java.io PushbackReader]))

;; TODO refactor
;;   mutate-branch
;;   read-forms
;;   mutate-src-file
;;   copy-file                    X
;;   project-src-files
;;   src-dir
;;   choose-rand-src-file
;;   delete-mutant
;;   make-dir
;;   delete-file-recur
;;   create-src-backup
;;   refresh-src
;;   ishell

(defn mutate-branch
  "given a Clojure program, return a copy(?) of the program for which a single node in the ast has been substituted with some substitution function"
  [f b]
  (stochastic-tree-f-app f b))

(defn read-forms
  "read clojure src as programmic data"
  [file]
  (let [rdr (-> file io/file io/reader PushbackReader.)
        sentinel (Object.)]
    (loop [forms []]
      (let [form (edn/read {:eof sentinel} rdr)]
        (if (= sentinel form)
          forms
          (recur (conj forms form)))))))

(defn mutate-src-file
  [src-obj]
  (let [src-tree (read-forms src-obj)]
    (stochastic-tree-f-app #(mutate-branch set-sub %) src-tree)))

(defn copy-file                                      ;; OK
  "copies a directory at specified path to destination path"
  [src-path dest-path]
  (fs/copy-dir src-path dest-path))

(defn overwrite-file
  "writes content to disk in place of specified file"
  [file-name cont-as-str]
  {:post (string? (slurp file-name))}
  (do
    (io/delete-file file-name)
    (println cont-as-str)
    (spit file-name cont-as-str)))

(defn project-src-files                             ;; OK
  "return list of src file contents"
  [project-name]
  (filter #(and (.endsWith (.getName %) ".clj")
                 not (.startsWith (.getName %) ".")))
          (file-seq (io/file (str project-name "/src/" project-name))))

(defn src-dir                                     ;; OK
 "returns lein project's source file directory object"
  [project-name clone-name]
  (io/file (str clone-name "/src/" project-name)))

(defn choose-rand-src-file
  [src-dir]
  (rand-nth src-dir))

(defn delete-mutant
  [file-name]
  (io/delete-file file-name))

(defn make-dir                ;; OK
  "create a directory"
  [name]
  {:pre [(string? name)]}
  (.mkdir (io/file name)))

(defn delete-file-recur       ;; OK
  "delete file recursively"
  [file-name]
  (let [func (fn [func f]
               (when (.isDirectory f)
                 (doseq [f2 (.listFiles f)]
                   (func func f2)))
               (io/delete-file f))]
    (func func (io/file file-name))))

(defn create-src-backup
  "create souce backup for later reference"
  [src-temp-path src-obj-path]
  (do
    (make-dir src-temp-path)
    (copy-file src-obj-path src-temp-path)))

(defn refresh-src
  [src-dir back-up-dir]
  (do
    (delete-file-recur src-dir)
    (io/copy back-up-dir src-dir)))

(defn i-shell
  "programmatic imperative shell that executes entire mutation and data generation process
  n -> iterations"
  [proj-path n]
  (let [proj-obj (fs/absolute proj-path)
        proj-name (fs/name proj-obj)
        clone-name (str proj-name "_CLONE")] ;; TODO: guarantee avoiding naming conflict
    (do
      (copy-file proj-path clone-name)
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
