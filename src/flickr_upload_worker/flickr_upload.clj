(ns flickr-upload-worker.flickr-upload
	(:use flickr-upload-worker.config)
	(:use flickr-upload-worker.mime-type)
	(:import com.flickr4java.flickr.Flickr)
	(:import com.flickr4java.flickr.REST)
	(:import com.flickr4java.flickr.uploader.UploadMetaData)
	(:import com.flickr4java.flickr.RequestContext)
	(:import com.flickr4java.flickr.auth.Auth)
	(:import org.scribe.model.Verifier)
	(:import java.io.File))


(def flickrApiKey (get (get-userconfig) :flickrApiKey))
(def flickrApiSharedSecret (get (get-userconfig) :flickrApiSharedSecret))
(def flickr (new Flickr flickrApiKey, flickrApiSharedSecret (new REST)))
(def extendedUseToken (get (get-userconfig) :extendedUseToken))
(def extendedUseSecret (get (get-userconfig) :extendedUseSecret))

(if (= extendedUseToken "unset") 
	(do 
		(def authInterface (. flickr getAuthInterface))
		(def authToken (. authInterface getRequestToken))
		(def authorizationUrl (. authInterface getAuthorizationUrl authToken com.flickr4java.flickr.auth.Permission/WRITE))
		(println authorizationUrl)
		(println "Please type in token: ")
		(def tokenKey (read-line))
		(println "Token is: " tokenKey)
		(def requestToken (. authInterface getAccessToken authToken (new Verifier tokenKey)))
		(alter-userconfig! assoc :extendedUseToken (. requestToken getToken))
		(alter-userconfig! assoc :extendedUseSecret (. requestToken getSecret))
		(println "Flickr authentication saved.")
	)
	(println "Flickr authentication available in configuration.")
)

(defn flickr-upload
	"Upload a file to flickr"
	[fullPath]
	;(def  "/Users/josh/Dropbox/Camera Uploads/2014-02-06 17.28.46.png")
	(println "Uploading: " fullPath)
	(def mimeType (ext-mime-type fullPath))

	(def auth (new Auth))
	(. auth setPermission com.flickr4java.flickr.auth.Permission/WRITE)
	(. auth setToken extendedUseToken)
	(. auth setTokenSecret extendedUseSecret)
	(. (RequestContext/getRequestContext) setAuth auth)
	(def metaData (new UploadMetaData))
	(. metaData setFilename fullPath)
	(def fileHandle (new File fullPath))
	(def uploader (. flickr getUploader))
	(def photoId (. uploader upload fileHandle metaData))
)




