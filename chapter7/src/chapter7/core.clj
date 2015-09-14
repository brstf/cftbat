(ns chapter7.core)

; Exercise 1:
; Use the list function, quoting, and read-string to create a list that,
; when evaluated, prints your first name and your favorite sci-fi movie
(defn favorite_movie
    [first-name movie]
    (read-string (str "(println (str \"" first-name "\" \" \" \"" movie"\"))")))

; Exercise 2:
; Create an infix function that takes a list like (1 + 3 * 4 - 5) and 
; transforms it into the lists that Clojure needs in order to correctly
; evaluate the expression using operator precedence rules.
(declare infix->prefix)
(defn infix
    [arithmetic-list]
    (->> arithmetic-list
        (map #(if (list? %) (infix %) %))
        (infix->prefix '(* /))
        (infix->prefix '(+ -))
        (first)))

; Replace first instance of either operator until none exist
(defn infix->prefix
    "Given a list of operators and a list of arithmetic operations, 
    change from infix to prefix notation on the operators"
    [ops al]
    ; If second element is in ops, reduce to prefix, recur
    ; otherwise combine first two with recursive call on drop 2
    (if (< (count al) 3)
        al
        (if (contains? (set ops) (second al))
            (infix->prefix ops (cons (list (second al) (first al) (nth al 2)) (drop 3 al)))
            (concat (take 2 al) (infix->prefix ops (drop 2 al))))))

(defn -main
    [& args]
    (eval (favorite_movie "Brian" "Interstellar"))
    (println (infix '(1 + 1)))
    (println (infix '(1 + 3 * 4 - 5)))
    (println (infix '((1 + 3) * 4 - 5))))