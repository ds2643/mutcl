(ns mutcl.core
  (:gen-class)
  (:require [clojure.zip :as z]
            [clojure.string :as str]
            [mutcl.filehandling :as fh]))

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
        another-one (fn [ix sx] (rand-nth (filter (partial not= ix) (seq sx))))
        ]
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

(defn -main
  [& args]
  (println "0"))
