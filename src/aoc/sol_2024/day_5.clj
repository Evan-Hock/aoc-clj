(ns aoc.sol-2024.day-5
    (:gen-class)
    (:require [aoc.core :as aoc])
    (:require [clojure.string :as str])
    (:require [clojure.set :as set]))


;; Parse the rules into a map of "exclusion sets"
;; An exclusion set is the set of numbers that cannot
;; come after a particular number according to the rules
(defn parse-rules [rules]
    (transduce
        (map
            (comp
                (partial map #(Integer/parseInt %))
                #(subvec % 1 3)))
        (completing (fn ([rules [before after]]
            (update rules after (fn [exclusion-set]
                (if (some? exclusion-set)
                    (conj exclusion-set before)
                    #{before}))))))
        {}
        (re-seq #"(\d+)\|(\d+)" rules)))


(defn parse-updates [updates]
    (->> updates
        str/split-lines
        (map (comp vec (partial map #(Integer/parseInt %)) #(str/split % #",")))))


(defn correctly-ordered? [get-exclusion-set update]
    (loop [xs (seq update) banned? #{}]
        (or
            (empty? xs)
            (let [[head & tail] xs]
                (and
                    (not (banned? head))
                    (recur tail (set/union banned? (get-exclusion-set head))))))))


(defn get-middle [coll]
    (nth coll (quot (count coll) 2)))


(defn -main []
    (aoc/puzzle 2024 5
        (fn [data]
            (let [[rules updates] (str/split data #"\n\n" 2)]
                [(parse-rules rules) (parse-updates updates)]))
        
        "The sum of the middle number of the correctly-ordered updates"
        (fn [[rules updates]]
            (transduce
                (comp
                    (filter #(correctly-ordered? rules %))
                    (map get-middle))
                +
                0
                updates))))