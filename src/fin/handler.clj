(ns fin.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [ring.util.response :refer [redirect]]
            [fin.views.layout :as layout]
            [fin.views.contents :as contents])
  (:gen-class))

(def secret-password (or (System/getenv "SECRET_PASSWORD") "secret"))
(def user-id "doesntmatterwhat")

(defroutes public-routes
  ; LOGIN
  (POST "/session" {{password "password"} :form-params}
    (if (= password secret-password)
      (-> (redirect "/")
          (assoc-in [:session :user-id] user-id))
      (layout/application "PW FALSCH" (contents/login))))
  ; LOGIN PAGE
  (GET "/login" _
    (layout/application "Login" (contents/login))))

(defroutes app-routes
  ; HOME PAGE
  (GET "/" _
    (layout/application "Home" (contents/index)))
  (POST "/file" {{{filename :filename tempfile :tempfile size :size} :file} :params}
    (do
      (io/copy tempfile (io/file "resources" "public" filename))
      (redirect "/")))
  ; LOGOUT
  (DELETE "/session" _
    (-> (redirect "/login")
        (assoc :session nil)))

  (route/not-found (layout/application "Page Not Found" (contents/not-found))))

(defn wrap-auth [handler]
  (fn [{session :session :as request}]
    (if (= user-id (:user-id session))
      (handler request)
      (redirect "/login"))))

(def s-defaults
  (-> site-defaults
      (assoc-in
       [:session :store]
       (cookie-store {:key (or (System/getenv "SESSION_STORE_SECRET") "SUPERSECRE777777")}))
      (assoc-in
       [:security :anti-forgery]
       false)))

(def wrapped-app-routes
  (wrap-auth app-routes))

(def app
  (-> (routes public-routes wrapped-app-routes)
      (wrap-defaults s-defaults)))

(defn start [port]
  (ring/run-jetty app {:port port
                       :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "3000"))]
    (start port)))
