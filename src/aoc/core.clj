(ns aoc.core
    (:require [clojure.java.io :as io])
    (:import java.net.URI)
    (:import (java.net.http HttpClient HttpRequest HttpResponse HttpResponse$BodyHandlers)))

(defn- load-session []
    (if-some [session-id (System/getenv "AOC_SESSION")]
        session-id
        (throw (RuntimeException. "aoc: AOC_SESSION environment variable is not set!"))))

(defn load-input [year day]
    (let [file (io/as-file (str "./aoc-" year "-" day "-input.txt"))]
        (when-not (.exists file)
            (let
                [http-client (HttpClient/newHttpClient)
                http-request
                    (.build
                        (cond->
                            (.. HttpRequest
                                (newBuilder (URI/create (str "https://adventofcode.com/" year "/day/" day "/input")))
                                (setHeader "Cookie" (str "session=" (load-session))))
                            (some? (System/getenv "AOC_USER_AGENT")) (.setHeader "UserAgent" (System/getenv "AOC_USER_AGENT"))))
                http-response (.send http-client http-request (HttpResponse$BodyHandlers/ofFile (.toPath file)))]
                (.body http-response)))
        (slurp file)))

(defn puzzle
    "Make a solution to an AOC puzzle.

    body => part-info [part-info]
    part-info => description solver

    `description` should evaluate to a string, and
    `parser` and `solver` should evaluate to functions.

    The `parser` function should take a string and return
    useful data.

    The `solver` should take that data and get the solution
    from it."
    [year day parse & sols]
    (assert (some #{(count sols)} '(2 4)) "Body must have either 2 or 4 elements.")
    (let
        [parsed-input (parse (load-input year day))]
        (letfn
            [(submit [i [desc sol]]
                (println (str "Part " i " (" desc "): " (sol parsed-input))))]
            (dorun (map submit (map inc (range)) (partition 2 sols))))))