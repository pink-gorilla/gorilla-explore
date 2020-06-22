(ns demo.config)

(def config-server
  {:exclude       #{".git" ".svn"}
   :roots {:gorilla-notebook "../gorilla-notebook/notebooks"}})

(def config-client
  {:repositories
   [{:name "local" :url "/explorer/index"}
    {:name "public" :url "https://raw.githubusercontent.com/pink-gorilla/gorilla-explore/master/resources/list.json"}]})

