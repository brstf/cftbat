(ns chapter10.core)

; Exercise 1:
; Create an atom with the initial value 0, use swap! to increment a 
; couple of times, and then dereference it
(defn atomtest
    "Defines an atom and performs a few swaps on it"
    []
    (let [a (atom 0)]
        (println (str "Initial value: " @a))
        (swap! a inc)
        (swap! a inc)
        (println (str "After 2 swaps: " @a))))

; Exercise 2:
; Create a function that uses futures to parallelize the task of 
; downloading random qutes from  http://www.braveclojure.com/random-quote
; using (slurp "http://www.braveclojure.com/random-quote"). The futures
; should update an atom that refers to a total word count for all quotes.
; The function will take the number of quotes to download as an argument 
; and return the atom's final value

(def rq-url "http://www.braveclojure.com/random-quote")

(defn word-count
    "Given a string, return a mapping of words to word count"
    [wctext]
    (->> (clojure.string/split wctext #"[ .;?!\-\n\r]")
        (filter #(not (empty? %)))
        (map #(clojure.string/lower-case %))
        (frequencies)))

(defmacro n-futures
    "Create n-futures that all execute the same task, then block and wait
    for each future to finish"
    [n body]
    `(let [promises# (take ~n (repeatedly promise))]
        (doseq [p# promises#]
            (do ~body (deliver p# true)))
        (every? ~deref promises#)))

(defn random-quote-word-count
    "Given a number of random-quotes to download, return a map of words 
    to word count for the downloaded quotes"
    [num-quotes]
    (let [wc (atom {})]
      (do 
        (n-futures num-quotes 
            (swap! wc (fn [current-wc] 
                        (merge-with + current-wc (word-count (slurp rq-url))))))
        @wc)))

; Exercise 3:
; Create a representation of two characters in a game. The first character has
; 15 health points out of a total of 40.  The second character has a healing 
; potion in his inventory.  Use refs and transactions to model the consumption 
; of the healing potion and the first character healing.

(defn str-character-state
  "Given a character map, return a string of its state"
  [character]
  (clojure.string/join " " (map #(str % " => " (% character)) (keys character))))
  
(defn ex3
  "Model the consumption of character 2's health potion by character 1"
  []
  (let [guy1 (ref {:hp 15 :max-hp 40 :health-potions 0})
        guy2 (ref {:hp 40 :max-hp 40 :health-potions 1})]
    (do
      (println (str "Character 1: " (str-character-state @guy1)))
      (println (str "Character 2: " (str-character-state @guy2)))
      (println "Drinking potion....")
      (dosync 
        (alter guy2 update-in [:health-potions] dec)
        (alter guy1 update-in [:hp] + (- (:max-hp @guy1) (:hp @guy1))))
      (println (str "Character 1: " (str-character-state @guy1)))
      (println (str "Character 2: " (str-character-state @guy2))))))
                  


(defn -main
    [& args]
    (atomtest)
    (println (random-quote-word-count 8))
    (ex3))