;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.pages.cm.cm)

(defn submit-image-form
  "a form version, using a default form (no ajax)"
  []
  [:div "Submit with an html form"
   [:form {:action "/cm"
           :enc-type "multipart/form-data"
           :method "post"}
    [:input {:type "file"
             :name "cm"}]
    [:button {:type "submit"}
     "Submit image form"]]])

(def cm
  [:div.page
   (submit-image-form)
   ])

(defn content []
  (atom {:content cm}))
