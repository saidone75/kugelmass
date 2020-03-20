(ns kugelmass.taglines)

(def taglines ["size DOES matter"
               "need we say more?"
               "fit to feel groovy"
               "divide and overflow"
               "shaken, not stirred"
               "your way, right away"
               "good to the last drop!"
               "not a flotation device"
               "Little. Green. Better."
               ;; "nice shoes, wanna fuck?"
               ;; "ribbed for her pleasure!"
               "it's like warm apple pie!"
               "there is always one more bug"
               ;; "so many cats, so few recipes"
               "go play leapfrog with a unicorn"
               "use only under adult supervision"
               "often imitated, never duplicated!"
               "the right choice for a healthy pet"
               "abandon hope, all ye who enter here"
               "about as cool as a fart in a spacesuit"
               ;; "now doesn't that make you feel better?"
               "the land of the free and the home of the brave"
               "just when you thought it couldn't get any better"
               "now she knows why her cat stays away from me! DAMNIT!"])

(defn get-tagline []
  (rand-nth taglines))
