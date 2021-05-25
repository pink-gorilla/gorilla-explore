(ns pinkgorilla.document.save-dialog.storage-type.file
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [re-frame.core :as rf]
   [re-com.core :refer [input-text radio-button]]))

(defn file [state change!]
  (let [config (rf/subscribe [:explorer/config])]
    (fn [state change!]
      (when-let [repositories (get-in @config [:server :roots])]
        [:div
         [:div
          [:h3.text-blue-600 "File Directory"]
            ;[:p (pr-str repositories)]
          (doall (map (fn [[name path]]
                        ^{:key name}
                        [radio-button
                         :label      name
                         :value       name
                         :model       (:explorer-root @state)
                         :on-change   #(do (change! :explorer-root name)
                                           (change! :path (str path "/")))])
                      repositories))]]))))

(defn filename [state change!]
  [:div
   [:div.flex.flex-row
    [:h3.w-16 "Path"]
    [:input.w-64 {;; blur does not work - prevents the click
            ;; :on-blur     #(dispatch [:save-dialog-cancel])
            ;; :on-mouse-down #(dispatch [:save-dialog-cancel])
                  :type      "text"
                  :value     (:path @state)
                  :on-change #(change! :path (-> % .-target .-value))}]]
   [:div.flex.flex-row
    [:h3.w-16 "Name"]
    [:input.w-64 {;; blur does not work - prevents the click
            ;; :on-blur     #(dispatch [:save-dialog-cancel])
            ;; :on-mouse-down #(dispatch [:save-dialog-cancel])
                  :type      "text"
                  :value     (:name @state)
                  :on-change #(change! :name (-> % .-target .-value))}]]
   [:div.flex.flex-row
    [:h3.w-16 "Full"]
    [:span (:filename @state)]]])