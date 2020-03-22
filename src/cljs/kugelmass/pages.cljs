(ns kugelmass.pages
  (:require [kugelmass.pages.life :as life]
            [kugelmass.pages.resume :as resume]))

(defn- get-life []
  {:content
   (life/render-board)
   :set-interval {:function life/update-board
                  :interval 1000}})

(defn- get-resume []
  {:content resume/resume})

(defn get-page [page]
  (cond
    (= :life page) (get-life)
    (= :resume page) (get-resume)))
