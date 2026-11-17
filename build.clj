(ns build
  (:require [clojure.tools.build.api :as b]))

(def class-dir "target/classes")
(def uber-file "target/kugelmass.jar")
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean "Remove generated build artifacts." [_]
  (doseq [path ["target" "dev-target"]]
    (b/delete {:path path})))

(defn frontend "Compile optimized ClojureScript." [_]
  (let [result (b/java-command
                {:basis (b/create-basis {:project "deps.edn" :aliases [:cljs]})
                 :java-opts ["-Xmx1g"]
                 :main 'clojure.main
                 :main-args ["-m" "cljs.main" "-co" "release.cljs.edn"
                             "-c" "kugelmass.system"]})
        {:keys [exit]} (b/process result)]
    (when-not (zero? exit)
      (throw (ex-info "ClojureScript compilation failed" {:exit exit})))))

(defn uber "Build target/kugelmass.jar, including the optimized frontend." [_]
  (b/delete {:path "target"})
  (frontend nil)
  (b/copy-dir {:src-dirs ["src/clj" "src/cljc" "resources" "target/frontend"]
               :target-dir class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile '[kugelmass.application]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'kugelmass.application})
  (println "Built" uber-file))
