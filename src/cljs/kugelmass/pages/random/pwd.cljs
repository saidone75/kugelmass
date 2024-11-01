;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.pages.random.pwd
  (:require [reagent.core :as r]
            [ajax.core :refer [GET]]))

(def page (r/atom {}))
(def state (r/atom {}))

(defn get-password []
  (GET "/random/pwd"
       {:response-format :text
        :handler handle-success
        :error-handler handle-error}))

(defn draw-page []
  [:div
   [:div {:id "pwd" :on-click copy-to-clipboard} (:pwd @state)]
   [:input {:type "button" :value "Generate another"
            :on-click get-password}]
   [:div (:msg @state)]])

(defn- copy-to-clipboard []
  (js/navigator.clipboard.writeText (-> (.. js/document (getElementById "pwd")) .-innerText))
  (swap! state assoc :msg "Copied to clipboard")
  (swap! page assoc :content (draw-page)))

(defn- handle-success [response]
  (.log js/console (str response))
  (swap! state assoc :pwd (str response))
  (swap! state assoc :msg "Password generated")
  (swap! page assoc :content (draw-page)))

(defn- handle-error [response]
  (swap! state assoc :pwd "error")
  (swap! page assoc :content (draw-page)))

(get-password)

(defn content []
  page)
