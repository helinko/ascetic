(ns ascetic.core
  (:require
   [reagent.core :as r]
   ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce debug?
  ^boolean js/goog.DEBUG)

(defonce messages
  (r/atom (sorted-map)))

(defonce user (r/atom ""))

(defonce counter (r/atom 0))

(defn add-message [text]
  (let [id (swap! counter inc)]
    (swap! messages assoc id {:id id :message text})))


;(defn save [id title] (swap! messages assoc-in [id :title] title))

(defonce init (do
                (add-message "This is mediocre at best")
                (add-message "Talk about under-whelming")
                ))

;; (defn message []
;;   (let [editing (r/atom false)]
;;     (fn [{:keys [id done title]}]
;;       [:li {:class (str (if done "completed ")
;;                         (if @editing "editing"))}
;;        [:div.view
;;         [:input.toggle {:type "checkbox" :checked done
;;                         :on-change #(toggle id)}]
;;         [:label {:on-double-click #(reset! editing true)} title]
;;         [:button.destroy {:on-click #(delete id)}]]
;;        (when @editing
;;          [todo-edit {:class "edit" :title title
;;                      :on-save #(save id %)
;;                      :on-stop #(reset! editing false)}])])))


(defn msg-input [{:keys [msg on-save on-stop]}]
  (let [val (r/atom msg)
        stop #(do (reset! val "")
                  (if on-stop (on-stop)))
        save #(let [v (-> @val str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input {:type "text" :value @val
               :id id :class class :placeholder placeholder
               
               :on-change #(reset! val (-> % .-target .-value))
               :on-key-down #(case (.-which %)
                               13 (save)
                               27 (stop)
                               nil)}])))

; This needs a user too!
(defn message-area []
  [:div {:class "message-area"}
   (for [m (vals @messages)]
     [:div {:class "message" :key (str (:id m))} 
      "Derp: " (:id m) " " (:message m)]
     )])

(defn ascetic-app [props]
  (let [filt (r/atom :all)]
    (fn []
      (let [msgs (vals @messages)]
        [:div
         [:header#header
          [:h1 "ascetic"]
          [message-area]
          [msg-input {:id "new-msg"
                       :placeholder "Write something"
                       :on-save add-message}]]
         ]))))

;(prn @counter)
 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App


;; (defn dev-setup []
;;   (when debug?
;;     (enable-console-print!)
;;     (println "dev mode")))

(defn reload []
  (r/render [ascetic-app messages]
                  (.getElementById js/document "app")))

(defn ^:export run []
;;  (dev-setup)
  (reload))
