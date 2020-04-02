(ns kugelmass.quotes)

(def quotes '("Frank Zappa"
              "Politics is the entertainment branch of industry."
              "Frank Zappa"
              "There are three things that smell of fish. One of them is fish. The other two are growing on you!"
              "Frank Zappa"
              "There is more stupidity than hydrogen in the universe, and it has a longer shelf life."
              "Frank Zappa"
              "Tobacco is my favorite vegetable."
              "Steve Jobs"
              "It's more fun to be a pirate than to join the Navy."
              "Robert Sewell"
              "If Java had true garbage collection, most programs would delete themselves upon execution."
              "Linus Torvalds"
              "The memory management on the PowerPC can be used to frighten small children."
              "Bjarne Stroustrup"
              "In C++ it's harder to shoot yourself in the foot, but when you do, you blow off your whole leg."
              "Bjarne Stroustrup"
              "There are only two kinds of programming languages: those people always bitch about and those nobody uses."
              "Leonard Brandwein"
              "Beware of programmers who carry screwdrivers."
              "Bernie Cosell"
              "Too many people, too few sheep."
              "Bob Gray"
              "Writing in C or C++ is like running a chain saw with all the safety guards removed."
              "Jeff Sickel"
              "Deleted code is debugged code."
              "Timothy Leary"
              "You're only as young as the last time you changed your mind."
              "Groucho Marx"
              "Outside of a dog, a book is a man's best friend. Inside of a dog it's too dark to read."
              "Groucho Marx"
              "I find television very educating. Every time somebody turns on the set, I go into the other room and read a book"
              "Pablo Picasso"
              "Computers are useless. They can only give you answers."
              "Ken Thompson"
              "One of my most productive days was throwing away 1,000 lines of code."
              "Brian Kernighan"
              "Controlling complexity is the essence of computer programming."
              "Richard Hamming"
              "Typing is no substitute for thinking."
              "L Peter Deutsch"
              "To iterate is human, to recurse divine."
              "Peter Clemenza"
              "Leave the gun, take the cannoli."))

(defn- make-quotes [quotes]
  (reduce
   #(conj %1 {:author (first %2) :quote (last %2)})
   []
   (partition 2 quotes)))

(defn get-quote []
  (let [quote (rand-nth (make-quotes quotes))]
    (str \" (:quote quote) \"
         (if (nil? (:author quote))
           nil
           (str " - " (:author quote))))))
