(ns company.app
  (:require
   [re-frame.core :as rf]
   [reagent.dom :as dom]
   [company.events :as e]
   [company.views :as views]))

(defn app
  []
  [views/public])

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (dom/render [app]
    (.getElementById js/document "app")))

(defn init
  []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (rf/dispatch-sync [::e/initialize])
  (js/console.log "init")
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
