(ns kugelmass.pages.life.life
  (:require [kugelmass.pages.life.life-utils :as life-utils]))

(defonce window-width  (.-innerWidth js/window))
(defonce window-height  (.-innerHeight js/window))

(defonce blocksize 20)

(def board {})

(set! board (assoc board :w (quot (* .80 window-width) blocksize)))
(set! board (assoc board :h (quot (* .70 window-height) blocksize)))

(set! board (assoc board :start true))

(defn- toggle [id]
  (if (:start board)
    (js/alert "Pause game by pressing spacebar before edit board!")
    (do
      (.setAttribute (.getElementById js/document id) "fill" (if (nth (:board board) id)
                                                               "#f0f0d0"
                                                               "#566a12"))
      (set! board (assoc board :board (update (:board board) id not))))))

(defn- block [id x y color]
  [:rect.block {:id id
                :x x
                :y y
                :fill color
                :on-click #(toggle id)}])

(defn- draw-board [width height]
  (let [w (:w board)]
    [:div.board
     [:svg.board {:width width :height height}
      (loop [board (:board board) blocks nil x 0 y 0 i 0]
        (if (empty? board) blocks
            (recur (rest board)
                   (conj blocks ^{:key i} [block i x y
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

(defn- handler [event]
  (if (= 32 event.keyCode)
    (if (:start board)
      (set! board (assoc board :start false))
      (set! board (assoc board :start true)))))

(defn create-board []
  (let [{w :w h :h} board]
    (set! board (assoc board :board (life-utils/init-game w h)))
    (draw-board (* blocksize w) (* blocksize h)))
  (js/document.addEventListener  "keydown" handler)
  )

(defn update-board []
  (let [{w :w h :h} board]
    (if (:start board)
      (set! board (assoc board :board (life-utils/compute-next-gen board))))
    (draw-board (* blocksize w) (* blocksize h))))
