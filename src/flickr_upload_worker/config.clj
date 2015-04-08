(ns flickr-upload-worker.config
	(:require [clojure.edn :as edn]))


(defn default-config
  "Create a reasonable default configuration. To be used on the
   first execution of the program or if any error occurs when
   trying to restore an existing configuration."
  []
  (identity {:flickrApiKey "unset"
     :flickrApiSharedSecret "unset"
     :extendedUseToken "unset"
     :extendedUseSecret "unset"
     :maximumRecieveCount 5}))
(def save-config (partial spit "config.edn"))
(defn load-config
  "Read configuration data back from disk"
  []
  (try
    (clojure.edn/read-string (slurp "config.edn"))
    (catch Exception e (default-config))))

(let [cfg-state (atom (load-config))]
  (add-watch cfg-state :cfg-state-watch
    (fn [_ _ _ new-state]
      (save-config new-state)))
  (def get-userconfig #(deref cfg-state))
  (def alter-userconfig! (partial swap! cfg-state))
  (def reset-userconfig! #(reset! cfg-state (default-config))))
