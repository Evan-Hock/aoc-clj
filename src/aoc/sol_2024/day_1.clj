(ns aoc.sol-2024.day-1
    (:gen-class)
    (:require [clojure.string :as str])
    (:require [aoc.core :as aoc]))

(defn -main []
    (aoc/puzzle 2024 1
        (fn [data]
            (->> data
                str/split-lines
                (map (fn [line]
                    (->> (str/split line #"\s+")
                        (map #(Integer/parseInt %))
                        vec)))
                (apply map vector)
                (map sort)
                vec))
        
        "Find the sum of the differences between the two lists"
        (fn [[left right]]
            (apply + (map (comp abs -) left right)))
            
        "Find the similarity scores between the left and right lists"
        (fn [[left right]]
            (let [freqs (frequencies right)]
                (reduce #(+ %1 (* %2 (get freqs %2 0))) 0 left)))))