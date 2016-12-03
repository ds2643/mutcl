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

(comment

;;
;;  salvage:
;;

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
;;

(defn src-copy
  "given reference to project file object, returns reference to source file-object"
  [file-object]
  {:post [(fs/childof? file-object src)]}
  (let [src (fs/absolute ())])))

(comment ;; TODO implement these functions
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
