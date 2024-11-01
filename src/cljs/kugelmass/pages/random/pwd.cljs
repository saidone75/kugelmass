;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.pages.random.pwd
  (:require [reagent.core :as r]
            [ajax.core :refer [GET]]))

(def page (r/atom {}))
(def res (r/atom {}))

(defn draw-page []
  [:div
   (:pwd @res)
   [:br]
   [:input {:type "button" :value "Generate another"
            :on-click get-password}]])

(defn- handle-success [response]
  (.log js/console (str response))
  (swap! res assoc :pwd (str response))
  (swap! page assoc :content (draw-page)))

(defn- handle-error [response]
  (.log js/console (str response)
        (swap! res assoc :pwd "error")))

(defn get-password []
  (GET "/random/pwd"
       {:response-format :text
        :handler handle-success
        :error-handler handle-error}))

(get-password)

(defn content []
  page)
