(ns leventrie.core
  (:use [clojure.tools.cli :only (cli)])
  (:gen-class))

(defn add-to-trie [trie x]
  (assoc-in trie x (merge (get-in trie x) {:terminal true})))


(defn update-col-by-extended-char [prev-col word c]
  (reduce
   (fn [col prev-c]
     (let [index (dec (count col))]
       (conj col (min
                  (inc (get col index))
                  (inc (get prev-col (inc index)))
                  (+ (get prev-col index) (if (= prev-c c) 0 1))))))
   [(inc (first prev-col))] ; initial
   word)) ; loop each char in the word

(defn search-trie-by-cost [trie word maxcost]
  (defn FF [prev-col trie]
    (mapcat
     (fn [[k v]]
       (cond
        (= k :terminal) ;then
        (if (<= (last prev-col) maxcost) [v])
        (map? v) ;then
        (if (<= (apply min prev-col) maxcost) (FF (update-col-by-extended-char prev-col word k) v))))
     trie))
  (FF (vec (range 0 (inc (count word)))) trie))


(defn search-trie-by-cost2 [trie word maxcost]
  (loop [col-suffix ; is a list of {:prev-col vec, :suffix string, :des map/true}.
         (map #(hash-map
                :prev-col (update-col-by-extended-char (vec (range 0 (inc (count word)))) word (key %))
                :suffix (key %)
                :des (val %))  trie)
         result (transient [])] ; suffix is a mapentry
    (if (empty? col-suffix) result
        (let [updated-col-suffix
              (mapcat
               (defn update-col-suffix [{:keys [prev-col suffix des]}]
                 (if (map? des) ;then
                   (if (<= (apply min prev-col) maxcost)
                       (mapcat
                        (fn [[k v]]
                          [{:prev-col (if (= k :terminal) prev-col (update-col-by-extended-char prev-col word k)) :suffix (str suffix k) :des v}])
                        des))
                   (if (<= (last prev-col) maxcost) [{:result suffix}])))
               col-suffix)]
          (recur
           (filter #(:suffix %) updated-col-suffix)
           (reduce #(conj! %1 %2) result (for [i updated-col-suffix :when (:result i)] (:result i))))))))


;;; ---------------------------
(defn -main
  "Input a library file and convert into a trie, for each word from stdin, do a quick search in the trie, output terms within given levenschtein distance."
  [& args]
  (let [[opts args banner]
        (cli args
             ["-h" "--help" "Show help" :flag true :default false]
             ["-d" "--distance" "the maxmimal distance allowed" :parse-fn #(Integer/parseInt %) :default 3]
             )]
    (when (or (not (first args)) (:help opts))
      (println banner)
      (println "Input a library file and convert into a trie, for each word from stdin, do a quick search in the trie, output terms within given levenschtein distance to arbitray leave in the trie.")
      (println "Example:   cat query.txt | java -jar $0 -d 4 library.txt")
      (println "Contact: qiulin.work@gmail.com")
      (println "Date:    Tue Dec  2 17:33:14 HKT 2014")
      (System/exit 0))

    (let [trie (reduce add-to-trie {}  (line-seq (clojure.java.io/reader (first args) :encoding "UTF-8")))]
      (println "query\tlibrary")
       (doseq [line (line-seq (clojure.java.io/reader *in*))]
         (doseq [i (persistent! (search-trie-by-cost2 trie line (:distance opts)))]
           (println (clojure.string/join "\t" [line (clojure.string/replace i #":terminal" "")])))))))

