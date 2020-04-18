(ns kugelmass.pages.life.life
  (:require [kugelmass.pages.life.life-utils :as life-utils]))

(defonce window-width  (.-innerWidth js/window))
(defonce window-height  (.-innerHeight js/window))

(defonce blocksize 20)

(defonce color-true "#566a12")
(defonce color-false "#f0f0d0")

(defonce board (atom {}))

(swap! board assoc :w (quot (* .80 window-width) blocksize))
(swap! board assoc :h (quot (* .70 window-height) blocksize))

(swap! board assoc :start true)

(defn- toggle [id]
  (if (:start @board)
    (js/alert "Pause the game to edit board, either by:\n- pressing spacebar\n- tapping with two fingers\nOther commands:\n- c or swipe left to clear board and pause\n- r or swipe right to randomize board")
    (do
      (.setAttribute (.getElementById js/document id) "fill" (if (nth (:board @board) id)
                                                               color-false
                                                               color-true))
      (swap! board assoc :board (update (:board @board) id not)))))

(defn- block [id x y color]
  [:rect {:id id
          :x x
          :y y
          :fill color
          :on-click #(toggle id)
          :width "16px"
          :height "16px"
          :rx "2px"
          :stroke "lightgray"
          :stroke-width "1px"}])

(defn- draw-board [w h]
  [:div.board {:id "board"}
   [:svg.board {:width (* blocksize w) :height (* blocksize h)}
    (loop [board (:board @board) blocks '() i 0]
      (if (empty? board) blocks
          (recur (rest board)
                 (conj blocks ^{:key i} [block i
                                         (* blocksize (mod i w))
                                         (* blocksize (quot i w))
                                         (if (first board)
                                           color-true
                                           color-false)])

                 (inc i))))]])

(defn- randomize-board []
  (let [{w :w h :h} @board]
    (swap! board assoc :board (life-utils/init-game w h))
    (draw-board w h)))

(defn- clear-board []
  (let [{w :w h :h} @board]
    (swap! board assoc :start false)
    (swap! board assoc :board (vec (take (* w h) (repeat false))))
    (draw-board w h)))

(defn- keydown-handler [event]
  (if (.getElementById js/document "board")
    (cond
      (= 32 event.keyCode) (swap! board assoc :start (not (:start @board)))
      (= 82 event.keyCode) (randomize-board)
      (= 67 event.keyCode) (clear-board))))  

(defonce touchstart-pageX nil)

(defn- touchstart-handler [event]
  (if (.getElementById js/document "board")
    (cond
      (= 2 event.touches.length) (swap! board assoc :start (not (:start @board)))
      :else (set! touchstart-pageX (aget (aget event.changedTouches 0) "pageX")))))

(defn- touchend-handler [event]
  (let [touchend-pageX (aget (aget event.changedTouches 0) "pageX")
        distance (- touchstart-pageX touchend-pageX)]
    (cond
      (and (pos? distance) (< 150 (Math.abs distance))) (clear-board)
      (and (not (pos? distance)) (< 150 (Math.abs distance)))(randomize-board))))

(defn create-board []
  (if (not (:board @board))
    (randomize-board))
  (js/document.addEventListener "keydown" keydown-handler)
  (js/document.addEventListener "touchstart" touchstart-handler)
  (js/document.addEventListener "touchend" touchend-handler))

(defn update-board []
  (if (:start @board)
    (let [prev-board (:board @board)]
      (swap! board assoc :board (life-utils/compute-next-gen @board))
      (if (= prev-board (:board @board))
        (swap! board assoc @board :start false))))
  (draw-board (:w @board) (:h @board)))
