(ns kugelmass.core
  (:require [reagent.core :as r :refer [atom]]
            [kugelmass.taglines :as taglines]
            [kugelmass.quotes :as quotes]
            [kugelmass.pages :as pages]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(enable-console-print!)

(defonce app-state (r/atom {}))

(defonce intervals (atom {}))

(defn- load-page [page]
  (js/clearInterval (:page @intervals))
  (let [page (pages/get-page page)]
    (swap! app-state assoc :content (:content page))
    (let [set-interval (:set-interval page)]
      (if (not (nil? set-interval))
        (swap! intervals assoc :page
               (js/setInterval
                #(swap! app-state assoc :content ((:function set-interval)))
                (:interval set-interval)))))))

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (load-page :life))

(secretary/defroute "/resume" []
  (load-page :resume))

(defn- header []
  [:div.header
   [:div.title "S A I D O N E"]
   [:div.tagline (:tagline @app-state)]])

(defn- content [page]
  [:div.content {:id "content"} (:content @app-state)])

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

(defn- update-quote! []
  (swap! app-state assoc :quote (quotes/get-quote))
  (if (nil? (:quote @intervals))
    (swap! intervals assoc :quote (js/setInterval update-quote! (+ 12000 (rand-int 8000))))))

(defn- update-tagline! []
  (swap! app-state assoc :tagline (taglines/get-tagline))
  (if (nil? (:tagline @intervals))
    (swap! intervals assoc :tagline (js/setInterval update-tagline! (+ 8000 (rand-int 4000))))))

(defn- render []
  (reagent.dom/render [site] (js/document.getElementById "app")))

(let [h (History.)]
  (events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

(update-quote!)
(update-tagline!)
