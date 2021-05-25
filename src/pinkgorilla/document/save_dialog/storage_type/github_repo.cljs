(ns pinkgorilla.document.save-dialog.storage-type.github-repo
  (:require
   [taoensso.timbre :refer-macros [info error]]
   [reagent.core :as r]
   [re-frame.core :refer [subscribe]]
   [re-com.core :refer [input-text radio-button]]))

(defn github [state change!]
  [:div.m-2.mt-5.pt-3
   [input-text
    :model           (:user @state)
    :width            "300px"
    :placeholder      "github user name"
    :on-change        #(change! :user %)
    :disabled?        (not (contains? #{:repo :gist} (:storage-type @state)))]

   [input-text
    :model           (:repo @state)
    :width            "300px"
    :placeholder      "github repo name"
    :on-change        #(change! :repo %)
    :disabled?        (not (contains? #{:repo} (:storage-type @state)))]

   [input-text
    :model           (:description @state)
    :width            "300px"
    :placeholder      "gist description"
    :on-change        #(change! :description %)
    :disabled?        (not (= :gist (:storage-type @state)))]])