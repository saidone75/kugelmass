(ns kugelmass.dev
  (:require [user :as user]))

(defn -main [& _]
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable user/stop))
  (user/go))
