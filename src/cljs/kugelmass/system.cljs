;; Copyright (c) 2020-2026 Saidone

(ns ^:figwheel-hooks kugelmass.system
  (:require [kugelmass.core :as core]))

(defn ^:export go []
  (core/render))

(defn ^:after-load reset []
  (core/reload))
