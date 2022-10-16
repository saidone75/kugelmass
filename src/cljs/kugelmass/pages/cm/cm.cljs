;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.pages.cm.cm
  (:require [reagent.core :as r]
            [clojure.data.xml :as xml]
            [clojure.string :as s]
            [goog.string :as gstring]
            [goog.string.format]))

(defonce page (r/atom {}))
(defonce src (atom {}))
(defonce generated-src (atom '()))
(defonce el-keys (atom 0))

(swap! src assoc :string "public static final String")
(swap! src assoc :qname "public static final QName")

(swap! src assoc :uri-suffix "_URI")
(swap! src assoc :prefix-suffix "_PREFIX")
(swap! src assoc :asp-prefix "ASP_")
(swap! src assoc :prop-prefix "PROP_")

(defn- get-entities [xml-data type]
  (filter #(not (string? %)) (mapcat :content (filter #(= (name type) (last (s/split (:tag %) #"/"))) (:content xml-data)))))

(defn- create-qname [property-name prefix]
  (gstring/format
   "QName.createQName(%s, %s)"
   (str (s/upper-case (s/replace property-name #":.*$" "")) (:uri-suffix @src))
   (gstring/format "%s%s_LOCALNAME" (prefix @src) (s/upper-case (s/replace property-name #"^.*:" "")))))

(defn- get-ns-def [namespace]
  (list
   (gstring/format "%s %s%s = \"%s\";" (:string @src) (s/upper-case (:prefix (:attrs namespace))) (:uri-suffix @src) (:uri (:attrs namespace)))
   (gstring/format "%s %s%s = \"%s\";" (:string @src) (s/upper-case (:prefix (:attrs namespace))) (:prefix-suffix @src) (:prefix (:attrs namespace)))))

(defn- get-entity-def [entity prefix]
  (if-not (nil? (:attrs entity))
    (list
     (gstring/format "%s %s%s_LOCALNAME = \"%s\";" (:string @src) (prefix @src) (s/upper-case (s/replace (:name (:attrs entity)) #".*:" "")) (s/replace (:name (:attrs entity)) #".*:" ""))
     (gstring/format "%s %s%s_QNAME = %s;" (:qname @src) (prefix @src) (s/upper-case (s/replace (:name (:attrs entity)) #".*:" "")) (create-qname (:name (:attrs entity)) prefix)))))

(defn- gen-src [xml-data]
  (reset! generated-src
          (map str (flatten
                    (concat
                     (map #(get-ns-def %) (get-entities xml-data :namespaces))
                     (map #(get-entity-def % :asp-prefix) (get-entities xml-data :aspects))
                     (map #(get-entity-def % :prop-prefix) (flatten (map #(get-entities % :properties) (concat (get-entities xml-data :aspects) (get-entities xml-data :types))))))))))

(defn- asp-input []
  [:input {:class "cm-button"
           :type "text"
           :value (:asp-prefix @src)
           :on-change #(swap! src assoc
                              :asp-prefix (-> % .-target .-value)
                              :msg "Source regenerated")}])

(defn- prop-input []
  [:input {:class "cm-button"
           :type "text"
           :value (:prop-prefix @src)
           :on-change #(swap! src assoc
                              :prop-prefix (-> % .-target .-value)
                              :msg "Source regenerated")}])

(defn- load-file-content [content]
  (swap! src assoc
         :xml-data (xml/parse-str (-> content .-target .-result))
         :msg "Source generated"))

(defn- process-upload [e]
  (let [reader (js/FileReader.)
        file (-> e .-target .-files (aget 0))]
    (set! (.-onload reader) #(load-file-content %))
    (.readAsText reader file)))

(defn- upload-button []
  [:input
   {:class "cm-button" :type "file" :accept ".xml" :on-change process-upload}])

(swap! page assoc :content
       (upload-button))

(defn content []
  page)

(defn- render-line [%]
  [:div {:key (swap! el-keys inc)} %])

(defn- copy-to-clipboard []
  (js/navigator.clipboard.writeText (-> (.. js/document (getElementById "generated-src")) .-innerText))
  (swap! src assoc :msg "Copied to clipboard"))

(defn- redraw []
  (gen-src (:xml-data @src))
  (swap! page assoc :content
         [:div
          [:table
           [:tbody
            [:tr
             [:td {:class "cm-button"} "Aspects prefix:"] [:td (asp-input)]]
            [:tr
             [:td {:class "cm-button"} "Properties prefix:"] [:td (prop-input)]]]]
          [:div {:class "cm-message"} (:msg @src)]
          [:div {:id "generated-src" :class "generated-src" :on-click copy-to-clipboard} (map render-line @generated-src)]]))

(add-watch src nil redraw)
