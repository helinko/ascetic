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

(defonce username (r/atom ""))

(defonce counter (r/atom 0))

(defn add-message [text]
  (let [id (swap! counter inc)]
    (swap! messages assoc id {:id id 
                              :username @username 
                              :message text})))

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

;; So the username is updated to the global atom on-change.
;; Add-message then lifts it from there to each message.
(defn username-field []
  [:input 
   {:type "text"
    :value @username
    :id "username-input"
    :placeholder "Give some username"
    :on-change #(reset! 
                 username 
                 (-> % .-target .-value))}])

; TODO: Add user
(defn message-area []
  [:div {:class "message-area"}
   (for [m (vals @messages)]
     [:div {:class "message" :key (str (:id m))} 
      @username ": " (:id m) " " (:message m)]
     )])

(defn ascetic-app []
  [:div
   [:header#header
    [:h1 "ascetic"]]
   [:section#main 
    [message-area]
    [username-field]
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
