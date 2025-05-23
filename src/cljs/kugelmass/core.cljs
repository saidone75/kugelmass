;; Copyright (c) 2020-2024 Saidone

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

(defn version [] "0.3.11")

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
   [:a {:href "#/tools"} "Tools"]
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

(secretary/defroute "/tools" []
  (load-page :tools))

(secretary/defroute "/cm" []
  (load-page :cm))

(secretary/defroute "/cm-clj" []
  (load-page :cm-clj))

(secretary/defroute "/pwd" []
  (load-page :pwd))

(let [h (History.)]
  (events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

(swap! app-state assoc :tagline (taglines/get-tagline))
(swap! app-state assoc :quote (quotes/get-quote))

(js/setInterval #(swap! app-state assoc :tagline (taglines/get-tagline)) (+ 10000 (rand-int 5000)))
(js/setInterval #(swap! app-state assoc :quote (quotes/get-quote)) (+ 20000 (rand-int 5000)))

(aset js/window "version" kugelmass.core/version)
(aset js/window "quote" kugelmass.quotes/get-quote)
(aset js/window "quotes" kugelmass.quotes/get-quotes)
(aset js/window "tagline" kugelmass.taglines/get-tagline)
(aset js/window "taglines" kugelmass.taglines/get-taglines)
(aset js/window "pwd" kugelmass.pages.random.pwd/get-password)
