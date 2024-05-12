;; Copyright (c) 2020-2024 Saidone

(ns kugelmass.pages.cm.cm-clj
  (:require [reagent.core :as r]
            [clojure.data.xml :as xml]
            [clojure.string :as s]
            [goog.string :as gs]
            [goog.string.format]))

(defonce page (r/atom {}))
(defonce state (atom {}))
(defonce src (atom '()))

(defonce header ";; generated with https://saidone.org/#/cm-clj")
(defonce const "^:const")

(swap! state assoc :def "def")
(swap! state assoc :keywordize)
(swap! state assoc :constant)

(swap! state assoc :type-prefix "type-")
(swap! state assoc :asp-prefix "asp-")
(swap! state assoc :assoc-prefix "assoc-")
(swap! state assoc :prop-prefix "prop-")
(swap! state assoc :localname-suffix "-localname")
(swap! state assoc :qname-suffix "-qname")
(swap! state assoc :uri-suffix "-uri")
(swap! state assoc :prefix-suffix "-prefix")
(swap! state assoc :camelcase-separator "-")

(defn kebab-case
  "Turn a camelCase string into kebab-case."
  [s]
  (->> s
       (#(s/split % #"(?<=[a-z])(?=[A-Z])"))
       (map #(s/lower-case %))
       (s/join "-")))

(defn- fix-name [name]
  (let [name (s/replace name #".*:" "")]
    (s/lower-case (s/replace name #"([a-z])([A-Z])" (str "$1" (:camelcase-separator @state) "$2")))))

(defn- create-qname [property-name prefix]
  (gs/format
   (if (:keywordize @state)
     "(keyword (format \"%%s:%%s\" %s %s))"
     "(format \"%%s:%%s\" %s %s)")
   (str (fix-name (s/replace property-name #":.*$" "")) (:prefix-suffix @state))
   (gs/format "%s%s%s" (prefix @state) (fix-name property-name) (:localname-suffix @state))))

(defn- get-def []
  (if (:constant @state)
    (gs/format "%s %s" (:def @state) const)
    (:def @state)))

(defn- get-ns-def [namespace]
  (list
   (gs/format "(%s %s%s \"%s\")" (get-def) (s/lower-case (:prefix (:attrs namespace))) (:uri-suffix @state) (:uri (:attrs namespace)))
   (gs/format "(%s %s%s \"%s\")" (get-def) (s/lower-case (:prefix (:attrs namespace))) (:prefix-suffix @state) (:prefix (:attrs namespace)))))

(defn- get-entity-def [entity prefix]
  (if-not (nil? (:attrs entity))
    (list
     (gs/format "(%s %s%s%s \"%s\")" (get-def) (prefix @state) (fix-name (:name (:attrs entity))) (:localname-suffix @state) (kebab-case(s/replace (:name (:attrs entity)) #"^.*:" "")))
     (gs/format "(%s %s%s%s %s)" (get-def) (prefix @state) (fix-name (:name (:attrs entity))) (:qname-suffix @state) (create-qname (:name (:attrs entity)) prefix)))))

(defn- get-entities [xml-data type]
  (filter #(not (string? %)) (mapcat :content (filter #(= (name type) (last (s/split (:tag %) #"/"))) (:content xml-data)))))

(defn- gen-src [xml-data]
  (reset! src
          (conj
           (map str (concat (mapcat #(get-ns-def %) (get-entities xml-data :namespaces))
                            (mapcat #(get-entity-def % :type-prefix) (get-entities xml-data :types))
                            (mapcat #(get-entity-def % :asp-prefix) (get-entities xml-data :aspects))
                            (mapcat #(get-entity-def % :assoc-prefix) (mapcat #(get-entities % :associations) (concat (get-entities xml-data :aspects) (get-entities xml-data :types))))
                            (mapcat #(get-entity-def % :prop-prefix) (mapcat #(get-entities % :properties) (concat (get-entities xml-data :aspects) (get-entities xml-data :types))))))
           nil
           header)))

(defn- input [key]
  [:input {:class "cm-button"
           :type "text"
           :value (key @state)
           :on-change #(swap! state assoc
                              key (-> % .-target .-value)
                              :msg "Source regenerated")}])

(defn- checkbox [key]
  [:input {:class "cm-button"
           :type "checkbox"
           :value (key @state)
           :on-change #(swap! state assoc
                              key (-> % .-target .-checked)
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
              [:td {:class "cm-prop-table-label"} "camelCase separator:"] [:td (input :camelcase-separator)]
              ]
             [:tr
              [:td {:class "cm-prop-table-label"} "Aspects prefix:"] [:td (input :asp-prefix)]
              [:td {:class "cm-prop-table-label"} "Qname suffix:"] [:td (input :qname-suffix)]
              [:td]]
             [:tr
              [:td {:class "cm-prop-table-label"} "Associations prefix:"] [:td (input :assoc-prefix)]
              [:td {:class "cm-prop-table-label"} "Prefix suffix:"] [:td (input :prefix-suffix)]
              [:td]]
             [:tr
              [:td {:class "cm-prop-table-label"} "Constant:"] [:td (checkbox :constant)]
              [:td {:class "cm-prop-table-label"} "Keywordize:"] [:td (checkbox :keywordize)]
              [:td]]]]]
          [:div {:class "cm-message"} (:msg @state)]
          [:div {:class "cm-src" :on-click copy-to-clipboard} [:code {:id "src" :class "cm-src"} (map #(str % "\n") @src)]]]))

(add-watch state nil redraw)

(swap! page assoc :content
       (upload-button))

(defn content []
  page)
