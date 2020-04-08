(ns fin.views.layout
  (:use [hiccup.page :only (html5)]))

(defn application [title & content]
  (html5 [:head
         [:title title]
         [:body
          [:div {:class "container"} content]]]))
