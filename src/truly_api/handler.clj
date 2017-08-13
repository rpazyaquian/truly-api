(ns truly-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.csv :as csv]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-params]]))

(def db (atom {}))

(defn update-numbers [db entry]
  (let [[number context name] entry]
    (swap! db assoc-in [number context] name)))

(defn number-context-exists? [db entry]
  (let [[number context name] entry]
    ;; NOTE: what to do if "name" is set to nil in the db?
    (if (not (= nil (get-in @db [number context])))  ;; if number-context exists already and is not nil
      true
      false)))

(defn add-number [db entry]
  (if (number-context-exists? db entry)
    {:status 403}
    (update-numbers db entry)))

(defn proper?
  "does the data have all the necessary fields, and are they populated?"
  [data]
  (let [all-expected-keys? (map #(contains? data %1) ["number" "context" "name"])]
    (every? identity all-expected-keys?)))

(defn normalize-input-data [data]
  ; expected: array of [number context name]
  [(get data "number") (get data "context") (get data "name")])

(defn api-add-number
  "utility for parsing json, ensuring proper fields, etc"
  [db data]
  (if (proper? data)
    (add-number db (normalize-input-data data))
    {:status 400}))

(defn expand-result [number [context name]]
  {:number number
   :name name
   :context context})

(defn render-results [number results]
  (let [expanded-results (map #(expand-result number %1) results)
        json-results (json/write-str {:results expanded-results})]
    json-results))

(defn get-number [db number]
  (let [results (get-in @db [number])]
    (render-results number results)))

(defn api-get-number [db number]
  (get-number db number))

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

(defroutes app-routes
  (GET "/query" [number] (api-get-number db number))
  (POST "/number" data (api-add-number db (into {} (:json-params data))))
  (route/not-found "Hello World"))

(def app
  (wrap-json-params (wrap-defaults app-routes api-defaults)))
