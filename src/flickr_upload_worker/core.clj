(ns flickr-upload-worker.core
  (:gen-class)
  (:use flickr-upload-worker.config)
  (:use flickr-upload-worker.flickr-upload)
  (:use flickr-upload-worker.sqs)
	(:require [clojure.data.json :as json]))

(def maximumRecieveCount (get (get-userconfig) :maximumRecieveCount))

(defn upload-from-queue
	"Pull one from SQS and upload to flickr"
	[]
	(def queueItem (sqs-fetch))
	(def fullPath  (get (json/read-str  (get queueItem :body)) "fullPath"))
	(def recvCount  (get (get queueItem :attributes) "ApproximateReceiveCount"))
	(println (str "Queued File: " fullPath " pulled " recvCount " times."))
	(if (< (read-string recvCount) maximumRecieveCount) 
		(do 
			(flickr-upload fullPath)
			(sqs-delete queueItem)
			) 
		(println "Skipping")
		)
)

(defn upload-loop
	"Pull and pull and pull"
	[]
	(loop []
		(do
			(try
				(upload-from-queue)
				(catch com.flickr4java.flickr.FlickrException e 
					(prn "caught" e (. e getErrorCode) (. e getErrorMessage)))
				(catch OutOfMemoryError e 
					(prn "caught" e))
				(catch Exception e
					(prn "caught" e)))
			(recur)
		)
	)
)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (upload-loop))

; (sqs-fetch-attempt)
; (get (get (first (sqs/receive client q2 :attributes #{"All"})) :attributes) "ApproximateReceiveCount")
