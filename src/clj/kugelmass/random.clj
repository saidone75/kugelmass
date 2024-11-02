;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.random
  (:import [java.security SecureRandom])
  (:require [clojure.string :as str]))


(def alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!(),-.:;?[]{}()")

(defn- entropy [password]
  (let [e (* (count password) (/ (Math/log (count alphabet)) (Math/log 2)))]
    (/ (Math/round (* e 10.0)) 10.0)))

(defn- password-json [password]
  {:password password
   :entropy (entropy password)})

(defn password [format]
  (let [password (apply str (repeatedly (+ 24 (rand-int 9)) #(get alphabet (mod (.nextInt (SecureRandom.)) (count alphabet)))))]
    (if (= :json (keyword format))
      (password-json password)
      password)))
