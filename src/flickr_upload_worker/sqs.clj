(ns flickr-upload-worker.sqs
	(:require [cemerick.bandalore :as sqs])
	(:require [clojure.data.json :as json]))

(def client (sqs/create-client))
(def region (. com.amazonaws.regions.Region getRegion com.amazonaws.regions.Regions/US_WEST_2))
(. client setRegion region)
(map #(println %) (sqs/list-queues client))
(def queueName (str "flickruploadfrom-" (clojure.string/replace (.. java.net.InetAddress getLocalHost getHostName) #"\." "_")))

(defn sqs-fetch
	"Try fetching some queue items from SQS"
	[]
	(def q (sqs/create-queue client queueName))
	(first (sqs/receive client q  :attributes #{"All"}))
	; (println (str "file: " (get (first (sqs/receive client q)) :body)))
	; (println (str "file: " (get (json/read-str (get (first (sqs/receive client q)) :body)) "fullPath"))))
)

(defn sqs-delete
	"Try delete a queue item from SQS"
	[queueItem]
	(sqs/delete client queueItem)
)