(ns company.handler
  (:require
   [clojure.java.io :as io]
   [muuntaja.core :as m]
   [reitit.ring :as ring]
   [reitit.ring.middleware.muuntaja :as muuntaja]))

(defn index
  []
  (slurp (io/resource "public/index.html")))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["assets/*" (ring/create-resource-handler {:root "public/assets"})]
     ["" {:handler (fn [_req] {:body (index) :status 200})}]]
    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))


