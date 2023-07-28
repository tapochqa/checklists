(ns checklists.handling
  (:require
    [tg-bot-api.telegram :as telegram]
    [checklists.checklists :as checklists]))


(defn the-handler 
  "Bot logic here"
  [config {:keys [message]} trigger-id]
  (let [things-link (checklists/parse-line (:text message))]
    (telegram/send-message 
      config 
      (-> message :chat :id) 
      (str 
        "<a href=\"https://lmnd.link/redirect?redirect=" things-link "\">"
        things-link
        "</a>")
      {:parse-mode :html})))
