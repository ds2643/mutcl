(ns mutcl.fh2
  (:require [clojure.string :as str]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.edn :as edn]
            [environ.core :refer [env]])
  (:use [mutcl.alg :refer :all])
  (:import [java.io PushbackReader]))

;; fh(1) functions:
;;   mutate-branch
;;   read-forms
;;   mutate-src-file
;;   project-src-files
;;   src-dir
;;   choose-rand-src-file
;;   delete-mutant
;;   make-dir
;;   delete-file-recur
;;   create-src-backup
;;   refresh-src
;;   ishell

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

;; TODO store this file in ~/temp
(defn clone-project
  "returns file-object"
  [path]
  {:pre [(string? path)
         (fs/exists? (fs/absolute path))]} ;; exists
  {:post }
  (let [copy-name (generate-file-name fs/*cwd*)]
    (do
      (fs/copy-dir path copy-name)
      (fs/absolute copy-name))))

(comment ;; TODO implement these functions
(defn clone-project "returns file-object" [path] nil)
(defn destroy-clone "returns nil" [path] nil)
(defn src-copy "returns file-object" [file-object] nil) ;; TODO consider enclosing temporary directory -> not necessary just move file contents rather than file
(defn replace-src "replace mutated src file contents" [src-file-object dst-file-object] nil)
)

(comment
(defn conduct-tests
  "given project path and number of iterations, perform tests"
  [path n]
  (let [pc (clone-project path) ;; project copy file object
        sc (src-copy pc) ] ;; src copy file object
    (do
      (for [x (range n)]
        ;; perform mutation tests
        ;; (replace-src pc sc))
      (destroy-clone clone-path))))
)
