(ns kugelmass.routes
  (:require [clojure.java.io :as io]
            [compojure.core :refer [ANY GET PUT POST DELETE routes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [response redirect]]))

(defn home-routes [endpoint]
  (routes
   (resources "/")
   (GET "/.well-known/acme-challenge/:key" [key]
        (redirect (str "http://acme.saidone.org/.well-known/acme-challenge/" key)))
   (ANY "*" _
        (-> "public/index.html"
            io/resource
            io/input-stream
            response
            (assoc :headers {"Content-Type" "text/html; charset=utf-8"})))))
