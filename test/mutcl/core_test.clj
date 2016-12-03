(ns mutcl.core-test
  (:require [me.raynes.fs :as fs]
            [clojure.test :refer :all]
            [mutcl.core :refer :all]
            [midje.sweet :refer :all]
            [environ.core :refer [env]])))

;; mutcl.alg tests
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

;; TODO mutcl.fh2 tests
;; TODO clone-project
(comment
(fact "`clone-project` copies a file, assigning an randomly generated name and returning a valid file object"
  (let [temp '(env :temp) ;; TODO temp directory here
        dir-cont (fs/list-dir temp)
        filename (str (gensym)) ;; TODO modify to eliminate namespace conflict
        test-dir (fs/mkdir (str temp "/" filename))
        clone-dir (clone-project test-dir)
        result (fs/exists? clone-dir)]
    (do
      (fs/delete test-dir)
      (fs/delete-dir clone-dir)
      result)) => true))

(fact "`gen-file-name` generates a file-name string that does not conflict with others in the specified directory"
      (let [t (gen-file-name fs/*cwd*)])
      (fs/exists? t) => false
      (string? t) => true)


