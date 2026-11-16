(ns kugelmass.application-test
  (:require [clojure.test :refer [deftest is testing]]
            [cheshire.core :as json]
            [com.stuartsierra.component :as component]
            [kugelmass.application :as application]
            [kugelmass.config :as config]))

(deftest middleware-order
  (let [wrapper (fn [handler label]
                  (fn [request]
                    (update (handler request) :trace (fnil conj []) label)))
        handler (application/app-handler
                  {:middleware [[wrapper :outer] [wrapper :inner]]})]
    (is (= [:inner :outer] (:trace (handler {:request-method :get :uri "/"}))))))

(deftest http-routes
  (let [handler (application/app-handler (config/config))
        request (fn [uri params]
                  (handler {:request-method :get :uri uri :params params
                            :query-string (when (= params {:format "json"}) "format=json")
                            :headers {} :scheme :http :server-name "localhost"
                            :server-port 10555}))]
    (testing "the host page and stylesheet are served"
      (is (= 200 (:status (request "/" {}))))
      (is (= 200 (:status (request "/css/style.css" {})))))
    (testing "the password endpoint returns JSON"
      (let [response (request "/random/password" {:format "json"})]
        (is (= 200 (:status response)))
        (is (= "application/json" (get-in response [:headers "Content-Type"])))
        (is (string? (:password (json/parse-string (:body response) true))))))
    (testing "the BirdNET error page still renders"
      (is (= 200 (:status (request "/birdnetpi/error/503" {})))))))

(deftest server-lifecycle
  (let [started (component/start (application/app-system
                                  (assoc (config/config) :http-port 0)))
        server (get-in started [:http :server])]
    (try
      (is (.isStarted server))
      (is (identical? server (get-in (component/start started) [:http :server])))
      (finally
        (let [stopped (component/stop started)]
          (is (nil? (get-in stopped [:http :server])))
          (is (.isStopped server)))))))
