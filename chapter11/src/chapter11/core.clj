(ns chapter11.core
    (:require [clojure.core.async
        :as a
        :refer [>! <! >!! <!! go chan buffer close! thread
                alts! alts!! timeout]]))

; Exercise 1:
; Translate the random quote exercise from Chapter 10 to use core.async
; instead of futures

(def rq-url "http://www.braveclojure.com/random-quote")

(defn word-count
    "Given a string, return a mapping of words to word count"
    [wctext]
    (->> (clojure.string/split wctext #"[ .;?!\-\n\r]")
        (filter #(not (empty? %)))
        (map #(clojure.string/lower-case %))
        (frequencies)))

(defn random-quote
	"Retrieve a random and get its word count map"
	[]
	(word-count (slurp rq-url)))

(defn update-wc
	"Given a word-count atom, update it given a new word count map
	and increment its count"
	[wc-atom wc-new]
	(swap! wc-atom (fn [old-wc] (merge-with + old-wc wc-new {:count 1}))))

(defn random-quotes-word-count
	"Given a number of random-quotes to download, return a map of words 
    to word count for the downloaded quotes"
	[num-quotes]
	(let [ch (chan) ; Word count updating channel
		  rt (chan) ; Return channel
		  wc (atom {:count 0})]
		(go 
			(while true (do 
				(update-wc wc (<! ch))
				(if (= (:count @wc) num-quotes) (>! rt @wc)))))
		(dotimes [n num-quotes] (go (>! ch (random-quote))))
		(<!! rt)))

; Server responsds with 503 if this number is too high (~50)
(defn -main
	[& args]
	(time (random-quotes-word-count 25)))