(ns pinkgorilla.explore.demo
  (:require
   [clojure.string]
   [clojure.pprint]
   [pinkgorilla.exolore.github-helper  :refer [user-repos tags user-gists specific-gist search-code me]]
   ))


(comment
  
  ;; REPO
  ;; http://viewer.gorilla-repl.org/view.html?source=github&user=<>&repo=<>&path= 
  (->> (vec (user-repos "awb99"))
       (clojure.pprint/print-table [:created_at :id :name :has_wiki]))
 ;; :html_url
 ;; :description
 ;; node_id
 ;; :public
 ;; :updated_at
 ;; files
 ;; raw_url
 ;; git_push_url
  
    ;; REPO-TAGS
  (tags "ribelo" "trennwand")
  
 (map :html_url (user-repos "awb99" {:oauth-token "519fc32186567847a356a6a46277158958866bcd"
                                     :per-page 2
                                     :type "private"}))
    
  
  ;; GIST
  ; https://gist.github.com/awb99/55b101d84d9b3814c46a4f9fbadcf2f8
  (->> (user-gists "awb99")
       (clojure.pprint/print-table [:created_at :description :files])
       ;(first)    
       )
  
  (->> (user-gists {:oauth-token "21cfc5276900feff73e5edf77939dcfb6e13e74e"})
       (clojure.pprint/print-table [:created_at :description :files])
       )
  
  ;; SEARCH-CODE
  
  
  ; (search-code "gorilla-repl"  {:in "file" :language "js"}))
  ; (search-code "addClass" {:in "file" :language "js"}))
  
  (->> ;(search-code "gorilla-repl"  {:in "file" :language "clj" :user "ribelo"})
   (search-code "gorilla-repl"  {:in "file" :language "clj" :repo "ribelo/trennwand"})
   (:items)
   (clojure.pprint/print-table [:name :path]))


  (search-code "gorilla-repl fileformat = 1"
               {:language "clj"}
               {:oauth-token "21cfc5276900feff73e5edf77939dcfb6e13e74e"})

  
  ;; => ({:html_url "https://github.com/clojurewb/clojurewb", :description "Clojure Workbook", :archived false, :open_issues_count 0, :watchers 0, :ssh_url "git@github.com:clojurewb/clojurewb.git", :hooks_url "https://api.github.com/repos/clojurewb/clojurewb/hooks", :archive_url "https://api.github.com/repos/clojurewb/clojurewb/{archive_format}{/ref}", :keys_url "https://api.github.com/repos/clojurewb/clojurewb/keys{/key_id}", :forks_count 0, :languages_url "https://api.github.com/repos/clojurewb/clojurewb/languages", :git_url "git://github.com/clojurewb/clojurewb.git", :permissions {:admin true, :push true, :pull true}, :issue_comment_url "https://api.github.com/repos/clojurewb/clojurewb/issues/comments{/number}", :git_refs_url "https://api.github.com/repos/clojurewb/clojurewb/git/refs{/sha}", :clone_url "https://github.com/clojurewb/clojurewb.git", :contents_url "https://api.github.com/repos/clojurewb/clojurewb/contents/{+path}", :has_downloads true, :teams_url "https://api.github.com/repos/clojurewb/clojurewb/teams", :has_issues true, :disabled false, :issue_events_url "https://api.github.com/repos/clojurewb/clojurewb/issues/events{/number}", :license nil, :private false, :watchers_count 0, :collaborators_url "https://api.github.com/repos/clojurewb/clojurewb/collaborators{/collaborator}", :homepage nil, :git_commits_url "https://api.github.com/repos/clojurewb/clojurewb/git/commits{/sha}", :name "clojurewb", :releases_url "https://api.github.com/repos/clojurewb/clojurewb/releases{/id}", :milestones_url "https://api.github.com/repos/clojurewb/clojurewb/milestones{/number}", :svn_url "https://github.com/clojurewb/clojurewb", :node_id "MDEwOlJlcG9zaXRvcnkyMTI5ODY3ODY=", :merges_url "https://api.github.com/repos/clojurewb/clojurewb/merges", :compare_url "https://api.github.com/repos/clojurewb/clojurewb/compare/{base}...{head}", :stargazers_count 0, :tags_url "https://api.github.com/repos/clojurewb/clojurewb/tags", :statuses_url "https://api.github.com/repos/clojurewb/clojurewb/statuses/{sha}", :notifications_url "https://api.github.com/repos/clojurewb/clojurewb/notifications{?since,all,participating}", :open_issues 0, :has_wiki true, :size 72, :assignees_url "https://api.github.com/repos/clojurewb/clojurewb/assignees{/user}", :commits_url "https://api.github.com/repos/clojurewb/clojurewb/commits{/sha}", :labels_url "https://api.github.com/repos/clojurewb/clojurewb/labels{/name}", :forks_url "https://api.github.com/repos/clojurewb/clojurewb/forks", :contributors_url "https://api.github.com/repos/clojurewb/clojurewb/contributors", :updated_at "2019-10-10T11:35:35Z", :pulls_url "https://api.github.com/repos/clojurewb/clojurewb/pulls{/number}", :has_pages false, :default_branch "master", :language "Clojure", :comments_url "https://api.github.com/repos/clojurewb/clojurewb/comments{/number}", :id 212986786, :stargazers_url "https://api.github.com/repos/clojurewb/clojurewb/stargazers", :issues_url "https://api.github.com/repos/clojurewb/clojurewb/issues{/number}", :trees_url "https://api.github.com/repos/clojurewb/clojurewb/git/trees{/sha}", :events_url "https://api.github.com/repos/clojurewb/clojurewb/events", :branches_url "https://api.github.com/repos/clojurewb/clojurewb/branches{/branch}", :url "https://api.github.com/repos/clojurewb/clojurewb", :downloads_url "https://api.github.com/repos/clojurewb/clojurewb/downloads", :forks 0, :subscribers_url "https://api.github.com/repos/clojurewb/clojurewb/subscribers", :full_name "clojurewb/clojurewb", :blobs_url "https://api.github.com/repos/clojurewb/clojurewb/git/blobs{/sha}", :subscription_url "https://api.github.com/repos/clojurewb/clojurewb/subscription", :fork false, :deployments_url "https://api.github.com/repos/clojurewb/clojurewb/deployments", :has_projects true, :pushed_at "2019-10-10T11:35:33Z", :owner {:html_url "https://github.com/clojurewb", :gravatar_id "", :followers_url "https://api.github.com/users/clojurewb/followers", :subscriptions_url "https://api.github.com/users/clojurewb/subscriptions", :site_admin false, :following_url "https://api.github.com/users/clojurewb/following{/other_user}", :node_id "MDQ6VXNlcjU2MTkyNzgw", :type "User", :received_events_url "https://api.github.com/users/clojurewb/received_events", :login "clojurewb", :organizations_url "https://api.github.com/users/clojurewb/orgs", :id 56192780, :events_url "https://api.github.com/users/clojurewb/events{/privacy}", :url "https://api.github.com/users/clojurewb", :repos_url "https://api.github.com/users/clojurewb/repos", :starred_url "https://api.github.com/users/clojurewb/starred{/owner}{/repo}", :gists_url "https://api.github.com/users/clojurewb/gists{/gist_id}", :avatar_url "https://avatars0.githubusercontent.com/u/56192780?v=4"}, :git_tags_url "https://api.github.com/repos/clojurewb/clojurewb/git/tags{/sha}", :created_at "2019-10-05T11:17:37Z", :mirror_url nil})
  


  (user-repos "awb99")



  (tentacles.core/api-call :get
                           "authorizations"
                           nil
                           {:oauth-token "519fc32186567847a356a6a46277158958866bcd"})


  (tentacles.oauth/valid-auth? "6bf5e8c264c815aa951e" "519fc32186567847a356a6a46277158958866bcd")

  (me {:oauth-token "519fc32186567847a356a6a46277158958866bcd"})

  (username "519fc32186567847a356a6a46277158958866bcd")

  ;"X-RateLimit-Limit" "60", 
  ;"X-RateLimit-Remaining" "44", 
  ;"X-RateLimit-Reset" "1570715916"
  )
