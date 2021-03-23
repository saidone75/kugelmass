;; Copyright (c) 2020-2021 Saidone

(ns kugelmass.pages
  (:require [kugelmass.pages.life.life :as life]
            [kugelmass.pages.resume.resume :as resume]))

(defn get-page [page]
  (cond
    (= :life page) (life/content)
    (= :resume page) (resume/content)))
