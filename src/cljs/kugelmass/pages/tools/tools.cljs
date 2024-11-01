;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.pages.tools.tools)

(def tools
  [:div.page
   [:h1 "Alfresco"]
   [:li [:a {:href "#/cm"} "CM - Generate Java sources for Alfresco Content Model XML"]]
   [:li [:a {:href "#/cm-clj"} "CM-CLJ - Generate Clojure sources for Alfresco Content Model XML"]]
   ])

(defn content []
  (atom {:content tools}))
