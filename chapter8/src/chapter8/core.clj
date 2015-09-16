(ns chapter8.core)

;
; Brews for the Brave and True sample code:
;
(def order-details-success
    {:name "Mitchard Blimmons"
     :email "mitchard.blimmons@gmail.com"})

(def order-details-failure
    {:name "Mitchard Blimmons"
     :email "mitchard.blimmonsgmail.com"})

(def order-details-validations
    {:name
        ["Please enter a name" not-empty]

     :email
        ["Please enter an email address" not-empty
        
        "Your email address doesn't look like an email address"
        #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
    "return a seq of error messages"
    [to-validate message-validator-pairs]
    (map first (filter #(not ((second %) to-validate))
        (partition 2 message-validator-pairs))))

(defn validate
    "Returns a map with a vec of errors for each key"
    [to-validate validations]
    (reduce (fn [errors validation]
        (let [[fieldname validation-check-groups] validation
               value (get to-validate fieldname)
               error-messages (error-messages-for value validation-check-groups)]
            (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
        {}
        validations))

(defmacro if-valid
    "Handle validation more concisely"
    [to-validate validations errors-name & then-else]
    `(let [~errors-name (validate ~to-validate ~validations)]
        (if (empty? ~errors-name)
            ~@then-else)))

; Exercise 1:
; Write the macro when-valid so that it behaves similarly to when.
(defmacro when-valid
    [to-validate validations & do-rest]
    `(if-valid ~to-validate ~validations error#
        (do ~@do-rest)))

; Exercise 2:
; You saw that and is implemented as a macro. Implement or as a macro.
(defmacro my-or
    ([] false)
    ([x] x)
    ([x & tail]
        `(let [or# ~x]
            (if or# or# (my-or ~@tail)))))

; Exercise 3:
; In Chapter 5 you created a series of functions (c-int, c-str, c-dex) to
; read an RPG characters attributes. Write a macro that defines an 
; arbitrary number of attribute-retrieving functions using one macro call
(defmacro defattrs
    [& attrs]
    `(do ~@(map #(intern *ns* (first %) (comp (second %) :attributes))
        (partition 2 attrs))))

(defattrs c-int :intelligence
          c-str :strength
          c-dex :dexterity)

; (From chapter 5)
(def character
    {:name "Smooches McCutes"
     :attributes {:intelligence 10
                  :strength 4
                  :dexterity 5}})

(defn -main
    [& args]
    (if-valid order-details-success order-details-validations error-name
        (println :success)
        (println :failure error-name))
    (println (when-valid order-details-success order-details-validations
        (println "It's valid!")
        (map second (seq order-details-success))))
    (println "Or test1:" (my-or true false false))
    (println "Or test2:" (my-or))
    (println "Or test3:" (my-or false false))
    (println "Or test4:" (my-or true (println (inc 1))))
    (println "Or test5:" (my-or (println "Mid-or") (inc 1)))
    (println "Intelligence:" (c-int character) "Strength:" 
        (c-str character) "Dexterity:" (c-dex character)))