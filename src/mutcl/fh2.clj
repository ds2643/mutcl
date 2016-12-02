(ns mutcl.fh2
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
