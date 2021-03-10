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
  [(mod n w) (quot n w)])

;; get neighbours of a cell
(defn- neighbours [n w h]
  (let [[x y] (compute-coords n w)
        ;; inc x and y with wrapping logic
        incx #(if (= %1 (dec w)) 0 (inc %1))
        decx #(if (= %1 0) (dec w) (dec %1))
        incy #(if (= %1 (dec h)) 0 (inc %1))
        decy #(if (= %1 0) (dec h) (dec %1))]
    [(compute-index (decx x) (decy y) w)
     (compute-index x (decy y) w)
     (compute-index (incx x) (decy y) w)
     (compute-index (decx x) y w)
     (compute-index (incx x) y w)
     (compute-index (decx x) (incy y) w)
     (compute-index x (incy y) w)
     (compute-index (incx x) (incy y) w)]))

;; get alive neighbours count
(defn- count-alive-neighbours [n board]
  (let [{w :w h :h board :board} board]
    (reduce
     #(if (nth board %2)
        (inc %1)
        %1)
     0
     (neighbours n w h))))

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
