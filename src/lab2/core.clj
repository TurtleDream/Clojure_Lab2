(ns lab2.core
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout onto-chan]]))

(defn print1 [chan]
  (<!! chan))

(defn group [chan, chan2]
  (a/go-loop[n 0 res []]
    (if (and (empty? res) (= n 0))
      (let [x (<! chan)]
        (if-not(nil? x)
          (recur x res)
          )
        )
      (if (or (empty? res) (> n 0))
        (let [x (<! chan)]
          (if-not (nil? x)
            (recur (dec n) (conj res x))
            ))
        (do (>! chan2 res)
            (recur 0 []))
        )
      )
    ))



(def ch (chan))
(def ch2 (chan 1))
(onto-chan ch '(3 4 0 2 1 2 2 4 5))
(group ch, ch2)
(print1 ch2)
(print1 ch2)
(print1 ch2)