(ns mutcl.core-test
  (:require [clojure.test :refer :all]
            [mutcl.core :refer :all]))

(deftest pick-rand-from-set-test
  (let [s #{1 3 5 7 9}]
    (is (not (= 1 (pick-rand-from-set 1 s))))))

(deftest set-sub-test
  "validate behavior of set-sub and its underlying dependencies"
  (let [m {:a #{1 2 3} :b #{4 5 6} :c #{7 8 9}}]
    (is (nil? (set-sub 10 m)))
    (is (and
          (or (= 2 (set-sub 1 m)) (= 3 (set-sub 1 m)))
          (not (= 1 (set-sub 1 m)))))))
;; TODO:  test error handling behavior
