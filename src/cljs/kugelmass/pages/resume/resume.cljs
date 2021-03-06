;; Copyright (c) 2020-2021 Saidone

(ns kugelmass.pages.resume.resume)

(def resume
  [:div.page
   [:h1 "Contact"]
   [:p
    [:a {:href "mailto:saidone@saidone.org"} "saidone@saidone.org"]
    " ("
    [:a {:href "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0xa1b1c1fac8a4f66b"} "my GPG key here"]
    ")" [:br] [:br]
    [:a {:href "https://github.com/saidone75"} [:img.logo {:src "/images/github.svg" :alt "GitHub"}]]
    " "
    [:a {:href "https://twitter.com/saidone"} [:img.logo {:src "/images/twitter.svg" :alt "Twitter"}]]
    " "
    [:a {:href "https://www.linkedin.com/in/saidone/"} [:img.logo {:src "/images/linkedin.svg" :alt "LinkedIn"}]] [:br] [:br]
    "Marco Marini" [:br]
    "La Spezia" [:br]
    "Italy"]
   [:h1 "Summary"]
   [:p
    "I'm a programmer with 20+ years of experience in software development, software engineering and system administration, mostly dealing with enterprise content management, data transformation and system integration."]
   [:h1 "Skills"]
   [:b "Development"]
   [:ul
    [:li "Clojure"]
    [:li "Java"]
    [:li "C ANSI"]
    [:li "Assembly - " [:em "GAS (x86), NASM (x86), ASM-One (68K), Mikro Assembler (6502), ZASM (Z80)"]]
    [:li "BASH, sed and AWK"]]
   [:b "Consultancy"]
   [:ul
    [:li "ECM/CMS - " [:em "Alfresco, Nuxeo"]]
    [:li "Application servers"]
    [:li "Distributed storage"]]
   [:h1 "Professional Experience"]
   [:ul
    [:li "Alfresco Consultant" [:br]
     "YOOX NET-A-PORTER GROUP" [:br]
     "Nov 2016 - Present" [:br]
     "London, United Kingdom"] [:br]
    [:li "Senior IT Security Consultant" [:br]
     "ISGroup" [:br]
     "Jan 2016 - Jul 2017" [:br]
     "Verona, Italy"] [:br]
    [:li "Alfresco Consultant" [:br]
     "THE NET-A-PORTER GROUP" [:br]
     "Mar 2015 - Sep 2015" [:br]
     "London, United Kingdom"] [:br]
    [:li "Alfresco Consultant" [:br]
     "Exari Systems" [:br]
     "Aug 2014 - Feb 2015" [:br]
     "London, United Kingdom"] [:br]
    [:li "Software Engineer" [:br]
     "Delta Progetti 2000" [:br]
     "Jul 2008 - Feb 2014" [:br]
     "La Spezia, Italy"] [:br]
    [:li "Senior IT Security Consultant" [:br]
     "ISGroup" [:br]
     "Mar 2007 - Aug 2014" [:br]
     "Verona, Italy"] [:br]
    [:li "Software Engineer" [:br]
     "GlaxoSmithKline" [:br]
     "Aug 2007 - Jun 2008" [:br]
     "Verona, Italy"] [:br]
    [:li "Software Engineer" [:br]
     "GlaxoSmithKline" [:br]
     "Jan 2006 - Mar 2007" [:br]
     "Verona, Italy"] [:br]
    [:li "Software Engineer" [:br]
     "Aliante" [:br]
     "Nov 2004 - Dec 2005" [:br]
     "Verona, Italy"] [:br]
    [:li "Software Engineer" [:br]
     "MeditArs" [:br]
     "Mar 2003 - Nov 2004" [:br]
     "Veronella, Italy"] [:br]
    [:li "Software Engineer" [:br]
     "Fantasma" [:br]
     "Jul 2000 - Dec 2002" [:br]
     "San Bonifacio, Italy"]]
   [:h1 "Certifications"]
   [:ul
    [:li "Alfresco Certified Engineer"]]])

(defn content []
  (atom {:content resume}))
