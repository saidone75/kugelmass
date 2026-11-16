(ns kugelmass.test-runner
  (:require [figwheel.main.testing :refer-macros [run-tests-async]]
            [kugelmass.core-test]
            [kugelmass.common-test]))

(defn -main [& _]
  (run-tests-async 10000))
