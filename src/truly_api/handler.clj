(ns truly-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/query" [] "Query for numbers as JSON goes here")
  (POST "/number" [] "POST for numbers goes here")
  (route/not-found "Hello World"))

(def app
  (wrap-defaults app-routes site-defaults))
