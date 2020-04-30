(ns kugelmass.pages
  (:require [kugelmass.pages.life.life :as life]
            [kugelmass.pages.resume.resume :as resume]
            [reagent.core :as r]))

(defn- get-life []
  (life/create-board))

(defn- get-resume []
  (r/atom {:content
           resume/resume}))

(defn get-page [page]
  (cond
    (= :life page) (get-life)
    (= :resume page) (get-resume)))
