(ns truly-api.core
  (:gen-class)
  (:require [truly-api.handler :refer [app]]
            [ring.adapter.jetty :as jetty]
            [clojure.tools.cli :refer [parse-opts]]))

;; shamelessly stolen from the docs for clojure.tools.cli
(def cli-options
  [["-p" "--port PORT" "Port"
    :default 3000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000)]]])

(defn -main
  [& args]
  (let [port (get-in (parse-opts args cli-options) [:options :port])]
    (jetty/run-jetty app {:port port})))
