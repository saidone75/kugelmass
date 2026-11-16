;; Copyright (c) 2020-2026 Saidone

(ns kugelmass.application
  (:gen-class)
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :refer [run-jetty]]
            [kugelmass.config :refer [config]]
            [kugelmass.routes :refer [home-routes]]))

(defn app-handler [{:keys [middleware]}]
  (reduce (fn [handler wrapper]
            (if (vector? wrapper)
              (apply (first wrapper) handler (rest wrapper))
              (wrapper handler)))
          (home-routes nil)
          (reverse middleware)))

(defrecord HttpServer [config server]
  component/Lifecycle
  (start [this]
    (if server
      this
      (let [server (run-jetty (app-handler config)
                              {:port (:http-port config) :join? false})]
        (println "Started kugelmass on" (str "http://localhost:" (:http-port config)))
        (assoc this :server server))))
  (stop [this]
    (when server (.stop server))
    (assoc this :server nil)))

(defn app-system [config]
  (component/system-map :http (map->HttpServer {:config config})))

(defn -main [& _]
  (let [system (component/start (app-system (config)))]
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. ^Runnable #(component/stop system)))))
