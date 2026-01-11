;; Copyright (c) 2020-2026 Saidone

(ns kugelmass.birdnetpi
  (:require [hiccup.page :as h]))

(def gifs ["https://media3.giphy.com/media/qVVVfmHDMBZug/giphy.gif?cid=ecf05e47mzt7om6n4k0oy1e8as0g20f4vpy42i88s0mv8yxf"
           "https://media0.giphy.com/media/7WQVxMQIOLmz6/giphy.gif?cid=ecf05e47utdpi6tm6768s5yuw1ge8q6up5qmjsb50m0bthnj"
           "https://media4.giphy.com/media/3o6fJcFmSgjUGqhVAc/giphy.gif?cid=ecf05e47pt4nytg8r809a2akml64cqftslizs6bng4bdfhrd"])

(defn error [error-code]
  (str (h/html4
        [:head (h/include-css "../../css/birdnet.css")]
        [:body
         [:div.content
          [:div.header "Service Unavailable"]
          [:div.gif [:img {:src (rand-nth gifs)}]]
          [:div.footer "The server is temporarily unable to service your request due to maintenance downtime or capacity problems. Please try again later."]]])))
