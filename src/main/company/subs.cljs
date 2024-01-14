(ns company.subs
  (:require
    [re-frame.core :as rf]))

(rf/reg-sub
  ::something
  (fn [db _v]
    (:something db)))

(rf/reg-sub
  ::other-something
  (fn [db _v]
    (:other-something db)))

(rf/reg-sub
  ::simulation
  (fn [db _v]
    (:simulation db)))

(rf/reg-sub
  ::galvanizations
  (fn [db _v]
    (:galvanizations db)))

