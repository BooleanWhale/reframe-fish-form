(ns web-form.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::forms
 (fn [db [_ id]]
   (get-in db [:form id] ""))) ;; empty string provinds default value (for reset)

(re-frame/reg-sub
 ::form-is-valid?
 (fn [db [_ form-ids]]
   (every? #(get-in db [:form %]) form-ids))) ;; get form-ids from db, checks if all keys have values

(re-frame/reg-sub
 ::fishies
 (fn [db]
   (get db :fishies []))) ;; gets fishies from database (fishies || [])