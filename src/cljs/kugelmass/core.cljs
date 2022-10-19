;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.core
  (:require [reagent.core :as r]
            [reagent.dom :as rdom]
            [kugelmass.taglines :as taglines]
            [kugelmass.quotes :as quotes]
            [kugelmass.pages :as pages]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(enable-console-print!)

(defonce app-state (r/atom {}))

(defonce page-state (r/atom nil))

(defn- header []
  [:div.header
   [:div.title [:a {:href "/#/"} "S A I D O N E"]]
   [:div.tagline (:tagline @app-state)]])

(defn- content []
  [:div.content {:id "content"} (:content @page-state)])

(defn- footer []
  [:div.footer
   [:div.quote (:quote @app-state)]])

(defn- toolbar []
  [:div.toolbar
   [:a {:href "#/"} "Home"]
   [:a {:href "#/resume"} "Resume"]])

(defn- site []
  [:div
   [header]
   [toolbar]
   [:div.body
    [content]]
   [footer]])

(defn- render []
  (rdom/render [site] (js/document.getElementById "app")))

(defn- load-page [page]
  (set! page-state (pages/get-page page))
  (render))

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (load-page :life))

(secretary/defroute "/resume" []
  (load-page :resume))

(secretary/defroute "/cm" []
  (load-page :cm))

(let [h (History.)]
  (events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

(swap! app-state assoc :tagline (taglines/get-tagline))
(swap! app-state assoc :quote (quotes/get-quote))

(js/setInterval #(swap! app-state assoc :tagline (taglines/get-tagline)) (+ 10000 (rand-int 5000)))
(js/setInterval #(swap! app-state assoc :quote (quotes/get-quote)) (+ 20000 (rand-int 5000)))

(defn version [] "0.2.2.1")
(aset js/window "version" kugelmass.core/version)
