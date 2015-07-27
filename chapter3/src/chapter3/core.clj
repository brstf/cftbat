(ns chapter3.core
    (:gen-class))

(require 'clojure.string)

(defn ex1
    "Exercise 1: Use str, vector, list, hash-map and hash-set functions"
    [str1 str2]
    ; Basic functionality:
    (println (str str1 str2))
    (println (vector str1 str2))
    (println (list str1 str2))
    (println (hash-map :s1 str1 :s2 str2))
    (println (hash-set str1 str2)))

(defn plus100
    "Exercise 2: Write a function that takes a number and adds 100 to it"
    [x]
    (+ x 100))

(defn dec-maker
    "Exercise 3: Write a function  that works exactly like inc-maker but 
    with subtraction"
    [d]
    (fn [x] (- x d)))

(defn mapset
    "Exercise 4: Write a function that works like map, but returns a set"
    [f l]
    (if (empty? (rest l))
        #{(f (first l))}
        (set (cons (f (first l)) (mapset f (rest l))))))

(defn matching-part
    ; Slight change from the text version to accomdate a change
    ; to symmetrize-body-parts
    "Returns a vector of the matching body part"
    [part]
    [{:name (clojure.string/replace (:name part) #"^left-" "right-")
     :size (:size part)}])

(defn alien-matching-part
    "Alien bodies have a left side, and a 1-4 side"
    [part]
    (into [] (map #(array-map :name 
        (clojure.string/replace (:name part) #"^left-" (str % "-"))
        :size (:size part)) (take 4 (drop 1 (range))))))

(defn alien-n-matching-part
    "Alien bodies with n parts have 1-n sides"
    [part n]
    (into [] (map #(array-map :name 
        (clojure.string/replace (:name part) #"^left-" (str % "-"))
        :size (:size part)) (take n (drop 1 (range))))))

(defn symmetrize-body-parts
    [asym-body-parts match-fn]
    (loop [remaining-asym-parts asym-body-parts final-body-parts []]
        (if (empty? remaining-asym-parts)
            final-body-parts
            (let [[part & remaining] remaining-asym-parts]
                (recur remaining
                    (into final-body-parts
                        (into #{part} (match-fn part))))))))

(defn symmetrize-body-parts-n
    "Given asymmetrical body parts and a number n, return a vector of 
    body parts with appropriately added symmetry for n body parts"
    [asym-body-parts n]
    (loop [remaining-asym-parts asym-body-parts final-body-parts []]
        (if (empty? remaining-asym-parts)
            final-body-parts
            (let [[part & remaining] remaining-asym-parts]
                (recur remaining
                    (into final-body-parts (into #{} 
                        (alien-n-matching-part part n))))))))

(defn -main
    [& args]
    (ex1 "Two" "Strings")
    (println (str "100 + 20 = " (plus100 20)))
    (def dec9 (dec-maker 9))
    (println (str "Dec9 called on 10: " (dec9 10)))
    (println (str "Mapset: " (mapset inc [1 1 2 2 3 4])))
    (def asym-hobbit-body-parts [
                {:name "head" :size 3}
                {:name "left-eye" :size 1}
                {:name "left-ear" :size 1}
                {:name "mouth" :size 1}
                {:name "nose" :size 1}
                {:name "neck" :size 2}
                {:name "left-shoulder" :size 3}
                {:name "left-upper-arm" :size 3}
                {:name "chest" :size 10}
                {:name "back" :size 10}
                {:name "left-forearm" :size 3}
                {:name "abdomen" :size 6}
                {:name "left-kidney" :size 1}
                {:name "left-hand" :size 2}
                {:name "left-knee" :size 2}
                {:name "left-thigh" :size 4}
                {:name "left-lower-leg" :size 3}
                {:name "left-achilles" :size 1}
                {:name "left-foot" :size 2}])
    (println (str "Hobbit: " (symmetrize-body-parts 
        asym-hobbit-body-parts matching-part)))
    (println (str "Alien : " (symmetrize-body-parts 
        asym-hobbit-body-parts alien-matching-part)))
    (println (str "3-Alien : " (symmetrize-body-parts-n 
        asym-hobbit-body-parts 3))))
