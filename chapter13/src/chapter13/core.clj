(ns chapter13.core)

; Exercise 1:
; Extend the full-moon-behavior multimethod to add behavior for your
; own kind of were creature
(defmulti full-moon-behavior
    (fn [were-creature] 
        (:were-type were-creature)))

(defmethod full-moon-behavior :wolf
    [were-creature]
    (str (:name were-creature) " will howl and murder"))

(defmethod full-moon-behavior :simmons
    [were-creature]
    (str (:name were-creature) " will encourage people and sweat to the oldies"))

(defmethod full-moon-behavior :bear
    [were-creature]
    (str (:name were-creature) " has the right to bear arms"))

; Exercise 2:
; Create a WereSimmons record type, then extend the WereCreature
; protocol
(defprotocol WereCreature
    (full-moon-behavior-record [x]))

(defrecord WereWolf [name title]
    WereCreature
    (full-moon-behavior-record [x]
        (str name " will howl and murder")))

(defrecord WereSimmons [name title]
    WereCreature
    (full-moon-behavior-record [x]
        (str name " will encourage people and sweat to the oldies")))

; Exercise 3:
; Create your own protocol, and then extend it using extend-type
; and extend-protocol
(defprotocol JavaSequence
    (add [x el])
    (size [x]))

(extend-protocol JavaSequence
    java.util.ArrayList
    (add [x el] (.add x el))
    (size [x] (.size x))

    java.util.Stack
    (add [x el] (.push x el))
    (size [x] (.size x)))


(defn test-protocol
    [java-seq]
    (dotimes [n 5] (add java-seq n))
    (size java-seq))
  
; Exercise 4:
; Create a role-playing game that implements behavior using multiple
; dispatch
(defmulti attack (fn [x] (:class x)))
(defmethod attack :fighter
    [character]
    (str "Character " (:name character) " swings his " (:weapon character)))
(defmethod attack :mage
    [character]
    (str "Character " (:name character) " casts " (:spell character)))

(defn -main
    [& args]
    (println (full-moon-behavior {:name "Bearnard" :were-type :bear}))
    (println (full-moon-behavior-record (map->WereSimmons {:name "Rich" :title "Supervisor"})))
    (let [arr-list (java.util.ArrayList.)]
        (println (str "Size: " (test-protocol arr-list)))
        (println arr-list))
    (let [stack (java.util.Stack.)]
        (println (str "Size: " (test-protocol stack)))
        (println stack))
    (println (attack {:name "Tucker", :class :fighter, :weapon "sword"}))
    (println (attack {:name "Morphumax", :class :mage, :spell "magic missile"})))