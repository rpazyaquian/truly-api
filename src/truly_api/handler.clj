(ns truly-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(def db (atom {}))

; data structure looks like this:

; {:numbers {"+11111111111" {"context whee" "Rebecca"
;                            "Other Context Whee" "whee"}}}

(defn add-number [db entry]
  ; (println entry)
  (swap! db update-in [:numbers] entry))

(defn get-number [db number])

(defn only-digits [number]
  (clojure.string/replace number #"\D" ""))

(defn add-1-if-less-than-11 [number]
  (if (< (count number) 11)
    (str "1" number)
    number))

(defn add-plus [number]
  (str "+" number))

(defn do-format-number [number]
  (-> (only-digits number)
      (add-1-if-less-than-11)
      (add-plus)))

(defn format-number [[number context name]]
  [(do-format-number number) context name])

(defn load-csv [db filename]
  (with-open [reader (io/reader filename)]
    (doall
      (->> (csv/read-csv reader)
           (map #(format-number %1))
           (map #(add-number db %1))))))

(load-csv db "resources/interview-callerid-data.csv")

(defroutes app-routes
  (GET "/query" [number] (get-number db number))
  (POST "/number" [data] (add-number db data))
  (route/not-found "Hello World"))

(def app
  (wrap-defaults app-routes api-defaults))
