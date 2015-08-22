(ns chapter4.core)

(require 'clojure.string)

(def filename "resources/suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
    [str]
    (Integer. str))

(def conversions {:name identity :glitter-index str->int})

(defn convert
    [vamp-key value]
    ((get conversions vamp-key) value))

(defn parse
    "Convert a CSV into rows of columns"
    [string]
    (map #(clojure.string/split % #",")
        (clojure.string/split string #"\n")))

(defn mapify
    "Return a seq of maps like {:name Edward Cullen :glitter-index 10}"
    [rows]
    (map (fn [unmapped-row]
        (reduce (fn [row-map [vamp-key value]]
            (assoc row-map vamp-key (convert vamp-key value)))
        {}
        (map vector vamp-keys unmapped-row)))
    rows))

(defn glitter-filter
    [minimum-glitter records]
    (filter #(>= (:glitter-index %) minimum-glitter) records))

; Exercise 1:
; Turn the result of your glitter-filter function into a list of names
(defn filter-names
    "Return a list of just the names of suspects returned by glitter-filter"
    [filtered]
    (map #(:name %) filtered))

; Exercise 2:
; Write a function 'append' which will append a name to your list of 
; suspects

; Exercise 3:
; Write a function validate which will check that :name and :glitter-index
; are present during append
(defn validate
    "Checks if all words in the list keywords are in the map record, return
    nil if all keywords are not present, or the record otherwise."
    [keywords record]
    (and (every? identity (map #(get record %) keywords)) record))

(defn append
    "Append a name to the list of suspects"
    [suspect-list new-suspect]
    (concat suspect-list (and (validate vamp-keys new-suspect) [new-suspect])))

; Exercise 4:
; Write a function that will take your list of maps and convert it back
; to a CSV string

(def filename-out "resources/suspects-out.csv")

(defn record-string
    "Create a comma separated string from a name record"
    [record]
    (clojure.string/join "," (map #(get record %) vamp-keys)))

(defn names-csv
    "Create newline separated string of names in the map"
    [records]
    (clojure.string/join "\n" (map #(record-string %) records)))

(defn suspects-to-file
    "Write the given suspects list out to the given filename"
    [suspects file-out]
    (spit file-out (names-csv suspects)))

(defn -main
    [& args]
    (def suspects (glitter-filter 3 (mapify (parse (slurp filename)))))
    (def appended-suspects (append suspects {:name "McFishwick", :glitter-index 0}))
    (dorun (map #(println %) (filter-names appended-suspects)))
    (suspects-to-file appended-suspects filename-out))