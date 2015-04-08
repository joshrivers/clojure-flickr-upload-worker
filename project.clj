(defproject flickr-upload-worker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
  	[com.cemerick/bandalore "0.0.6"]
    [org.clojure/data.json "0.2.5"]
    [com.flickr4java/flickr4java "2.12"]]
  :main ^:skip-aot flickr-upload-worker.core
  :target-path "target/%s"
  :jvm-opts ["-Xmx8G"]
  :profiles {:uberjar {:aot :all}})
