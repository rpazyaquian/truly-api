(ns truly-api.core
  (:gen-class)
  (:require [truly-api.handler :refer [app db load-csv]]
            [ring.adapter.jetty :as jetty]
            [clojure.tools.cli :refer [parse-opts]]))

;; shamelessly stolen from the docs for clojure.tools.cli
(def cli-options
  [["-p" "--port PORT" "Port"
    :default 3000
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000)]]
   ["-f" "--file FILENAME" "Filename for CSV data to load"
    :default "data.csv"]])

(defn -main
  [& args]
  (let [port (get-in (parse-opts args cli-options) [:options :port])
        filename (get-in (parse-opts args cli-options) [:options :file])]
    (load-csv db filename)
    (jetty/run-jetty app {:port port})))
