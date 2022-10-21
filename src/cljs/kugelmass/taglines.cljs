;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.taglines)

(def taglines ["DON'T PANIC"
               "call me maybe"
               "Shaken, not stirred"
               "not a flotation device"
               "Little. Green. Better."
               "the enemy knows the system"
               ;;"tickling the dragon's tail"
               "Better built. Better hacked."
               "go play leapfrog with a unicorn"
               ;;"a likely place - and probably true"
               "the only winning move is not to play"
               "now she knows why her cat stays away from me! DAMNIT!"
               "Now they know how many holes it takes to fill the Albert Hall"])

(defn get-tagline []
  (rand-nth taglines))
