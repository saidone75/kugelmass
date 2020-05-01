(ns kugelmass.pages.life.life
  (:require [reagent.core :as r]
            [kugelmass.pages.life.life-utils :as life-utils]))

(defonce window-width  (.-innerWidth js/window))
(defonce window-height  (.-innerHeight js/window))

(defonce blocksize 20)

(defonce color-true "#566a12")
(defonce color-false "#f0f0d0")

(defonce board (atom {}))

(defonce state (r/atom {}))

(swap! board assoc :w (quot (* .80 window-width) blocksize))
(swap! board assoc :h (quot (* .70 window-height) blocksize))

(swap! state assoc :start true)

(defn- toggle-modal []
  (-> (.getElementById js/document "usage") (aget "classList") (.toggle "show-modal")))

(defn- toggle [id]
  (if (:start @state)
    (toggle-modal)
    (do
      (swap! board assoc :board (update (:board @board) id not)))))

(defn- modal []
  [:div.modal {:id "usage"
               :on-click #(toggle-modal)}
   [:div.modal-content {:class (let [ratio (/ window-width window-height)]
                                 (if (> ratio 1)
                                   "modal-content-large"
                                   "modal-content-small"))}
    [:b "USAGE"] [:br]
    "Pause the game to edit board, either by:" [:br]
    "pressing spacebar" [:br]
    "or" [:br]
    "tapping with two fingers" [:br] [:br]
    "Other commands:" [:br]
    "\"c\" or swipe left to clear board " [:b "*and*"] " pause" [:br]
    "\"r\" or swipe right to randomize board" [:br] [:br]
    "More on " [:a {:href "https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life"} "Conway's Game of Life"]]])

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

(defn- draw-board []
  (let [{w :w h :h board :board} @board]
    (swap! state assoc :content
           [:div.board {:id "board"}
            (modal)
            [:svg.board {:width (* blocksize w) :height (* blocksize h)}
             (loop [board board blocks '() i 0]
               (if (empty? board) blocks
                   (recur (rest board)
                          (conj blocks ^{:key i} [block i
                                                  (* blocksize (mod i w))
                                                  (* blocksize (quot i w))
                                                  (if (first board)
                                                    color-true
                                                    color-false)])
                          (inc i))))]])))

(defn- randomize-board []
  (let [{w :w h :h} @board]
    (swap! board assoc :board (life-utils/init-game w h))))

(defn- increase-speed []
  (if (< (:speed @state) 8)
    (do
      (js/clearInterval (:interval @state))
      (swap! state assoc :interval (js/setInterval update-board (quot 1000 (* (:speed @state) 2))) :speed (* (:speed @state) 2)))))

(defn- decrease-speed []
  (if (> (:speed @state) 1)
    (do
      (js/clearInterval (:interval @state))
      (swap! state assoc :interval (js/setInterval update-board (quot 1000 (/ (:speed @state) 2))) :speed (/ (:speed @state) 2)))))

(defn- clear-board []
  (let [{w :w h :h} @board]
    (swap! state assoc :start false)
    (swap! board assoc :board (vec (take (* w h) (repeat false))))))

(defn- keydown-handler [event]
  (if (.getElementById js/document "board")
    (cond
      (= 32 event.keyCode) (swap! state assoc :start (not (:start @state)))
      (= 82 event.keyCode) (randomize-board)
      (= 67 event.keyCode) (do (clear-board)
                               (swap! state assoc :start false))
      (= 187 event.keyCode) (increase-speed)
      (= 189 event.keyCode) (decrease-speed))))

(defonce touchstart {})
(defonce swipe-threshold (/ window-width 3))
(defonce time-threshold {:min 180 :max 1000})

(defn- touchstart-handler [event]
  (if (.getElementById js/document "board")
    (cond
      (= 2 event.touches.length) (swap! state assoc :start (not (:start @state)))
      :else (set! touchstart {:x (-> event.changedTouches (aget 0) (aget "pageX"))
                              :t (.getTime (js/Date.))}))))

(defn- touchend-handler [event]
  (let [touchend {:x (-> event.changedTouches (aget 0) (aget "pageX"))
                  :t (.getTime (js/Date.))}
        distance (- (:x touchend) (:x touchstart))
        time (- (:t touchend) (:t touchstart))]
    (if (and (> time (:min time-threshold)) (< time (:max time-threshold)))
      (cond
        (< distance (* -1 swipe-threshold)) (clear-board)
        (> distance swipe-threshold) (randomize-board)))))

(defn- update-board []
  (if (:start @state)
    (let [prev-board (:board @board)]
      (swap! board assoc :board (life-utils/compute-next-gen @board))
      (if (= prev-board (:board @board))
        (swap! state assoc :start false)))))

(defn- create-board []
  (if (not (:board @board))
    (do
      (randomize-board)
      (add-watch board :board #(draw-board))))
  (if (nil? (:keydown @state))
    (js/document.addEventListener "keydown" keydown-handler))
  (if (nil? (:touchstart @state))
    (js/document.addEventListener "touchstart" touchstart-handler))
  (if (nil? (:touchend @state))
    (js/document.addEventListener "touchend" touchend-handler))
  (if (nil? (:interval @state))
    (swap! state assoc :interval (js/setInterval update-board 1000) :speed 1))
  state)

(defn content []
  (create-board))
