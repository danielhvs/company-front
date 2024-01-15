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
 ::products
 (fn [db _v]
   (:products db)))

(rf/reg-sub
 ::shapes
 (fn [db _v]
   (:shapes db)))

(rf/reg-sub
 ::selected-shape
 :<- [::shapes]
 (fn [shapes _v]
   (js/console.log "WUT" shapes)
   (some->> shapes
            (filter :checked)
            first)))


