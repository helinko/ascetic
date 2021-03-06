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


(defn create-message [text]
  (let [id (swap! counter inc)
        ; Not sure if this is idiomatic
        username (if (empty? @username) "Anon" @username)]
    (swap! messages assoc id {:id id 
                              :username username 
                              :message text})))

(defonce init (do
                (create-message "This is mediocre at best")
                (create-message "Talk about under-whelming")
                ))

(defn msg-input []
  (let [msg (r/atom "")
        stop #(reset! msg "")
        save #(let [v (-> @msg str clojure.string/trim)]
                (if-not (empty? v) (create-message v))
                (stop))]
    (fn [] 
      [:input 
       {:type "text" 
        :value @msg 
        :id "msg-input" 
        :placeholder "Write something"
        :class "msg-input"
        :on-change #(reset! msg (-> % .-target .-value))
        ;; 13=Esc is not that important here, but since 
        ;; it was present in the example, leave it in.
        :on-key-down #(case (.-which %)
                        13 (save)
                        27 (stop)
                        nil)
        }])))

;; The username is updated to the global atom on-change.
;; create-message then lifts it from there to each message.
(defn username-field []
  [:input 
   {:type "text"
    :value @username
    :id "username-input"
    :placeholder "Give some username"
    :on-change #(reset! 
                 username 
                 (-> % .-target .-value))}])

(defn message-area []
  [:div {:class "message-area"}
   (for [m (vals @messages)]
     [:div {:class "message" :key (str (:id m))} 
      (:username m) ": " (:id m) " " (:message m)]
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
