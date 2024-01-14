(ns company.events
  (:require
   [ajax.core :as ajax]
   [ajax.protocols :as protocol]
   [clojure.string :as str]
   [day8.re-frame.http-fx] ; this will register the :http-xhrio implementation
   [goog.dom :as dom]
   [re-frame.core :as rf]
   [company.db :as db]))

(def ^:private timeout 60000)
(def ^:private api "http://localhost:3000")

(rf/reg-event-db
 ::input-updated
 (fn [db [_ input v]]
   (assoc-in db [input :data] v)))

(rf/reg-event-db
 ::input-state-updated
 (fn [db [_ input k kk v]]
   (assoc-in db [input k kk] v)))

(rf/reg-event-db
 ::key-val
 (fn [db [_ k v]]
   (assoc db k v)))

(rf/reg-event-db
 ::initialize
 (fn [_db _]
   {:galvanizations (update-vals (group-by :data db/galvanizacoes) first)
    :something false
    :other-something true}))

(rf/reg-event-db
 ::sucess
 (fn [db [_ result]]
   (assoc db :simulation result)))

(rf/reg-event-db
 ::http-fail
 (fn [db [_ result]]
   (js/console.log {:debug "result" :data result})
   db))

(rf/reg-event-fx
 ::calculate
 (fn [{:keys [db]} _v]
   {:db db
    :http-xhrio {:method :get
                 :uri (str api "/calculate")
                 :timeout timeout
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::sucess]
                 :on-failure [::http-fail]}}))

(defn- abre-pdf [arquivo-pdf]
  (let [file (js/Blob. (clj->js [arquivo-pdf])
                       (clj->js {:type "application/pdf"}))
        url (.createObjectURL js/URL file)
        link (dom/createDom "a" #js {"href" url})]
    (dom/appendChild (.-body js/document) link)
    (set! (.-download link) "orcamento-company.pdf")
    (.click link)
    (dom/removeChildren link)))

(rf/reg-event-db
 ::sucesso-orcamento
 (fn [db [_ result]]
   (abre-pdf result)
   db))

(rf/reg-event-fx
 ::get-pdf
 (fn [{:keys [db]} v]
   {:db db
    :http-xhrio {:method :post
                 :uri (str api "/make-pdf")
                 :timeout timeout
                 :params (last v)
                 :format (ajax/json-request-format)
                 :response-format {:content-type "application/pdf"
                                   :description "pdf"
                                   :read protocol/-body
                                   :type :arraybuffer}
                 :on-success [::sucesso-orcamento]
                 :on-failure [::http-fail]}}))
