(ns mutcl.core
  (:gen-class)
  (:require [clojure.zip :as z]
            [clojure.string :as str]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import [java.io PushbackReader]))

(defn stochastic-tree-f-app [f tree]
  "apply function to an arbitrary node of an unbalanced, asymmetric tree, with equal probability of application of the function to all nodes in the tree"
  (let [zp    (z/zipper list? seq (fn [_ c] c) tree)
        nodes (->> (iterate z/next zp)
                   (take-while (complement z/end?))
                   (filter (comp #(not (seq? %))  z/node))
                   (into []))]
    (-> (rand-nth nodes)
        (z/edit f)
        z/root)))

(def cloj-form-sets
  {:arithmetic #{'+ '- '/ '*}
   :logic #{'or 'and}
   :comparison #{'> '< '<= '>=}
   :equality #{'not= '=}
   :bit-logic #{'bit-or 'bit-and 'bit-and-not 'bit-xor}
   :bit-shift {'bit-shift-right 'bit-shift-left}
   :is #{'class? 'decimal? 'empty? 'future? 'identical? 'integer? 'keyword? 'list? 'map? 'nil? 'neg? 'pos? 'number? 'ratio? 'rational? 'record? 'symbol? 'string? 'true? 'var? 'vector? 'zero?}
   :id #{'inc 'dec}
   :eo #{'even? 'odd?}})

(defn rand-from-containing-set
  "returns item randomly from containing set"
  [item]
  (let [art-set (cloj-form-sets :arithmetic)
        lgc-set (cloj-form-sets :logic)
        cmp-set (cloj-form-sets :comparision)
        eql-set (cloj-form-sets :equality)
        btl-set (cloj-form-sets :bit-logic)
        bts-set (cloj-form-sets :bit-shift)
        is-set (cloj-form-sets :is)
        eo-set (cloj-form-sets :eo)
        cnd-set (cloj-form-sets :cnd)
        another-one (fn [ix sx] (rand-nth (filter (partial not= ix) (seq sx))))]
  (cond
    (contains? art-set item) (another-one item art-set)
    (contains? lgc-set item) (another-one item lgc-set)
    (contains? cmp-set item) (another-one item cmp-set)
    (contains? eql-set item) (another-one item eql-set)
    (contains? btl-set item) (another-one item btl-set)
    (contains? bts-set item) (another-one item bts-set)
    (contains? is-set item)  (another-one item is-set)
    (contains? eo-set item)  (another-one item eo-set)
    (contains? cnd-set item) (another-one item cnd-set)
    ;; TODO: contract motif-> necessary to find or write cond altern macro
    :else nil)))

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
    (make-dir src-temp-name)
    (copy-file src-obj-path src-temp-path))

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
        prog-name (fs/name proj-obj)
        clone-name (str proj-name "_clone")] ;; TODO: guarantee avoiding naming conflict
    (do
        (copy-file proj-path clone-name) ;; copy project; original proj file spared of modification; all mutations performed on a clone
        (let [clone-obj (fs/absolute clone-name)
              clone-src-obj (src-dir proj-name clone-name)
              src-temp-name "src_temp"]
          (do
            (create-src-backup src-temp-name clone-src-obj)
            (for [x (range n)]
              (println "foo")
              ;; mutate file contents
              ;; run tests
              ;; output data
              (refresh-src clone-src-obj src-temp-name))
            (delete-file-recur src-temp-name)
            (delete-file-recur clone-obj))))))

(defn -main
  [& args]
  (println "0"))
