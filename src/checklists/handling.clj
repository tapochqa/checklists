(ns checklists.handling
  (:require
    [tg-bot-api.telegram :as telegram]
    [checklists.checklists :as checklists]))


(defn the-handler 
  "Bot logic here"
  [config {:keys [message]} trigger-id]
  (let []
    (telegram/send-message 
      config 
      (-> message :chat :id) 
      (str
        "<code>"(checklists/parse-line (:text message) :encode-reveal false)"</code>"
        "\n""\n"
        "<a href=\"https://lmnd.link/redirect?redirect=" 
        (checklists/parse-line (:text message) :encode-reveal true) "\">"
        "Открыть в Фингсе"
        "</a>")
      {:parse-mode :html})))
