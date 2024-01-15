(ns company.views
  (:require
   ["@mui/material/Box" :default Box]
   ["@mui/material/Button" :default Button]
   ["@mui/material/Checkbox" :default Checkbox]
   ["@mui/material/FormControl" :default FormControl]
   ["@mui/material/FormControlLabel" :default FormControlLabel]
   ["@mui/material/FormGroup" :default FormGroup]
   ["@mui/material/FormLabel" :default FormLabel]
   ["@mui/material/Grid" :default Grid]
   ["@mui/material/Paper" :default Paper]
   ["@mui/material/Radio" :default Radio]
   ["@mui/material/RadioGroup" :default RadioGroup]
   ["@mui/material/Table" :default Table]
   ["@mui/material/TableBody" :default TableBody]
   ["@mui/material/TableCell" :default TableCell]
   ["@mui/material/TableContainer" :default TableContainer]
   ["@mui/material/TableHead" :default TableHead]
   ["@mui/material/TableRow" :default TableRow]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [company.events :as e]
   [company.subs :as s]))

(def <sub (comp deref rf/subscribe))

(defn get-pdf
  []
  (when-let [data (<sub [::s/simulation])]
    [:> Button {:variant "contained" :on-click #(rf/dispatch [::e/get-pdf data])}
     "get pdf"]))

(defn search
  []
  (let [shape (<sub [::s/selected-shape])]
    [:> Button {:variant "contained" :on-click #(rf/dispatch [::e/search (:data shape)])}
     "search"]))

(def Checkbox* (r/adapt-react-class Checkbox))
(def Radio* (r/adapt-react-class Radio))

(defn view-parameters
  []
  (let [something (<sub [::s/something])
        other-someting (<sub [::s/other-something])]
    [:<>
     [:> FormLabel {:component "legend"} "Parameters"]
     [:> FormGroup
      [:> FormControlLabel
       {:control (r/as-element [Checkbox*
                                {:checked other-someting
                                 :onChange #(rf/dispatch [::e/key-val :other-something (not other-someting)])
                                 :name "something"}])
        :label "Something"}]
      [:> FormControlLabel
       {:control (r/as-element [Checkbox*
                                {:checked something
                                 :onChange #(rf/dispatch [::e/key-val :something (not something)])
                                 :name "other something"}])
        :label "Other Something"}]]]))

(defn view-shapes
  []
  (let [shapes (<sub [::s/shapes])]
    [:<>
     [:> FormLabel {:id "demo-radio-buttons-group-label"} "shapes"]
     [:> RadioGroup {:row true :aria-labelledby "demo-radio-buttons-group-label", :defaultValue "", :name "radio-buttons-group"}
      (for [{:keys [label checked data] :as shape} shapes]
        ^{:key label}
        [:div {:on-click (fn [_e] (rf/dispatch [::e/shape-selected shape]))}
         [:> FormControlLabel {:value label, :control (r/as-element [Radio* {:checked checked :value data}])
                               :label label}]])]]))

(defn- view-simulation
  []
  (when-let [simulation (<sub [::s/simulation])]
    [:div {:class "px-1"}
     [:> TableContainer {:component Paper}
      [:> Table {:aria-label "simple table"}
       [:> TableHead
        [:> TableRow
         [:<>
          [:> TableCell {:align "left"} "Name"]
          [:> TableCell {:align "right"} "Quantity"]
          [:> TableCell {:align "right"} "Price"]]]]
       [:> TableBody
        (for [{:keys [name quantity price]} simulation]
          [:> TableRow
           [:<>
            [:> TableCell {:align "left"} name]
            [:> TableCell {:align "right"} quantity]
            [:> TableCell {:align "right"} price]]])]]]]))

(defn public
  []
  [:> Grid {:container true :spacing 2
            :height "100vh"}
   [:> Grid {:item true
             :xs 12
             :md 6
             :style {:display "flex"
                     :flexDirection "column"
                     :alignItems "center"
                     :justifyContent "center"}}
    [:> FormControl
     #_[view-parameters]
     [view-shapes]
     [:> Box {:sx {:m 1}}]
     [search]]]
   [:> Grid {:item true
             :xs 12
             :md 6
             :style {:display "flex"
                     :flexDirection "column"
                     :alignItems "center"
                     :justifyContent "center"}
             :backgroundColor "lightblue"}
    [view-simulation]
    [:> Box {:sx {:m 1}}]
    [get-pdf]]])

