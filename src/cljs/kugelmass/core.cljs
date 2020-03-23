(ns kugelmass.core
  (:require [reagent.core :as reagent :refer [atom]]
            [kugelmass.taglines :as taglines]
            [kugelmass.quotes :as quotes]
            [kugelmass.pages :as pages]
            [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(enable-console-print!)

(defonce app-state (atom {:content nil}
                         {:page nil}
                         {:quote (quotes/get-quote)}
                         {:tagline (taglines/get-tagline)}))

(defonce intervals (atom {}))

(defn load-page [page]
  (js/clearInterval (:page @intervals))
  (let [page (pages/get-page page)]
    (swap! app-state assoc :content (:content page))
    (let [set-interval (:set-interval page)]
      (swap! intervals assoc :page
             (js/setInterval
              #(swap! app-state assoc :content ((:function set-interval)))
              (:interval set-interval))))))

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (load-page :life))

(secretary/defroute "/resume" []
  (load-page :resume))

(defn header []
  [:div.header
   [:div.title "SAIDONE.ORG"]
   [:div.tagline (:tagline @app-state)]])

(defn content [page]
  [:div.content {:id "content"} (:content @app-state)])

(defn footer []
  [:div.footer
   [:div.quote (:quote @app-state)]])

(defn toolbar []
  [:div.toolbar
   [:a {:href "#/"} "Home"]
   [:a {:href "#/resume"} "Resume"]])

(defn site []
  [:div
   [header]
   [toolbar]
   [:div.body
    [content]]
   [footer]])

(defn update-quote! []
  (swap! app-state assoc :quote (quotes/get-quote))
  (if (nil? (:tagline @intervals))
    (defonce qupte-interval (js/setInterval update-quote! (+ 16000 (rand-int 8000))))))

(defn update-tagline! []
  (swap! app-state assoc :tagline (taglines/get-tagline))
  (if (nil? (:tagline @intervals))
    (swap! intervals assoc :tagline (js/setInterval update-tagline! (+ 10000 (rand-int 5000))))))

(defn render []
  (reagent/render [site] (js/document.getElementById "app")))

(let [h (History.)]
  (events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

(update-quote!)
(update-tagline!)
