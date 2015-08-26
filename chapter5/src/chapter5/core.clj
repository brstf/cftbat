(ns chapter5.core)

(def character
    {:name "Smooches McCutes"
     :attributes {:intelligence 10
                  :strength 4
                  :dexterity 5}})

; Exercise 1:
; You used (comp :intelligence :attributes) to create a function that
; returns a character’s intelligence. Create a new function, attr, 
; that you can call like (attr :intelligence) and that does the same thing
(defn attr
    "Given an attribute keyword, return a function that takes a character
    and returns the given attribute"
    [attr-keyword]
    (comp attr-keyword :attributes))

(def c-int (attr :intelligence))

; Exercise 2:
; Implement the comp function
(defn comp-ex
    "Functionally equivalent to comp"
    ([f] (fn [& args] (apply f args)))
    ([f & fs]
    (fn [& args]
        (f (apply (apply comp-ex fs) args)))))

; Exercise 3:
; Implement the assoc-in function
(defn assoc-in-ex
    "Functionally equivalent to assoc-in"
    [m [k & ks] v]
    (if (empty? ks)
        (assoc m k v)
        (assoc m k (assoc-in-ex (get m k) ks v))))

; Exercise 4:
; Look up and use update-in
(defn use-update-in
    "Test function for update-in, increment dexterity"
    []
    (update-in character [:attributes :dexterity] inc))

; Exercise 5:
; Implement update-in
(defn update-in-ex
    "Functionally equivalent to update-in"
    [m [k & ks] f & args]
    (if (empty? ks)
        ; TODO this doesn't work applying the funciton like this
        (assoc m k (apply f (concat args [(get m k)])))
        (assoc m k (apply update-in-ex (get m k) ks f args))))

(defn -main
    []
    (println (c-int character))
    (println ((comp inc *) 2 3))
    (println ((comp-ex inc *) 2 3))
    (println ((comp int inc #(/ % 2)) 10))
    (println ((comp-ex int inc #(/ % 2)) 10))
    (println (assoc-in-ex character [:attributes :strength] 5))
    (println (use-update-in))
    (println (update-in-ex character [:attributes :dexterity] inc)))