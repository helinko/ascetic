(ns ascetic.handler
  (:require [ascetic.views :as views]
            [compojure.core :as cc]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(cc/defroutes app-routes
  (cc/GET "/"
       []
       (views/home-page))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
