;; Copyright (c) 2020-2023 Saidone

(ns kugelmass.components.ui
  (:require [com.stuartsierra.component :as component]
            [kugelmass.core :refer [render]]))

(defrecord UIComponent []
  component/Lifecycle
  (start [component]
    (render)
    component)
  (stop [component]
    component))

(defn new-ui-component []
  (map->UIComponent {}))
