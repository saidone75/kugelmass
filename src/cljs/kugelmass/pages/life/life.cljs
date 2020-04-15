(ns kugelmass.pages.life.life
  (:require [kugelmass.pages.life.life-utils :as life-utils]))

(defonce window-width  (.-innerWidth js/window))
(defonce window-height  (.-innerHeight js/window))

(defonce blocksize 20)

(defonce board (atom {}))

(swap! board assoc :w (quot (* .80 window-width) blocksize))
(swap! board assoc :h (quot (* .70 window-height) blocksize))

(def uniqkey (atom 0))
(defn- gen-key []
  (swap! uniqkey inc)
  @uniqkey)

(defn- block [x y color]
  [:rect.block {:x x
                :y y
                :fill color}])

(defn- draw-board [width height]
  (let [w (:w @board)]
    [:div.board
     [:svg.board {:width width :height height}
      (loop [board (:board @board) blocks nil x 0 y 0 i 0]
        (if (empty? board) blocks
            (recur (rest board)
                   (conj blocks ^{:key (gen-key)} [block x y
                                                   (if (first board)
                                                     "#566a12"
                                                     "#f0f0d0")])
                   (if (= 0 (mod (inc i) w))
                     0
                     (+ blocksize x))
                   (if (= 0 (mod (inc i) w))
                     (+ blocksize y)
                     y)
                   (inc i))))]]))

(defn create-board []
  (let [{w :w h :h} @board]
    (swap! board assoc :board (life-utils/init-game w h))
    (draw-board (* blocksize w) (* blocksize h))))

(defn update-board []
  (let [{w :w h :h} @board]
    (swap! board assoc :board (life-utils/compute-next-gen @board))
    (draw-board (* blocksize w) (* blocksize h))))
