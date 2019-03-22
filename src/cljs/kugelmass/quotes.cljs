(ns kugelmass.quotes)

(def quotes [{:author "Frank Zappa"
              :quote "Politics is the entertainment branch of industry."}
             {:author "Frank Zappa"
              :quote "There are three things that smell of fish. One of them is fish. The other two are growing on you!"}
             {:author "Frank Zappa"
              :quote "There is more stupidity than hydrogen in the universe, and it has a longer shelf life."}
             {:author "Frank Zappa"
              :quote "Tobacco is my favorite vegetable."}
             {:author "Steve Jobs"
              :quote "It's better to be a pirate than to join the Navy."}
             {:author "Robert Sewell"
              :quote "If Java had true garbage collection, most programs would delete themselves upon execution."}
             {:author "Linus Torvalds"
              :quote "The memory management on the PowerPC can be used to frighten small children."}
             {:author "Bjarne Stroustrup"
              :quote "In C++ it's harder to shoot yourself in the foot, but when you do, you blow off your whole leg."}
             {:author "Leonard Brandwein"
              :quote "Beware of programmers who carry screwdrivers."}
             {:author "Bernie Cosell"
              :quote "Too many people, too few sheep."}
             {:author nil
              :quote "I can hardly believe anything about last night."}
             {:author "Bob Gray"
              :quote "Writing in C or C++ is like running a chain saw with all the safety guards removed."}
             {:author "Timothy Leary"
              :quote "You're only as young as the last time you changed your mind."}
             {:author "Groucho Marx"
              :quote "Outside of a dog, a book is a man's best friend. Inside of a dog it's too dark to read."}
             {:author "Groucho Marx"
              :quote "I find television very educating. Every time somebody turns on the set, I go into the other room and read a book"}
             {:author "Pablo Picasso"
              :quote "Computers are useless. They can only give you answers."}
             {:author "Peter Clemenza"
              :quote "Leave the gun, take the cannoli."}])

(defn get-quote []
  (def quote (rand-nth quotes))
  (str \" (:quote quote) \"
       (if (nil? (:author quote))
         nil
         (str " - " (:author quote)))))
