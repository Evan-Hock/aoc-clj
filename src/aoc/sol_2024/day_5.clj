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
        (map (comp (partial map #(Integer/parseInt %)) #(subvec % 1 3)))
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
    (loop [xs (seq update), banned? #{}]
        (or
            (empty? xs)
            (let [[head & tail] xs]
                (and
                    (not (banned? head))
                    (recur tail (set/union banned? (get-exclusion-set head))))))))


(defn get-middle [coll]
    (nth coll (quot (count coll) 2)))


(defn swapv [v i j]
    (assoc v i (v j), j (v i)))


;; This is basically a sorting algorithm.
;; It goes through every element in the update vector,
;; and it ensures that it has no elements after it that
;; are in its shitlist
(defn fix-ordering [shitlist-of update]
    (reduce
        (fn [v i]
            (let [start (inc i)]
                (loop [j start, acc v]
                    (cond
                        (>= j (count acc)) acc
                        (contains? (shitlist-of (acc i)) (acc j)) (recur start (swapv acc i j))
                        :else (recur (inc j) acc)))))
        update
        (range (dec (count update)))))


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
                updates))
                
        "The sum of the middle numbers of the incorrectly-ordered updates, when correctly ordered"
        (fn [[rules updates]]
            (transduce
                (comp
                    (remove #(correctly-ordered? rules %))
                    (map #(get-middle (fix-ordering rules %))))
                +
                0
                updates))))