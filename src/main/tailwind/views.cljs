(ns tailwind.views
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
   [tailwind.events :as e]
   [tailwind.subs :as s]))

(def <sub (comp deref rf/subscribe))

(defn get-pdf
  []
  (when-let [data (<sub [::s/simulation])]
    [:> Button {:variant "contained" :on-click #(rf/dispatch [::e/get-pdf data])}
     "get pdf"]))

(defn calculate
  []
  [:> Button {:variant "contained" :on-click #(rf/dispatch [::e/calculate])}
   "calculate"])

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

(defn view-galvanizations
  []
  (let [galvanizations (<sub [::s/galvanizations])]
    [:<>
     [:> FormLabel {:id "demo-radio-buttons-group-label"} "Galvanizations"]
     [:> RadioGroup {:row true :aria-labelledby "demo-radio-buttons-group-label", :defaultValue "", :name "radio-buttons-group"}
      (for [kw-galvanizacao (keys galvanizations)
            :let [{:keys [label checked data]} (kw-galvanizacao galvanizations)]]
        ^{:key kw-galvanizacao}
        [:div {:on-click (fn [_e]
                           (doseq [other (disj (set (keys galvanizations)) kw-galvanizacao)]
                             (rf/dispatch [::e/input-state-updated :galvanizations other :checked false]))
                           (rf/dispatch [::e/input-state-updated :galvanizations kw-galvanizacao :checked true]))}
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
     [view-parameters]
     [view-galvanizations]
     [:> Box {:sx {:m 1}}]
     [calculate]]]
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

