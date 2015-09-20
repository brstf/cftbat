(ns chapter9.core)

; Exercise 1:
; Write a function that takes a string as an argument and searches for
; it on Bing and Google using the slurp function. Your function should
; return the HTML of the first page returned by the result

(defn search-html
    [search-url search-term]
    (slurp (str search-url "search?q=" search-term)))

(defn web-search-ex1
    "Searches Bing and Google for the search term and returns the HTML
    of the first page returned"
    [search-term]
    (let [p (promise)]
        (doseq [url ["https://www.google.com/" "https://www.bing.com/"]]
            (future (deliver p (search-html url search-term))))
       (deref p 10000 "Timed out")))

; Exercise 2:
; Update your function so it takes a second argument consisting of the
; search engines to use

(defn web-search
    "Searches the given search domains for the search term and returns 
    the HTML of the first page of search results returned"
    [search-term search-engines]
    (let [p (promise)]
        (doseq [url search-engines]
            (future (deliver p (search-html url search-term))))
       (deref p 10000 "Timed out")))

; Exercise 3:
; Create a new function that takes a search term and search engines as 
; arguments, and returns a vector of the URLs from the first page of
; search results from each search engine

(defn get-links
    "Get a lazy sequence of links from an HTML page"
    [html-page]
    (map second (re-seq #"<a href=\"(http://.*?)\"" html-page)))

(defn multi-web-search
    "Searches each of the given search-engines for the given search-term
    and returns a list of all links found on the first page of each search"
    [search-term search-engines]
    (let [promises (take (count search-engines) (repeatedly promise))]
        (doseq [[p domain] (map list promises search-engines)]
            (future (deliver p (get-links (search-html domain search-term)))))
        (apply concat (map #(deref % 10000 "Timed out") promises))))

(defn -main
    [& args]
    (println "Exercise 1: [Google and Bing race on searching \"clojure\"]")
    (println (web-search-ex1 "clojure"))
    (println "Exercise 2: [Yahoo search for \"clojure\"]")
    (println (web-search "clojure" ["https://search.yahoo.com/"]))
    (println "Exercise 3: [Bing and Yahoo search for \"clojure\"]")
    (println (multi-web-search "clojure" ["https://www.bing.com/" "https://search.yahoo.com/"])))