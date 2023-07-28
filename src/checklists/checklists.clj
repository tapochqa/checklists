(ns checklists.checklists
  (:require 
    [clojure.string :as str]
    [cheshire.core :as json])
  (:import [java.net URLEncoder]))


(defn add-project
  [params title]
  
  (conj params
    {:type 
     :project
     
     :attributes
     {:title title}}))


(defn item
  [title]
  {:type 
   :to-do
   
   :attributes
   {:title title}})


(defn add-items
  [params project items]
  
  (let [desired
        (first
          (filter
            (fn [i] (= project (-> i :attributes :title)))
            params))]
    
    (replace
      {desired
       (assoc desired :attributes
         (into (:attributes desired)
           {:items (mapv item items)}))}
      params)))


(defn parse-keywords
  [code]
  (-> code
    (str/replace #"!date"
      (.format (java.text.SimpleDateFormat. "dd.MM") (new java.util.Date))
      )
    (str/replace #"!time"
      (.format (java.text.SimpleDateFormat. "hh:mm") (new java.util.Date)))))


(defmacro make-checklist
  [& commands]
  `(let [params# ~[]]
        (-> params#
          ~@commands)))


(defn parse-line
  "Example line:
   Buy list !date: milk, bread !time, poisons"
  [line & {:keys [encode-reveal]}]
  (let [code 
        (parse-keywords line)
        
        code 
        (str/replace code #" " " ")
        
        splitted 
        (str/split code #": ")
        
        project 
        (first splitted)
        
        items 
        (str/split (last splitted) #", ")
        
        checklist
        (make-checklist
          (add-project project)
          (add-items project items))]
    
    (if encode-reveal
      
      (URLEncoder/encode 
        (str "things:///json?data="
          (-> checklist
            (json/encode)
            (str "&reveal=true")))
        "UTF-8")
      
      (str "things:///json?data="
        (-> checklist
          (json/encode)
          (URLEncoder/encode "UTF-8")
          (str "&reveal=true"))))))

(comment
  
  (parse-line "Buy list !date: milk, bread !time, poisons"))










