(ns kugelmass.core
  (:require [reagent.core :as reagent :refer [atom]]
            [kugelmass.taglines :as taglines]
            [kugelmass.quotes :as quotes]
            [kugelmass.life-renderer :as life-renderer]
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

(defonce intervals (atom nil))

(defn life []
  (swap! app-state assoc :content (life-renderer/render-board)))

(defn resume []
  (swap! app-state assoc :content (pages/resume)))

(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (swap! app-state assoc :page :life)
  (life))

(secretary/defroute "/resume" []
  (swap! app-state assoc :page :resume)
  (resume))

(defn header []
  [:div.header
   [:div.title "saidone.org"]
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
    (swap! intervals assoc :quote (js/setInterval update-quote! (+ 16000 (rand-int 8000))))))

(defn update-tagline! []
  (swap! app-state assoc :tagline (taglines/get-tagline))
  (if (nil? (:tagline @intervals))
    (swap! intervals assoc :tagline (js/setInterval update-tagline! (+ 10000 (rand-int 5000))))))

(defn update-life! []
  (if (= :life (:page @app-state))
    (swap! app-state assoc :content (life-renderer/update-board)))
  (if (nil? (:life @intervals))
    (swap! intervals assoc :life (js/setInterval update-life! 1000))))

(defn render []
  (reagent/render [site] (js/document.getElementById "app")))

(let [h (History.)]
  (events/listen h EventType/NAVIGATE #(secretary/dispatch! (.-token %)))
  (doto h (.setEnabled true)))

(update-quote!)
(update-tagline!)
(update-life!)
