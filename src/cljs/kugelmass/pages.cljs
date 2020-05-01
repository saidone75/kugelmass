(ns kugelmass.pages
  (:require [reagent.core :as r]
            [kugelmass.pages.life.life :as life]
            [kugelmass.pages.resume.resume :as resume]))

(defn- get-life []
  (life/create-board))

(defn- get-resume []
  (r/atom {:content
           resume/resume}))

(defn get-page [page]
  (cond
    (= :life page) (get-life)
    (= :resume page) (get-resume)))
