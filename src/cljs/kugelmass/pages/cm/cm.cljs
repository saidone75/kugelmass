;; Copyright (c) 2020-2022 Saidone

(ns kugelmass.pages.cm.cm
  (:require [reagent.core :as r]
            [clojure.data.xml :as xml]
            [clojure.string :as s]
            [goog.string :as gstring]
            [goog.string.format]))

(defonce page (r/atom {}))
(defonce state (atom {}))
(defonce src (atom '()))
(defonce counter (atom 0))

(swap! state assoc :string "public static final String")
(swap! state assoc :qname "public static final QName")

(swap! state assoc :type-prefix "TYPE_")
(swap! state assoc :asp-prefix "ASP_")
(swap! state assoc :prop-prefix "PROP_")
(swap! state assoc :localname-suffix "_LOCALNAME")
(swap! state assoc :qname-suffix "_QNAME")
(swap! state assoc :uri-suffix "_URI")
(swap! state assoc :prefix-suffix "_PREFIX")

(defn- get-entities [xml-data type]
  (filter #(not (string? %)) (mapcat :content (filter #(= (name type) (last (s/split (:tag %) #"/"))) (:content xml-data)))))

(defn- create-qname [property-name prefix]
  (gstring/format
   "QName.createQName(%s, %s)"
   (str (s/upper-case (s/replace property-name #":.*$" "")) (:uri-suffix @state))
   (gstring/format "%s%s%s" (prefix @state) (s/upper-case (s/replace property-name #"^.*:" "")) (:localname-suffix @state))))

(defn- get-ns-def [namespace]
  (list
   (gstring/format "%s %s%s = \"%s\";" (:string @state) (s/upper-case (:prefix (:attrs namespace))) (:uri-suffix @state) (:uri (:attrs namespace)))
   (gstring/format "%s %s%s = \"%s\";" (:string @state) (s/upper-case (:prefix (:attrs namespace))) (:prefix-suffix @state) (:prefix (:attrs namespace)))))

(defn- get-entity-def [entity prefix]
  (if-not (nil? (:attrs entity))
    (list
     (gstring/format "%s %s%s%s = \"%s\";" (:string @state) (prefix @state) (s/upper-case (s/replace (:name (:attrs entity)) #".*:" "")) (:localname-suffix @state) (s/replace (:name (:attrs entity)) #".*:" ""))
     (gstring/format "%s %s%s%s = %s;" (:qname @state) (prefix @state) (s/upper-case (s/replace (:name (:attrs entity)) #".*:" "")) (:qname-suffix @state) (create-qname (:name (:attrs entity)) prefix)))))

(defn- gen-src [xml-data]
  (reset! src
          (map str (flatten
                    (concat
                     (map #(get-ns-def %) (get-entities xml-data :namespaces))
                     (map #(get-entity-def % :type-prefix) (get-entities xml-data :types))
                     (map #(get-entity-def % :asp-prefix) (get-entities xml-data :aspects))
                     (map #(get-entity-def % :prop-prefix) (flatten (map #(get-entities % :properties) (concat (get-entities xml-data :aspects) (get-entities xml-data :types))))))))))

(defn- input [key]
  [:input {:class "cm-button"
           :type "text"
           :value (key @state)
           :on-change #(swap! state assoc
                              key (-> % .-target .-value)
                              :msg "Source regenerated")}])

(defn- load-file-content [content]
  (swap! state assoc
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

(defn- copy-to-clipboard []
  (js/navigator.clipboard.writeText (-> (.. js/document (getElementById "src")) .-innerText))
  (swap! state assoc :msg "Copied to clipboard"))

(defn- redraw []
  (gen-src (:xml-data @state))
  (swap! page assoc :content
         [:div
          [:div {:class "cm-prop-table-div"}
           [:table {:class "cm-prop-table"}
            [:tbody
             [:tr
              [:td {:class "cm-prop-table-label"} "Type prefix:"] [:td (input :type-prefix)]
              [:td {:class "cm-prop-table-label"} "Localname suffix:"] [:td (input :localname-suffix)]
              [:td {:class "cm-prop-table-label"} "URI suffix:"] [:td (input :uri-suffix)]]
             [:tr
              [:td {:class "cm-prop-table-label"} "Aspects prefix:"] [:td (input :asp-prefix)]
              [:td {:class "cm-prop-table-label"} "Qname suffix:"] [:td (input :qname-suffix)]
              [:td {:class "cm-prop-table-label"} "Prefix suffix:"] [:td (input :prefix-suffix)]]
             [:tr
              [:td {:class "cm-prop-table-label"} "Properties prefix:"] [:td (input :prop-prefix)]]
             ]]]
          [:div {:class "cm-message"} (:msg @state)]
          [:div {:class "cm-src"} [:code {:id "src" :class "cm-src" :on-click copy-to-clipboard} (map #(str % "\n"))]]]))

(add-watch state nil redraw)

(swap! page assoc :content
       (upload-button))

(defn content []
  page)
