(ns mutcl.filehandling
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

(defn i-shell
  "programmatic imperative shell that executes entire mutation and data generation process"
  [program-path]
  (let [program-dir (fs/absolute program-path)
        program-name (fs/name program-dir)
        program-clone-name (str program-name "_clone")]
    (do
        (copy-file program-dir program-clone-name)
        ;; copy file being tested (keep name handy as project_clone)
        (let [clone-dir (fs/absolute program-clone-name)

              ]

          )
        (comment
        2. copy src file contents of project_clone into a temporary folder
        3. loop
           a. mutate file contents
           b. run tests
           c. output data
           d. replace source with temporary folder contents
        5. delete project_clone
        ))))

;;> mutcl Projects/my_project # leiningen project
