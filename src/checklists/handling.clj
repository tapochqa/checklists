(ns checklists.handling
  (:require
    [tg-bot-api.telegram :as telegram]
    [checklists.checklists :as checklists]))


(defn the-handler 
  "Bot logic here"
  [config {:keys [message]} trigger-id]
  
  (telegram/send-message 
    config 
    (-> message :chat :id) 
    (checklists/parse-line (:text message))
    
    ))
