(ns company.server
  (:require
   [company.handler :refer [app]]
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

(def shapes
  [{:label "triangle" :data :triangle}
   {:label "square" :data :square}
   {:label "circle" :data :circle}
   {:label "all" :data :all :checked true}])

(let [all (map #(select-keys % [:label :data]) shapes)
      selected (first (filter #(= % (select-keys {:label "circle" :data :circle} [:label :data])) all))
      removed (remove #(= selected %) all)]
  selected
  (conj removed
        (assoc selected :checked true)))

#{{:label "all", :data :all}
  {:label "circle", :data :circle}
  {:label "square", :data :square, :checked true}
  {:label "triangle", :data :triangle}}
