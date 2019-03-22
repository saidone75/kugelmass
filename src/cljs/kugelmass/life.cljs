(ns kugelmass.life)

;; random boolean 75% false
(defn random-bool []
  (if (> (rand-int 4) 2)
    true
    false))

;; board status
(defonce board (atom nil))

;; init board
(defn init-game [w h]
  (def width w)
  (def height h)
  (reset! board (vec (doall (take (* width height) (repeatedly random-bool))))))

;; calculate vector index from coords
(defn compute-index [x y]
  (+ x (* y width)))

;; calculate grid coords from vector index
(defn compute-coords [n]
  {:x (mod n width) :y (quot n width)})

;; check if a cell is alive
(defn is-alive [n]
  (true? (nth @board n)))

;; get neighbours of a cell
(defn neighbours [n]
  (let [coords (compute-coords n)
        ;; inc x and y with wrapping logic
        incx #(cond
                (= %1 (dec width)) 0
                :else (inc %1))
        decx #(cond
                (= %1 0) (dec width)
                :else (dec %1))
        incy #(cond
                (= %1 (dec height)) 0
                :else (inc %1))
        decy #(cond
                (= %1 0) (dec height)
                :else (dec %1))]
    [(compute-index (decx (:x coords)) (decy (:y coords)))
     (compute-index (:x coords) (decy (:y coords)))
     (compute-index (incx (:x coords)) (decy (:y coords)))
     (compute-index (decx (:x coords)) (:y coords))
     (compute-index (incx (:x coords)) (:y coords))
     (compute-index (decx (:x coords)) (incy (:y coords)))
     (compute-index (:x coords) (incy (:y coords)))
     (compute-index (incx (:x coords)) (incy (:y coords)))]))

;; get alive neighbours count
(defn count-alive-neighbours [n]
  (let [neighbours (neighbours n)]
    (reduce
     #(if (is-alive %2)
        (inc %1)
        %1)
     0
     neighbours)))

;; compute next generation of board
(defn compute-next-gen []
  (reset! board
          (reduce
           #(let [alive-neighbours (count-alive-neighbours (count %1))]
              (if %2
                (cond
                  (< alive-neighbours 2) (conj %1 false)
                  (and (> alive-neighbours 1) (< alive-neighbours 4)) (conj %1 true)
                  (> alive-neighbours 3) (conj %1 false))
                (if (= alive-neighbours 3)
                  (conj %1 true)
                  (conj %1 false))))
           []
           @board)))
