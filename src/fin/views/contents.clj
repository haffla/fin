(ns fin.views.contents
  (:use [hiccup.form]
        [ring.util.anti-forgery]))

(defn index []
  [:div
   [:h1 "Home"]
   (form-to [:delete "/session"]
            (anti-forgery-field)
            [:button {:type "submit"} "Logout"])
   (form-to {:enctype "multipart/form-data"} [:post "/file"]
            (anti-forgery-field)
            (file-upload "file")
            [:button {:type "submit"} "Upload"])])

(defn not-found []
  [:div
   [:h1 "Page Not Found"]])

(defn login []
  [:div
   (form-to [:post "/session"]
            [:input {:name "password"}]
            (anti-forgery-field)
            [:button {:type "submit"} "Submit"])])

