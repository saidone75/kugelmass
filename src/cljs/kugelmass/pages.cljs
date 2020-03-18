(ns kugelmass.pages)

(defn resume []
  [:div
   [:p
    [:h1 "Contact"]
    [:a {:href "mailto:saidone@saidone.org"} "saidone@saidone.org"]
    " ("
    [:a {:href "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x97c97241f9be57f1"} "my GPG key here"]
    ")" [:br] [:br]
    "Marco Marini" [:br]
    "Viale delle Fontanelle 6" [:br]
    "37047 San Bonifacio VR" [:br]
    "Italy"]
   [:p
    [:h1 "Summary"]
    "I'm a programmer with ~20 years of experience in software development, software engineering and system administration, mostly dealing with enterprise content management, data transformation and system integration."]
   [:p
    [:h1 "Skills"]
    "Development"
    [:ul
     [:li "Java"]
     [:li "Clojure"]
     [:li "C ANSI"]
     [:li "Assembly"]
     [:li "PHP"]
     [:li "BASH, sed and AWK"]]
    "Consultancy"
    [:ul
     [:li "ECM/CMS (Alfresco, Nuxeo)"]
     [:li "Web and application servers"]
     [:li "Distributed systems and storage"]]
    ]
   [:p
    [:h1 "Professional Experience"]
    [:h1 "Certifications"]
    [:ul
     [:li "Alfresco Certified Engineer"]]]])
