;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.pages.cm.cm
  (:require [reagent.core :as r]
            [clojure.data.xml :as xml]
            [clojure.string :as s]
            [goog.string :as gstring]
            [goog.string.format]))

(def page (r/atom {}))
(def src (atom {}))

(swap! src assoc :string "public static final String")
(swap! src assoc :qname "public static final QName")

(defn- get-entities [xml-data type]
  (filter #(not (string? %)) (mapcat :content (filter #(= (name type) (last (s/split (:tag %) #"/"))) (:content xml-data)))))

(defn- create-qname [property-name prefix]
  (gstring/format
   "QName.createQName(%s, %s)"
   (str (s/upper-case (s/replace property-name #":.*$" "")) "_URI")
   (gstring/format "%s_%s_LOCALNAME" prefix (s/upper-case (s/replace property-name #"^.*:" "")))))

(defn- get-ns-def [namespace]
  (list
   (gstring/format "%s %s_URI = \"%s\";" (:string @src) (s/upper-case (:prefix (:attrs namespace))) (:uri (:attrs namespace)))
   (gstring/format "%s %s_PREFIX = \"%s\";" (:string @src) (s/upper-case (:prefix (:attrs namespace))) (:prefix (:attrs namespace)))))

(defn- get-entity-def [entity prefix]
  (list
   (gstring/format "%s %s_%s_LOCALNAME = \"%s\";" (:string @src) prefix (s/upper-case (s/replace (:name (:attrs entity)) #".*:" "")) (s/replace (:name (:attrs entity)) #".*:" ""))
   (gstring/format "%s %s_%s_QNAME = %s;" (:qname @src) prefix (s/upper-case (s/replace (:name (:attrs entity)) #".*:" "")) (create-qname (:name (:attrs entity)) prefix))))

(defn display-file-content [content]
  (let [xml-data (xml/parse-str (-> content .-target .-result))]
    [:div
     (map str (flatten
               (concat
                (map #(get-ns-def %) (get-entities xml-data :namespaces))
                (map #(get-entity-def % "ASP") (get-entities xml-data :aspects))
                (map #(get-entity-def % "PROP") (flatten (map #(get-entities % :properties) (concat (get-entities xml-data :aspects) (get-entities xml-data :types))))))))]))

(defn put-upload [e]
  (let [reader (js/FileReader.)
        file (-> e .-target .-files (aget 0))]
    (set! (.-onload reader) #(swap! page assoc :content (display-file-content %)))
    (.readAsText reader file)))

(defn upload-button []
  [:div
   [:input
    {:type "file" :accept ".xml" :on-change put-upload}]])

(swap! page assoc :content (upload-button))

(defn content []
  page)
