(ns com.example.core
  (:require [org.msync.spring-boost :as boost]
            [compojure.core :refer :all]
            [compojure.route :refer [not-found]]
            [clojure.string]
            [clojure.pprint :refer [pprint]])
  (:import [java.util.logging Logger]
           [org.springframework.context ApplicationContext]))

(defonce logger (Logger/getLogger (str *ns*)))

(defroutes app
  "Root hello-world GET endpoint, and another echo end-point that handles both GET and POST.
  The :body entry in the request-map comes in either as a map for JSON requests, or as a String
  for other types."
  (GET "/" [:as {query-string :query-string}]
    (str "<h1>Hello World.</h1>"
         (if-not (clojure.string/blank? query-string) (str "We received a query-string " query-string))))
  (GET "/echo/:greeter" [greeter]
    {:status 200
     :headers {:content-type "application/json"}
     :body {:greeting (str "Hello, " greeter)}})
  (POST "/echo/:greeter" [greeter :as request]
    {:status 200
     :headers {:content-type "application/json"}
     :body {:greetings (str "Hello, " greeter)
            :echo (:body request)}})
  (not-found "<h1>Page not found</h1>"))

(defn web-socket-handler [session]
  (pprint session)
  ;; Use the session as you wish - to create session-specific handlers
  (fn [^String message]
    (str "*Hello*, " (.toUpperCase message))))

(defn main
  "Set this as your entry-point for the Clojure code in your spring-boot app.
  Gets the ApplicationContext object as an argument - which you are free to ignore or use."
  [^ApplicationContext application-context]

  (.info logger (str "[spring-clj] Initializing clojure app..."))
  (boost/set-handler! app)
  (boost/set-websocket-handler! web-socket-handler))

(comment
  (require '[org.msync.spring-boost.application-context :as ac])
  (ac/get-application-context)
  (def boost-configuration (get components "boostConfiguration"))
  (map str (.getMethods (class boost-configuration)))
  (.getMethod (class boost-configuration) "setInitSymbol" (into-array Class [String]))
  (ac/beans-with-annotation org.springframework.stereotype.Component)
  (->> (ac/beans-with-annotation org.springframework.stereotype.Component)
       vals
       (map class)))