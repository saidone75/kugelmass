;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.random
  (:import [java.security SecureRandom])
  (:require [clojure.string :as str]))


(def alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!(),-.:;?[]{}()")

(defn- random-pwd []
  (apply str (repeatedly (+ 24 (rand-int 9)) #(get alphabet (mod (.nextInt (SecureRandom.)) (count alphabet))))))

(defn password []
  (random-pwd))




