(ns company.server
    (:require [company.handler :refer [app]]
              [config.core :refer [env]]
              [ring.adapter.jetty :refer [run-jetty]])
    (:gen-class))

(defonce server (atom nil))

(defn start! [port]
  (reset! server (run-jetty app {:port port :join? false})))

(defn stop!
  []
  (when @server
    (.stop @server)))

(defn -main
  []
  (let [port (or (env :port) 3448)]
    (start! port)))

