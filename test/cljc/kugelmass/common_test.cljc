;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.common-test
  #? (:cljs (:require-macros [cljs.test :refer (is deftest testing)]))
  (:require [kugelmass.common :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test])))

(deftest example-passing-test-cljc
  (is (= 1 1)))
