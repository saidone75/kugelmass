(ns kugelmass.pages
  (:require [kugelmass.pages.life.life :as life]
            [kugelmass.pages.resume.resume :as resume]))

(defn- get-life []
  {:content
   (life/create-board)
   :set-interval {:function life/update-board
                  :interval 1000}})

(defn- get-resume []
  {:content resume/resume})

(defn get-page [page]
  (cond
    (= :life page) (get-life)
    (= :resume page) (get-resume)))
