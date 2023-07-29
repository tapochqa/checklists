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


(defn into-assoc-replace
  [coll item key val]
  (replace
      {item
       (assoc item key
         (into (key item)
           val))}
      coll))


(defn add-items
  [params project items]
  
  (let [desired
        (first
          (filter
            (fn [i] (= project (-> i :attributes :title)))
            params))]
    
    (into-assoc-replace
      params
      desired
      :attributes
      {:items (mapv item items)})))


(defn add-deadline
  [params project deadline]
  (let [desired
        (first
          (filter
            (fn [i] (= project (-> i :attributes :title)))
            params))]
    
    
    (-> params
      (into-assoc-replace
        desired
        :attributes
        {:deadline deadline})
      (into-assoc-replace
        desired
        :attributes
        {:when :someday})
    
    
    )))


(defn parse-keywords
  [code]
  (-> code
    str
    (str/replace #"!date"
      (.format (java.text.SimpleDateFormat. "dd.MM") (new java.util.Date)))
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
        (str line)

        code 
        (parse-keywords code)
                
        code 
        (str/replace code #" " " ")
        
        splitted 
        (str/split code #": ")
        
        project 
        (first splitted)
        
        project' (str/replace project #"!" "")
        
        deadline
        (if (= (last project) \!)
          :today
          nil)
        
        items 
        (str/split (last splitted) #", ")
        
        checklist
        (make-checklist
          (add-project project')
          (add-deadline project' deadline)
          (add-items project' items))]
    
    (if encode-reveal
      
      (URLEncoder/encode 
        (format 
          "things:///json?data=%s"
          (-> checklist
            (json/encode)
            (str "&reveal=true")))
        "UTF-8")
      
      (format 
        "things:///json?data=%s"
        (-> checklist
          (json/encode)
          (URLEncoder/encode "UTF-8")
          (str "&reveal=true"))))))

(comment
  
  (parse-line "Buy list !date!: milk, bread !time, poisons")
  (parse-line nil :encode-reveal true)
  
  (str/replace nil #"!date"
      (.format (java.text.SimpleDateFormat. "dd.MM") (new java.util.Date))
      )
  )










