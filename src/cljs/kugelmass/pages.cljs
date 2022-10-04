;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.pages
  (:require [kugelmass.pages.life.life :as life]
            [kugelmass.pages.resume.resume :as resume]
            [kugelmass.pages.cm.cm :as cm]))

(defn get-page [page]
  (cond
    (= :life page) (life/content)
    (= :resume page) (resume/content)
    (= :cm page) (cm/content)))
