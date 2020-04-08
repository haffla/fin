(ns fin.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [ring.util.response :refer [redirect]]
            [fin.views.layout :as layout]
            [fin.views.contents :as contents])
  (:gen-class))

(def secret-password (or (System/getenv "SECRET_PASSWORD") "secret"))
(def user-id "doesntmatterwhat")

(defroutes app-routes
  ; HOME PAGE
  (GET "/" {session :session}
    (if (= user-id (:user-id session))
      (layout/application "Home" (contents/index))
      (redirect "/login")))
  ; LOGIN
  (POST "/session" {{password "password"} :form-params session :session}
    (if (= password secret-password)
      (-> (redirect "/")
          (assoc-in [:session :user-id] user-id))
      (layout/application "PW FALSCH" (contents/login))))
  ; LOGIN PAGE
  (GET "/login" _ (layout/application "Login" (contents/login)))
  ; LOGOUT
  (DELETE "/session" {session :session}
    (-> (redirect "/login")
        (assoc :session nil)))

  (route/not-found (layout/application "Page Not Found" (contents/not-found))))

; (defn wrap-auth [handler]
;   (fn [{session :session :as request}]
;     (do (println session)
;         (if-let [user-id (:user-id session)]
;           (handler request)
;           (handler request)))))

(def app
  (-> app-routes
      (wrap-defaults (assoc-in
                      site-defaults
                      [:session :store]
                      (cookie-store {:key (or (System/getenv "SESSION_STORE_SECRET") "SUPERSECRE777777")})))))

(defn start [port]
  (ring/run-jetty app {:port port
                       :join? false}))

(defn -main []
  (let [port (Integer. (or (System/getenv "PORT") "3000"))]
    (start port)))
