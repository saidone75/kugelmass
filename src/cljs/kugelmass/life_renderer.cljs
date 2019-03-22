(ns kugelmass.life-renderer
  (:require [kugelmass.life :as life]))

(def window-width  (.-innerWidth js/window))
(def blocksize 20)

(def uniqkey (atom 0))
(defn gen-key []
  (swap! uniqkey inc)
  @uniqkey)

(defn block [x y color]
  [:rect.block {:x x
                :y y
                :fill color}])

(defn draw-board [width height blocksize]
  [:div.board
   [:svg.board {:width width :height height}
    (loop [board @life/board blocks nil x 0 y 0 i 0]
      (if (empty? board) blocks
          (recur (rest board)
                 (conj blocks ^{:key (gen-key)} [block x y
                                                 (if (first board)
                                                   "#7b971b"
                                                   "#f0f0d0")])
                 (if (= 0 (mod (inc i) life/width))
                   0
                   (+ blocksize x))
                 (if (= 0 (mod (inc i) life/width))
                   (+ blocksize y)
                   y)
                 (inc i))))]])

(defn render []
  (def x (quot window-width blocksize))
  (life/init-game x (quot x 3))
  (draw-board (* blocksize x) (* blocksize (quot x 3)) blocksize))

(defn update []
  (life/compute-next-gen)
  (draw-board (* blocksize x) (* blocksize (quot x 3)) blocksize))
