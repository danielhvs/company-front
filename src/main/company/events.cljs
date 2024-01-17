(ns company.events
  (:require
   [ajax.core :as ajax]
   [ajax.protocols :as protocol]
   [company.db :as db]
   [day8.re-frame.http-fx] ; this will register the :http-xhrio implementation
   [goog.dom :as dom]
   [re-frame.core :as rf]))

(def ^:private timeout 60000)
(def ^:private api "http://localhost:3000")

(rf/reg-event-db
 ::input-updated
 (fn [db [_ input v]]
   (assoc-in db [input :data] v)))

(rf/reg-event-db
 ::shape-selected
 (fn [db [_ shape]]
   (let [shapes (:shapes db)
         all (mapv #(select-keys % [:label :data]) shapes)
         selected (first (filter #(= % (select-keys shape [:label :data])) all))
         removed (remove #(= selected %) all)
         new-shapes (conj removed
                          (assoc selected :checked true))]
     (assoc db :shapes (sort-by :label new-shapes))))) ;; HACK lazy me fix this simole thing

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
   {:shapes db/shapes
    :something false
    :other-something true}))

(rf/reg-event-db
 ::sucess
 (fn [db [_ result]]
   (assoc db :products result)))

(rf/reg-event-db
 ::http-fail
 (fn [db [_ result]]
   (js/console.log {:debug "result" :data result})
   db))

(rf/reg-event-fx
 ::search
 (fn [{:keys [db]} [_ v]]
   {:db db
    :http-xhrio {:method :get
                 :uri (str api "/product?shape=" (name v))
                 :timeout timeout
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::sucess]
                 :on-failure [::http-fail]}}))

(rf/reg-event-db
 ::deleted-ok
 (fn [db [_ {:keys [_id]}]]
   (assoc db :products
          (remove #(= (:_id %) _id)
                  (:products db)))))

(rf/reg-event-fx
 ::delete-product
 (fn [{:keys [db]} [_ id]]
   {:db db
    :http-xhrio {:method :delete
                 :uri (str api "/product/" id)
                 :timeout timeout
                 :response-format (ajax/json-response-format {:keywords? true})
                 :format (ajax/json-request-format)
                 :on-success [::deleted-ok]
                 :on-failure [::http-fail]}}))

(defn- update-product
  [id update-fn {:keys [_id] :as item}]
  (cond-> item
    (= _id id) (update-fn)))

(rf/reg-event-db
 ::update-product-locally
 (fn [db [_ id quantity]]
   (assoc db :products
          (map (partial update-product id #(assoc % :quantity quantity))
               (:products db)))))

(rf/reg-event-db
 ::product-updated
 (fn [db [_ {:keys [_id]}]]
   (assoc db :products
          (map (partial update-product _id #(assoc % :editing? false))
               (:products db)))))

(rf/reg-event-fx
 ::create-product
 (fn [{:keys [db]} [_ shape]]
   {:db db
    :http-xhrio {:method :post
                 :uri (str api "/product")
                 :timeout timeout
                 :params {:quantity 25
                          :price 25
                          :name (if (= :all shape) 
                                  (->> [:circle :triangle :square] shuffle first)
                                  shape)}
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::product-updated]
                 :on-failure [::http-fail]}}))

(rf/reg-event-fx
 ::update-product-backend
 (fn [{:keys [db]} [_ id]]
   {:db db
    :http-xhrio {:method :put
                 :uri (str api "/product")
                 :timeout timeout
                 :params (->> (:products db)
                              (filter #(= (:_id %) id))
                              first)
                 :format (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [::product-updated]
                 :on-failure [::http-fail]}}))

(rf/reg-event-db
 ::edit-product
 (fn [db [_ id]]
   (assoc db :products
          (map (partial update-product id #(assoc % :editing? true))
               (:products db)))))

(defn- open-pdf [arquivo-pdf]
  (let [file (js/Blob. (clj->js [arquivo-pdf])
                       (clj->js {:type "application/pdf"}))
        url (.createObjectURL js/URL file)
        link (dom/createDom "a" #js {"href" url})]
    (dom/appendChild (.-body js/document) link)
    (set! (.-download link) "company.pdf")
    (.click link)
    (dom/removeChildren link)))

(rf/reg-event-db
 ::pdf-received
 (fn [db [_ result]]
   (open-pdf result)
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
                 :on-success [::pdf-received]
                 :on-failure [::http-fail]}}))
