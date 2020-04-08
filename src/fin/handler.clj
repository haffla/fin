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

(defroutes app-routes
  (GET "/" {session :session}
    (if (= "2222" (:user-id session))
      (layout/application "Home" (contents/index))
      (redirect "/login")))
  (GET "/login" {session :session}
    (let [session (assoc session :user-id "2222")]
      (-> (redirect "/")
          (assoc :session session))))
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
