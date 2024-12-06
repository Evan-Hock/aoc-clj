(ns aoc.sol-2024.day-4
    (:gen-class)
    (:require [aoc.core :as aoc])
    (:require [clojure.string :as str]))


(defn with-index [coll]
    (map-indexed vector coll))


(defn mdims [mat]
    [(alength mat) (alength (aget mat 0))])


(defn desc [i]
    (range i (- i 4) -1))
    

(defn asc [i]
    (range i (+ i 4)))


(defn const [i]
    (repeat 4 i))


(defn mextract [mat v-indices h-indices]
    (str/join (map #(aget mat %1 %2) v-indices h-indices)))


(defn xmas-nw? [mat i j]
    (and
        (>= i 3)
        (>= j 3)
        (= "XMAS" (mextract mat (desc i) (desc j)))))


(defn xmas-n? [mat i j]
    (and
        (>= i 3)
        (= "XMAS" (mextract mat (desc i) (const j)))))


(defn xmas-ne? [mat i j]
    (let [m (get (mdims mat) 1)]
        (and
            (>= i 3)
            (<= j (- m 4))
            (= "XMAS" (mextract mat (desc i) (asc j))))))


(defn xmas-e? [mat i j]
    (let [m (get (mdims mat) 1)]
        (and
            (<= j (- m 4))
            (= "XMAS" (mextract mat (const i) (asc j))))))


(defn xmas-se? [mat i j]
    (let [[n m] (mdims mat)]
        (and
            (<= i (- n 4))
            (<= j (- m 4))
            (= "XMAS" (mextract mat (asc i) (asc j))))))


(defn xmas-s? [mat i j]
    (let [n (get (mdims mat) 0)]
        (and
            (<= i (- n 4))
            (= "XMAS" (mextract mat (asc i) (const j))))))


(defn xmas-sw? [mat i j]
    (let [n (get (mdims mat) 0)]
        (and
            (<= i (- n 4))
            (>= j 3)
            (= "XMAS" (mextract mat (asc i) (desc j))))))

    
(defn xmas-w? [mat i j]
    (and
        (>= j 3)
        (= "XMAS" (mextract mat (const i) (desc j)))))


(defn xmas-count-for [mat i j]
    (cond-> 0
        (xmas-nw? mat i j) inc
        (xmas-n? mat i j) inc
        (xmas-ne? mat i j) inc
        (xmas-e? mat i j) inc
        (xmas-se? mat i j) inc
        (xmas-s? mat i j) inc
        (xmas-sw? mat i j) inc
        (xmas-w? mat i j) inc))


(defn mas? [s]
    (or
        (= s "MAS")
        (= s "SAM")))


(defn x-mas? [mat i j]
    (let
        [nw-se-diag (mextract mat (range (dec i) (+ i 2)) (range (dec j) (+ j 2)))
         sw-ne-diag (mextract mat (range (inc i) (- i 2) -1) (range (dec j) (+ j 2)))]
        (and
            (mas? nw-se-diag)
            (mas? sw-ne-diag))))


(defn -main []
    (aoc/puzzle 2024 4
        (fn [data]
            (let
                [lines (str/split-lines data)
                 mat (make-array Character/TYPE (count lines) (count (first lines)))]
                (doseq
                    [[i line] (with-index lines)
                     [j char] (with-index line)]
                    (aset mat i j, char))
                mat))
        
        "The number of occurences of XMAS"
        (fn [mat]
            (let [[n m] (mdims mat)]
                (->>
                    (for
                        [i (range 0 n)
                         j (range 0 m)]
                        [i j])
                    (map (fn [[i j]] (xmas-count-for mat i j)))
                    (apply +))))

        "The number of X-MASes"
        (fn [mat]
            (let [[n m] (mdims mat)]
                (->>
                    (for
                        [i (range 1 (dec n))
                         j (range 1 (dec m))]
                        [i j])
                    (filter (fn [[i j]] (x-mas? mat i j)))
                    count)))))