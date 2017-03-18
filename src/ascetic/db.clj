(ns ascetic.db
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2:file"
              :subname "db/ascetic"})

(defn create-message-table
  []
  (sql/db-do-commands db-spec
                      (sql/create-table-ddl :messages
                                            [[:timestamp "bigint"]
                                             [:content "varchar(80)"]])))


(defn add-message-to-db
  [content]
  "Add the content and a Unix timestamp to the database."
  (sql/insert! db-spec :messages
               {:timestamp (quot (.getTime (new java.util.Date)) 1000)
                :content content}))

(defn get-all-messages 
  []
  (sql/query db-spec ["SELECT * FROM messages"]))

(defn delete-all-messages 
  []
  (sql/execute! db-spec ["DELETE FROM messages"]))

(defn drop-message-table 
  []
  (sql/db-do-commands db-spec
                      (sql/drop-table-ddl :messages)))


;; (defn add-message-to-db 
;;   [timestamp content]
;;   (let [results (sql/with-connection db-spec
;;                      (sql/insert-record :messages
;;                                         {:timestamp timestamp :content content}))]
;;     (assert (= (count results) 1))
;;     (first (vals results))))

;; ;; Not done!
;; (defn get-message
;;   [loc-id]
;;   (let [results (sql/with-connection db-spec
;;                   (sql/with-query-results res
;;                     ["select x, y from locations where id = ?" loc-id]
;;                     (doall res)))]
;;     (assert (= (count results) 1))
;;     (first results)))

;; (defn get-all-messages
;;   []
;;   (let [results (sql/with-connection db-spec
;;                   (sql/with-query-results res
;;                     ["select timestamp, content from messages"]
;;                     (doall res)))]
;;     results))

;; (defn delete-all
;;   []
;;   (let [results (sql/with-connection db-spec 
;;                   (sql/drop-table :messages))]
;;     results))
