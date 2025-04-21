;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.taglines)

(def taglines ["DON'T PANIC"
               "call me maybe"
               "the enemy knows the system"
               "Jeder für sich und Gott gegen alle"
               "the only winning move is not to play"
               "col Squarzia e Curzio nel sacro palazo"
               "now she knows why her cat stays away from me! DAMNIT!"
               "Now they know how many holes it takes to fill the Albert Hall"])

(defn get-tagline []
  (rand-nth taglines))

(defn get-taglines []
  (run! js/console.log taglines))
