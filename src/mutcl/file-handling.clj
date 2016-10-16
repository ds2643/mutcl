(ns m.core
  (:gen-class)
  (:require [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import [java.io PushbackReader]))

;; TODO add helper for slurping
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
    (spit file-name cont-as-str)))

(defn project-src-files                             ;; OK
  "return list of src file contents"
  [project-name]
  (filter #(and (.endsWith (.getName %) ".clj")
                 (not (.startsWith (.getName %) ".")))
          (file-seq (io/file (str project-name "/src/" project-name)))))

(defn src-files                                     ;; UNTESTED
  "return list of src file contents"
  [src-dir]
  (filter #(and (.endsWith (.getName %) ".clj")
                (not (.startsWith (.getName %) ".")))
          src-dir))

(defn src-dir                                     ;; OK
  "returns lein project's source file directory"
  [project-name]
  (io/file (str project-name "/src/" project-name)))

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

(defn refresh-src
  [src-dir back-up-dir]
  (do
    (delete-file-recur src-dir)
    (io/copy back-up-dir src-dir)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; DEPR ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment ;;depr
  (defn hide-src-file
    ;; DEPR
    "hide src file by appending '._hidden_' to name"
    [file]
    (let [dest-path (str (.getParent file))
          copy-name (str "._hidden_" (.getName file))
          copy (io/file (str dest-path copy-name))]
      (io/copy file copy)))


  (defn clone-project
 ;;DEPR
  "clones project to spare unforseen side-effects of mutation"
  [project-name]
  (let [project-file (io/file project-name)
        project-dir-name (str (.getParent project-file) (.getName project-file))
        temp-project-dir-name (str (.getParent project-file) (.getName project-file) ".bak")]
    (fs/copy project-dir-name temp-project-dir-name)))

  (defn clone-project
    ;; DEPR
    ;; DANGER: POTENTIALLY DELETES FILE CONTENTS FROM PROJECT FILE!!!
  "given a filename of a lein project, produce a mutant template"
  [project-name]
  (let
      [project (fs/absolute (str project-name))
       test-path (fs/absolute
                   (str (fs/name project) "/"
                        (fs/name project) "/"))
       src-path  (fs/absolute
                   (str (fs/name project) "/src/"
                        (fs/name project) "/"))]
  {:pre [(and
          (reduce #(and %1 %2) (map fs/exists?
                           '(project test-path src-path)))
          (reduce #(and %1 %2) (map fs/directory?
                           '(project test-path src-path)))
          (fs/child-of? project src-path)
          (fs/child-of? project test-path))]}
  ;; worked in terminal:
  ;; (fs/copy-dir (fs/absolute (fs/name fs/*cwd*)) (fs/absolute "../mutcl_1"))
     (fs/copy-dir project (str (fs/*cwd*) (fs/name project) "_temp"))))

  )
