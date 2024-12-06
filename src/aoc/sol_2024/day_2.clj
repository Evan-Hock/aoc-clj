(ns aoc.sol-2024.day-2
    (:gen-class)
    (:require [aoc.core :as aoc])
    (:require [clojure.string :as str]))


(defn monotonic? [report]
    (or (apply < report) (apply > report)))


(defn within-safe-range? [report]
    (let [adj-diffs (map (comp abs -) report (rest report))]
        (every? #(<= % 3) adj-diffs)))


(defn safe? [report]
    (and (monotonic? report) (within-safe-range? report)))


(defn one-removed [report]
    (seq
        (loop [pre [] post report acc []]
            (if (empty? post)
                acc
                (let [tail (rest post)]
                    (recur
                        (conj pre (first post))
                        tail
                        (conj acc (concat pre tail))))))))


(defn -main []
    (aoc/puzzle 2024 2
        (fn [data]
            (->> data
                str/split-lines
                (map
                    (fn [line]
                        (->> (str/split line #"\s+")
                            (map #(Integer/parseInt %)))))))
        
        "How many reports are safe?"
        (fn [reports]
            (count (filter safe? reports)))

        "How many reports are safe with the Problem Dampener?"
        (fn [reports]
            (->>
                reports
                (filter #(or (safe? %) (boolean (some safe? (one-removed %)))))
                count))))