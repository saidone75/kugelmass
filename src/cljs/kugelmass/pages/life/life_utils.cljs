(ns kugelmass.pages.life.life-utils)

;; random boolean 75% false
(defn- random-bool []
  (if (> (rand-int 4) 2)
    true
    false))

;; init board
(defn init-game [w h]
  (vec (doall (take (* w h) (repeatedly random-bool)))))

;; calculate vector index from coords
(defn- compute-index [x y w]
  (+ x (* y w)))

;; calculate grid coords from vector index
(defn- compute-coords [n w]
  {:x (mod n w) :y (quot n w)})

;; check if a cell is alive
(defn- is-alive [n board]
  (true? (nth (:board board) n)))

;; get neighbours of a cell
(defn- neighbours [n w h]
  (let [coords (compute-coords n w)
        ;; inc x and y with wrapping logic
        incx #(cond
                (= %1 (dec w)) 0
                :else (inc %1))
        decx #(cond
                (= %1 0) (dec w)
                :else (dec %1))
        incy #(cond
                (= %1 (dec h)) 0
                :else (inc %1))
        decy #(cond
                (= %1 0) (dec h)
                :else (dec %1))]
    [(compute-index (decx (:x coords)) (decy (:y coords)) w)
     (compute-index (:x coords) (decy (:y coords)) w)
     (compute-index (incx (:x coords)) (decy (:y coords)) w)
     (compute-index (decx (:x coords)) (:y coords) w)
     (compute-index (incx (:x coords)) (:y coords) w)
     (compute-index (decx (:x coords)) (incy (:y coords)) w)
     (compute-index (:x coords) (incy (:y coords)) w)
     (compute-index (incx (:x coords)) (incy (:y coords)) w)]))

(def memo-neighbours (memoize neighbours))

;; get alive neighbours count
(defn- count-alive-neighbours [n board]
  (let [neighbours (memo-neighbours n (:w board) (:h board))]
    (reduce
     #(if (is-alive %2 board)
        (inc %1)
        %1)
     0
     neighbours)))

;; compute next generation of board
(defn compute-next-gen [board]
  (reduce
   #(let [alive-neighbours (count-alive-neighbours (count %1) board)]
      (if %2
        (cond
          (< alive-neighbours 2) (conj %1 false)
          (and (> alive-neighbours 1) (< alive-neighbours 4)) (conj %1 true)
          (> alive-neighbours 3) (conj %1 false))
        (if (= alive-neighbours 3)
          (conj %1 true)
          (conj %1 false))))
   []
   (:board board)))
