(ns web-form.views
  (:require
   [re-frame.core :as re-frame]
   [web-form.events :as events]
   [web-form.subs :as subs]))

(def fishie-type ["Ocellaris" "Percula" "Hybrid"]) ;; array to be used in select options

(defn fishies-list []
  (let [fishies @(re-frame/subscribe [::subs/fishies])]
    [:div
     [:h2 "Fishies!"]
     [:ul 
      (map (fn [{:keys [fishie-name fishie-type]}]
             [:li {:key fishie-name} (str fishie-name " (" fishie-type ")")]) fishies)
      ]]))

(defn text-input [id label] ;; defines element
  (let [value (re-frame/subscribe [::subs/forms id])] ;; Creates a two-way binding
    [:div.field ;; defines element and adds class
     [:label.label label] ;; sets textContent
     [:div.control
      [:input.input {:value @value ;; associate value with the subscription value (@ dereferences the value)
                     :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)]) ;; dispatches event function - # makes is an anonymous function
                     :type "text" :placeholder "Text input"}]]]))                                       ;; .- indicates a property (just a . is treated as a function)
                                                                                                        ;; onChange (event) event.target.value;

(defn select-input [id label options]
  (let [value (re-frame/subscribe [::subs/forms id])]
  [:div.field 
   [:label.label label
    [:div.control
     [:div.select
      [:select {:value @value
       :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}
       [:option {:value ""} "Select fishie"]
       ;; [:option {:value "Option 1"} "Option 1"]
       ;; [:option {:value "Option 2"} "Option 2"]
       (map (fn [o] [:option {:key o :value o} o]) options) ;; maps option variable into option tags
                                                            ;; o is unique key name
       ]]]]]))

(defn api-data-list [] ;; Probably won't work, haha
  (let [api-data @(re-frame/subscribe [::subs/api-data])]
    [:div
     [:h2 "API fishie info"]
     [:ul
      (for [fishie api-data]
        ^{:key (:fishie-name fishie)}
        [:li (str (:fishie-name fishie) " (" (:fishie-type fishie) ")")])]]))

(defn main-panel []
  (let [is-valid? @(re-frame/subscribe [::subs/form-is-valid? [:fishie-name :fishie-type]])] ;; set is-valid to if inputs have data
    [:div.section 
     [fishies-list]
     [:hr]
     [api-data-list]
     [:hr]
     [text-input :fishie-name "Fishie name"] ;; Passes in #id and label
     [select-input :fishie-type "Fishie type" fishie-type]
     [:button.button.is-primary {:disabled (not is-valid?) ;; disbles button if is-valid === false
                                 :on-click #(re-frame/dispatch [::events/save-form])} "save"]
     ]))
