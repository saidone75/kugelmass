;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.pages
  (:require [kugelmass.pages.life.life :as life]
            [kugelmass.pages.resume.resume :as resume]
            [kugelmass.pages.tools.tools :as tools]
            [kugelmass.pages.cm.cm :as cm]
            [kugelmass.pages.cm.cm-clj :as cm-clj]))

(defn get-page [page]
  (cond
    (= :life page) (life/content)
    (= :resume page) (resume/content)
    (= :tools page) (tools/content)
    (= :cm page) (cm/content)
    (= :cm-clj page) (cm-clj/content)))
