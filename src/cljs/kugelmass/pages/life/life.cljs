(ns kugelmass.pages.life.life
  (:require [kugelmass.pages.life.life-utils :as life-utils]))

(defonce window-width  (.-innerWidth js/window))
(defonce window-height  (.-innerHeight js/window))

(defonce blocksize 20)

(defonce board {})

(set! board (assoc board :w (quot (* .80 window-width) blocksize)))
(set! board (assoc board :h (quot (* .70 window-height) blocksize)))

(set! board (assoc board :start true))

(defn- toggle [id]
  (if (:start board)
    (js/alert "Pause the game to edit board, either by:\n- pressing spacebar\n- tapping with two fingers\nOther commands:\n- c or swipe left to clear board\n- r or swipe right to randomize board")
    (do
      (set! board (assoc board :board (update (:board board) id not)))
      (.setAttribute (.getElementById js/document id) "fill" (if (nth (:board board) id)
                                                               "#f0f0d0"
                                                               "#566a12")))))

(defn- block [id x y color]
  [:rect.block {:id id
                :x x
                :y y
                :fill color
                :on-click #(toggle id)}])

(defn- draw-board [width height]
  (let [w (:w board)]
    [:div.board {:id "board"}
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

(defn- randomize-board []
  (let [{w :w h :h} board]
    (set! board (assoc board :board (life-utils/init-game w h)))
    (draw-board (* blocksize w) (* blocksize h))))

(defn- clear-board []
  (let [{w :w h :h} board]
    (set! board (assoc board :board (vec (take (* w h) (repeat false)))))
    (draw-board (* blocksize w) (* blocksize h))))

(defn- keydown-handler [event]
  (if (.getElementById js/document "board")
    (cond
      (= 32 event.keyCode) (set! board (assoc board :start (not (:start board))))
      (= 82 event.keyCode) (randomize-board)
      (= 67 event.keyCode) (clear-board))))  

(def touchstart-pageX nil)

(defn- touchstart-handler [event]
  (if (.getElementById js/document "board")
    (cond
      (= 2 event.touches.length) (set! board (assoc board :start (not (:start board))))
      :else (set! touchstart-pageX (aget (aget event.changedTouches 0) "pageX")))))

(defn- touchend-handler [event]
  (let [touchend-pageX (aget (aget event.changedTouches 0) "pageX")
        distance (- touchstart-pageX touchend-pageX)]
    (cond
      (and (pos? distance) (< 150 (Math.abs distance))) (clear-board)
      (and (not (pos? distance)) (< 150 (Math.abs distance)))(randomize-board))))

(defn create-board []
  (randomize-board)
  (js/document.addEventListener "keydown" keydown-handler)
  (js/document.addEventListener "touchstart" touchstart-handler)
  (js/document.addEventListener "touchend" touchend-handler))

(defn update-board []
  (if (:start board)
    (set! board (assoc board :board (life-utils/compute-next-gen board))))
  (draw-board (* blocksize (:w board)) (* blocksize (:h board))))
