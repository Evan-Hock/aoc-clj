(ns aoc.sol-2024.day-3
    (:gen-class)
    (:require [aoc.core :as aoc]))


(defn -main []
    (aoc/puzzle 2024 3
        identity
        
        "The sum of all the multiplications"
        (fn [mem]
            (->> mem
                (re-seq #"mul\((\d+),(\d+)\)")
                (transduce
                    (map (comp (partial apply *) (partial map #(Integer/parseInt %)) #(subvec % 1 3)))
                    +)))
                
        "Only enabled multiplications count"
        (fn [mem]
            (as-> mem x
                (re-seq #"do\(\)|don't\(\)|mul\((\d+),(\d+)\)" x)
                (transduce
                    (map (fn [[match x y]]
                        (case match
                            "do()" :do
                            "don't()" :dont
                            (* (Integer/parseInt x) (Integer/parseInt y)))))
                    (completing (fn [[multiplier acc] inst]
                        (case inst
                            :do [1 acc]
                            :dont [0 acc]
                            [multiplier (+ acc (* multiplier inst))])))
                    [1 0]
                    x)
                (get x 1)))))