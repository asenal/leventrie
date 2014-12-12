(ns leventrie.core-test
  (:require [clojure.test :refer :all]
            [leventrie.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(def trie (reduce add-to-trie {}  ["abc" "abcd" "b" "a"]))
;(def trie-big (reduce add-to-trie {}  (line-seq (clojure.java.io/reader "../data/address.txt"))))
