(ns mutcl.alg
  (:require [clojure.zip :as z]
            [clojure.string :as str]))

(defn stochastic-tree-f-app
  "apply function to an arbitrary node of an unbalanced, asymmetric tree, with equal probability of application of the function to all nodes in the tree"
  [f tree]
  (let [zp    (z/zipper list? seq (fn [_ c] c) tree)
        nodes (->> (iterate z/next zp)
                   (take-while (complement z/end?))
                   (filter (comp #(not (seq? %))  z/node))
                   (into []))]
    (-> (rand-nth nodes)
        (z/edit f)
        z/root)))

(def cloj-form-sets
  "hard encoding of valid substitutions"
  {:arithmetic #{'+ '- '/ '*}
   :logic #{'or 'and}
   :comparison #{'> '< '<= '>=}
   :equality #{'not= '=}
   :bit-logic #{'bit-or 'bit-and 'bit-and-not 'bit-xor}
   :bit-shift {'bit-shift-right 'bit-shift-left}
   :is #{'class? 'decimal? 'empty? 'future? 'identical? 'integer? 'keyword? 'list? 'map? 'nil? 'neg? 'pos? 'number? 'ratio? 'rational? 'record? 'symbol? 'string? 'true? 'var? 'vector? 'zero?}
   :id #{'inc 'dec}
   :eo #{'even? 'odd?}})

(defn pick-rand-from-set
  "pick another item from a set sx from which ix is a number"
  [ix sx]
  (rand-nth (filter (partial not= ix) (seq sx))))

(defn set-sub-1
  "helper recusive function to set-sub"
  [syb set-keys form-sets]
  (cond
    (empty? set-keys)
      nil ;; no match yields substitution with null value
    (contains? (form-sets (first set-keys)) syb)
      (pick-rand-from-set syb (form-sets (first set-keys)))
    :else
      (set-sub-1 syb (rest set-keys) form-sets)))

(defn set-sub
  "substitute symbol with another from key-value map of sets"
  [syb form-sets]
  (let
    [set-keys (keys form-sets)]
    (set-sub-1 syb set-keys form-sets)))





