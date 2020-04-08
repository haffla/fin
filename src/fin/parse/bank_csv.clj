(ns fin.parse.bank-csv
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [clojure.data.csv :as csv]))

(def map-row
  (fn [row] {:booked (first row)
             :to (get row 3)
             :purpose (get row 8)
             :currency (get row 10)
             :amount (get row 11)
             :plus? (= (last row) "H")}))

(defn parse-bank-csv []
  (with-open [reader (io/reader (io/resource "bank.csv"))]
    (let [data (csv/read-csv reader :separator \;)]
      (doall
       (map
        map-row
        data)))))

