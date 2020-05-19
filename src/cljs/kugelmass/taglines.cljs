(ns kugelmass.taglines)

(def taglines ["call me maybe"
               "size DOES matter"
               "need we say more?"
               "fit to feel groovy"
               "the silly-awakener"
               "divide and overflow"
               "shaken, not stirred"
               "cut with the billhook"
               "not a flotation device"
               "Little. Green. Better."
               "it's like warm apple pie!"
               "the enemy knows the system"
               "tickling the dragon's tail"
               "there is always one more bug"
               "the last place you want to go"
               "go play leapfrog with a unicorn"
               "a likely place - and probably true"
               "abandon hope, all ye who enter here"
               "the only winning move is not to play"
               "the land of the free and the home of the brave"
               "now she knows why her cat stays away from me! DAMNIT!"])

(defn get-tagline []
  (rand-nth taglines))
