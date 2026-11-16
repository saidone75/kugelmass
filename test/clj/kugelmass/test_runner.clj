(ns kugelmass.test-runner
  (:require [clojure.test :as test]
            [kugelmass.common-test]
            [kugelmass.example-test]
            [kugelmass.application-test]))

(defn -main [& _]
  (let [{:keys [fail error]} (test/run-tests 'kugelmass.common-test
                                           'kugelmass.example-test
                                           'kugelmass.application-test)]
    (shutdown-agents)
    (System/exit (if (zero? (+ fail error)) 0 1))))
