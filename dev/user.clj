(ns user
  (:require
   [clojure.tools.nrepl.server :as nrepl]
   [company.server :as server]))

(defonce nrepl-server (nrepl/start-server))
(spit "./.nrepl-port" (:port nrepl-server))

(defn restart!
  []
  (server/stop!)
  (server/start! 3448))

(comment 
  (restart!))
