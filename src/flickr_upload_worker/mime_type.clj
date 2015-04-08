(ns flickr-upload-worker.mime-type
  "Utility functions for determining the mime-types files."
  (:require [clojure.string :as str]))

(def ^{:doc "A map of file extensions to mime-types."}
  default-mime-types
  {"avi"      "video/x-msvideo"
   "bmp"      "image/bmp"
   "gif"      "image/gif"
   "jpe"      "image/jpeg"
   "jpeg"     "image/jpeg"
   "jpg"      "image/jpeg"
   "mov"      "video/quicktime"
   "m4v"      "video/mp4"
   "mp3"      "audio/mpeg"
   "mp4"      "video/mp4"
   "mpe"      "video/mpeg"
   "mpeg"     "video/mpeg"
   "mpg"      "video/mpeg"
   "oga"      "audio/ogg"
   "ogg"      "audio/ogg"
   "ogv"      "video/ogg"
   "pdf"      "application/pdf"
   "png"      "image/png"
   "qt"       "video/quicktime"
   "tif"      "image/tiff"
   "tiff"     "image/tiff"
   "wmv"      "video/x-ms-wmv"})

(defn- filename-ext
  "Returns the file extension of a filename or filepath."
  [filename]
  (if-let [ext (second (re-find #"\.([^./\\]+)$" filename))]
    (str/lower-case ext)))

(defn ext-mime-type
  "Get the mimetype from the filename extension. Takes an optional map of
  extensions to mimetypes that overrides values in the default-mime-types map."
  ([filename]
     (ext-mime-type filename {}))
  ([filename mime-types]
     (let [mime-types (merge default-mime-types mime-types)]
       (mime-types (filename-ext filename)))))