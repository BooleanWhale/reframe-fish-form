(ns web-form.events
  (:require
   [re-frame.core :as re-frame]
   [web-form.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::update-form ;; fn (function) passes in vector
 (fn [db [_ id val]] ;; function [database [_destructure id-of-element value-of-element ]]
   (assoc-in db [:form id] val))) ;; associates in db, passes in form with id of element and it's value
;; assoc-in is a map - passes in two keys and value => key1: [key2: val]

(re-frame/reg-event-db
 ::save-form
 (fn [db]
   (let [form-data (:form db) ;; let form binding (form from db)
         fishies (get db :fishies []) ;; gets the current fishies list data (provides empty default value)
         updated-fishies (conj fishies form-data)] ;; const updated-fishies = [...fishies, form-data]
   (-> db ;; threads the database
       (assoc :fishies updated-fishies) ;; assocites :fishies with updated-fishies
       (dissoc :form))))) ;; disassocites form (clears) 