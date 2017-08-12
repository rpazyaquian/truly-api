(ns truly-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(def db (atom {}))

(defn add-number [db entry] (swap! db update-in [:numbers] entry))

(defn find-number [db number])

(defroutes app-routes
  ; (GET "/query" [number] (get-number db number))
  ; (POST "/number" [data] (add-number db data
  (route/not-found "Hello World"))

(def app
  (wrap-defaults app-routes api-defaults))
