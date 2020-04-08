(ns fin.views.contents
  (:use [hiccup.form]
        [ring.util.anti-forgery]))

(defn index []
  [:div {:id "content"}
   [:h1 "Home"]
   (form-to [:delete "/session"]
            (anti-forgery-field)
            [:button {:type "submit"} "Logout"])])

(defn not-found []
  [:div
   [:h1 "Page Not Found"]])

(defn login []
  [:div
   (form-to [:post "/session"]
            [:input {:name "password"}]
            (anti-forgery-field)
            [:button {:type "submit"} "Submit"])])

