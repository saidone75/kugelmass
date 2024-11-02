;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.routes
  (:require [clojure.java.io :as io]
            [cheshire.core :as json]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response redirect]]
            [kugelmass.birdnetpi :as birdnetpi]
            [kugelmass.random :as random]))

(defn home-routes [endpoint]
  (routes
   (resources "/")
   (GET "/.well-known/acme-challenge/:key" [key]
        (redirect (str "http://acme.saidone.org/.well-known/acme-challenge/" key)))
   (GET "/birdnetpi/error/:code" [code]
        (-> (birdnetpi/error code)
            response
            (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))
   (GET "/random/password" [format]
     (if (= format (name :json))
       (-> (random/password format)
           json/generate-string
           response
           (assoc :headers {"Content-Type" "application/json"}))
       (-> (random/password nil)
           response
           (assoc :headers {"Content-Type" "text/html; charset=utf-8"}))))
   (ANY "*" _
        (-> "public/index.html"
            io/resource
            io/input-stream
            response
            (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))))
