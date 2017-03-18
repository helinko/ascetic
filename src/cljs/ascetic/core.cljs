(ns ascetic.core
  (:require
   [reagent.core :as reagent]
   ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Vars

(defonce debug?
  ^boolean js/goog.DEBUG)

(defonce app-state
  (reagent/atom
   {:messages 
    [{:user "matt" :message "diudiudiu"}
     {:user "matt" :message "DASASDASKOADSKODAS"}
     {:user "john" :message "NONONONONONONONON"}
     {:user "dave" :message "YES"}]}))

(defonce new-message
  (reagent/atom
   "Write something"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Page

(defn message-printer []
  [:div {:class "message-area"}
   (for [m (:messages @app-state)]
     [:div {:class "message"} 
      (:user m) ": " (:message m)]
     )])

(defn message-input []
  [:div.message-input 
   [:input {:type "text" 
            :name "message-input"
            :value @new-message}]]
  )


(defn page [ratom]
  [:div {:class "messages-and-input"}
   [message-printer]
   [message-input]]
  )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")
    ))

(defn reload []
  (reagent/render [page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (reload))
