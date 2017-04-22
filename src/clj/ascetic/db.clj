(ns ascetic.db
  (:require 
   [clojure.java.jdbc :as sql]))

(def db-spec 
  {:classname "org.h2.Driver"
   :subprotocol "h2:file"
   :subname "db/ascetic"})

(defn create-messages-table []
  (sql/with-connection
    db-spec
    (sql/create-table 
     :messages
     [:id "bigint primary key auto_increment"]
     [:username "varchar(16)"]
     [:message "varchar(80)"])
    (sql/insert-records :messages
                        {:})
    ))

(defn all-messages []
  (sql/query db-spec ["SELECT * FROM messages"]))


(defn delete-all-messages 
  []
  (sql/execute! db-spec ["DELETE FROM messages"]))

(defn drop-message-table 
  []
  (sql/db-do-commands db-spec
                      (sql/drop-table-ddl :messages)))
