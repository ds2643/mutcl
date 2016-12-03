(ns mutcl.fh2
  (:require [clojure.string :as str]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [environ.core :refer [env]])
  (:use [mutcl.alg :refer :all])
  (:import [java.io PushbackReader]))

(defn gen-file-name
  "generate an name for a file in the specified directory such that no name space conflicts can occur"
  [dir]
  {:pre [(string? dir)
         (fs/exists? (fs/absolute dir))]}
  (let [dir-cont (fs/list-dir dir)
        possible-name (gensym)]
    (if (fs/exists? (fs/absolute (str path possible-name)))
      (gen-anon-file-name dir)
      possible-name)))

(defn clone-project
  "returns file-object"
  [path]
  {:pre [(string? path)
         (fs/exists? (fs/absolute path))]} ;; exists
  {:post }
  (let [copy-path (str "/tmp/" (generate-file-name "/tmp/"))]
    (do
      (fs/copy-dir path copy-path)
      (fs/absolute copy-path))))

(defn destroy-clone
  "deletes file-object and returns nil"
  [file-object]
  {:pre [(fs/exists? file-object)]}
  (fs/delete-dir file-object))

(defn restore-proj-clone
  "restore project clone from source path"
  [src-path clone-obj]
  {:post [(= (.getName clone-obj) n)]}
  (let [n (.getName clone-obj)]
  (fs/copy-dir-into (io/file src-path) clone-obj)))

(defn list-src-file-obj
  "given project file object, return sequence of source files as file-objects"
  [file-object]
  (let [all-files (.listFiles file-object)]
    (filter #(and (.endsWith (.getName %) ".clj")
                 not (.startsWith (.getName %) "project")) all-files)))

(defn perform-mutation tests
  "print results of mutation tests to stdout"
  [pc]
  (let [src-files (list-src-file-obj pc)]
      nil))

(defn conduct-tests
  "given project path and number of iterations, perform tests"
  [path n]
  (let [pc (clone-project path)] ;; project copy file object
    (do
      (for [x (range n)]
        ;; (perform-mutation-tests pc)
        ;; (restore-proj-clone path pc))
      (destroy-clone clone-path))))

