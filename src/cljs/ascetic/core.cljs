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

(defonce init (do
                (add-message "This is mediocre at best")
                (add-message "Talk about under-whelming")
                ))


(defn msg-input []
  (let [msg (r/atom "")
        stop #(reset! msg "")
        save #(let [v (-> @msg str clojure.string/trim)]
                (if-not (empty? v) (add-message v))
                (stop))]
    (fn [] 
      [:input 
       {:type "text" 
        :value @msg 
        :id "msg-input" 
        :placeholder "Write something"
        :class "msg-input-cl"
        :on-change #(reset! msg (-> % .-target .-value))
        :on-key-down #(case (.-which %)
                        13 (save)
                        27 (stop)
                        nil)
        }])))



; TODO: Add user
(defn message-area []
  [:div {:class "message-area"}
   (for [m (vals @messages)]
     [:div {:class "message" :key (str (:id m))} 
      "Derp: " (:id m) " " (:message m)]
     )])

(defn ascetic-app []
  [:div
   [:header#header
    [:h1 "ascetic"]]
   [:section#main 
    [message-area]
    [msg-input]]])

 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App


(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (r/render [ascetic-app]
            (.getElementById js/document "app")))


(defn ^:export run []
  (dev-setup)
  (reload))
