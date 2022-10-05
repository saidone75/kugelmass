;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.pages.cm.cm
  (:require [reagent.core :as r]))

(def page (r/atom {}))

(defn display-file-content [content]
  [:div (-> content .-target .-result)])

(defn put-upload [e]
  (let [reader (js/FileReader.)
        file (-> e .-target .-files (aget 0))]
    (set! (.-onload reader) #(swap! page assoc :content (display-file-content %)))
    (.readAsText reader file)))

(defn upload-button []
  [:div
   [:input
    {:type "file" :accept ".xml" :on-change put-upload}]])

(swap! page assoc :content (upload-button))

(defn content []
  page)
