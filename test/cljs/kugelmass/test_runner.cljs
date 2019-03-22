(ns kugelmass.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [kugelmass.core-test]
   [kugelmass.common-test]))

(enable-console-print!)

(doo-tests 'kugelmass.core-test
           'kugelmass.common-test)
