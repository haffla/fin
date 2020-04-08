(ns fin.views.contents)

(defn index []
  [:div {:id "content"}
   [:h1 "Fuck Hiccup"]])

(defn not-found []
  [:div
   [:h1 "Page Not Found"]])
