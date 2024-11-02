;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.pages.random.pwd
  (:require [reagent.core :as r]
            [ajax.core :refer [GET]]))

(def page (r/atom {}))
(def state (r/atom {}))

(defn- handle-success [response]
  (.log js/console (:password response))
  (swap! state assoc :pwd (:password response))
  (swap! state assoc :entropy (:entropy response))
  (swap! state assoc :msg (str "Password generated with " (:entropy response) " of entropy")))

(defn- handle-error [response]
  (swap! state assoc :pwd "error")
  (swap! state assoc :entropy nil)
  (swap! state assoc :msg "Error"))

(defn get-password []
  (GET "/random/password"
       {:params {:format :json}
        :response-format :json
        :keywords? true
        :handler handle-success
        :error-handler handle-error}))

(defn- copy-to-clipboard []
  (js/navigator.clipboard.writeText (-> (.. js/document (getElementById "pwd")) .-innerText))
  (swap! state assoc :msg "Copied to clipboard"))

(defn draw-page []
  [:div
   [:div {:class "pwd" :id "pwd" :on-click copy-to-clipboard} (:pwd @state)]
   [:input {:class "pwd-another" :type "button" :value "Generate another"
            :on-click get-password}]
   [:div {:class "pwd-state"} (:msg @state)]])

(add-watch state :password #(swap! page assoc :content (draw-page)))

(get-password)

(defn content []
  page)
