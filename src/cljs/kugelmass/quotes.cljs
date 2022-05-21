;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.quotes)

(def quotes '("Leonard Brandwein"
              #{"Beware of programmers who carry screwdrivers."}
              "Peter Clemenza"
              #{"Leave the gun, take the cannoli."}
              "Bernie Cosell"
              #{"Too many people, too few sheep."}
              "L Peter Deutsch"
              #{"To iterate is human, to recurse divine."}
              "Edsger W. Dijkstra"
              #{"Simplicity is prerequisite for reliability."}
              "Bob Gray"
              #{"Writing in C or C++ is like running a chain saw with all the safety guards removed."}
              "Richard Hamming"
              #{"Typing is no substitute for thinking."}
              "Douglas Hofstadter"
              #{"Irrationality is the square root of all evil."
                "Hofstadter's Law: It always takes longer than you expect, even when you take into account Hofstadter's Law."}
              "Steve Jobs"
              #{"It's more fun to be a pirate than to join the Navy."}
              "Alan Kay"
              #{"A change in perspective is worth 80 IQ points."}
              "Brian Kernighan"
              #{"Controlling complexity is the essence of computer programming."}
              "Donald Knuth"
              #{"Beware of bugs in the above code; I have only proved it correct, not tried it."}
              "Timothy Leary"
              #{"You're only as young as the last time you changed your mind."}
              "Robert C. Martin"
              #{"Truth can only be found in one place: the code."}
              "Groucho Marx"
              #{"Outside of a dog, a book is a man's best friend. Inside of a dog it's too dark to read."
                "I find television very educating. Every time somebody turns on the set, I go into the other room and read a book."}
              "Marvin Minsky"
              #{"You don't understand anything until you learn it more than one way."}
              "Alan Perlis"
              #{"A language that doesn't affect the way you think about programming, is not worth knowing."
                "If you have a procedure with 10 parameters, you probably missed some."
                "LISP programmers know the value of everything and the cost of nothing."
                "Fools ignore complexity. Pragmatists suffer it. Some can avoid it. Geniuses remove it."}
              "Pablo Picasso"
              #{"Computers are useless. They can only give you answers."
                "On met très longtemps à devenir jeune."}
              "Robert Sewell"
              #{"If Java had true garbage collection, most programs would delete themselves upon execution."}
              "Jeff Sickel"
              #{"Deleted code is debugged code."}
              "Bjarne Stroustrup"
              #{"In C++ it's harder to shoot yourself in the foot, but when you do, you blow off your whole leg."
                "There are only two kinds of programming languages: those people always bitch about and those nobody uses."}
              "Ken Thompson"
              #{"One of my most productive days was throwing away 1,000 lines of code."}
              "Linus Torvalds"
              #{"The memory management on the PowerPC can be used to frighten small children."}
              "Voltaire"
              #{"Quand il s’agit d’argent, tout le monde est de la même religion."
                "Le mieux est l'ennemi du bien."
                "Où est l'amitié est la patrie."}
              "Frank Zappa"
              #{"Politics is the entertainment branch of industry."
                "There are three things that smell of fish. One of them is fish. The other two are growing on you!"
                "There is more stupidity than hydrogen in the universe, and it has a longer shelf life."
                "Tobacco is my favorite vegetable."}))

(defn- make-quotes [quotes]
  (let [quotes (partition 2 quotes)]
    (reduce
     #(concat %1 (for [quote (last %2)]
                   {:author (first %2) :quote quote}))
     '() 
     quotes)))

(def memo-make-quotes (memoize make-quotes))

(defn get-quote []
  (let [quote (rand-nth (memo-make-quotes quotes))]
    (str \" (:quote quote) \"
         (if (nil? (:author quote))
           nil
           (str " - " (:author quote))))))
