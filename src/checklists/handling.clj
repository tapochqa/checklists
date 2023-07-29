(ns checklists.handling
  (:require
    [tg-bot-api.telegram :as telegram]
    [checklists.checklists :as checklists]))


(defn the-handler 
  "Bot logic here"
  [config {:keys [message edited_message]} trigger-id]
  (let [message (conj {} message edited_message)]
    (telegram/send-message 
      config 
      (-> message :chat :id)
      (format
        "<code>%s</code>\n\n<a href=\"https://lmnd.link/redirect?redirect=%s\">Открыть в Фингсе</a>"
        (checklists/parse-line (:text message) :encode-reveal false)
        (checklists/parse-line (:text message) :encode-reveal true))
      {:parse-mode :html})))


(comment
  
  )