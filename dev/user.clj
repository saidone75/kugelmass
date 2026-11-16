(ns user
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [figwheel.main.api :as figwheel]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.reload :refer [wrap-reload]]
            [kugelmass.application :as application]
            [kugelmass.config :refer [config]]))

(defonce system (atom nil))

(defn start []
  (when-not @system
    (.mkdirs (io/file "dev-target/public"))
    (let [config (update (config) :middleware
                         #(into [[wrap-file "dev-target/public"] wrap-reload] %))]
      (reset! system (component/start (application/app-system config)))))
  @system)

(defn stop []
  (figwheel/stop-all)
  (when @system
    (component/stop @system)
    (reset! system nil)))

(defn go []
  (start)
  (try
    (figwheel/start {:mode :serve} "dev")
    (catch Throwable e
      (stop)
      (throw e))))

(defn reset []
  (stop)
  (go))

(defn cljs-repl []
  (figwheel/cljs-repl "dev"))
