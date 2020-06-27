# Ping Gorilla Explore [![GitHub Actions status |pink-gorilla/gorilla-explore](https://github.com/pink-gorilla/gorilla-explore/workflows/CI/badge.svg)](https://github.com/pink-gorilla/gorilla-explore/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/gorilla-explore.svg)](https://clojars.org/org.pinkgorilla/gorilla-explore)


- Tools to explore (and load) the universe of Pink Gorilla documents.
- This project is used in [Notebook](https://github.com/pink-gorilla/gorilla-notebook) and [Goldly](https://github.com/pink-gorilla/goldly).


# notebook index

The indexer goes through selected github user repos and gists, and searches for .cljg files. Meta data will be extracted and the notebook is added to the index. 

```
lein build-index
```

- Notebooks that have part of the path "broken" will not be included.
- After the index is rebuilt, the new indices have to be pushed to github.

# UI demo

The demo runs a webserver on port 8001 and has the explorer on path "/explorer".

```
lein demo
```

# components

- **explorer**: the notebook explorer web handler, and its web frontend.
- **document**: a loader and displayer for notebooks (or other content) powered by files / github repo / github gist.
- **save-dialog**: use it for save and save-as operations. Gets a storage, returns a storage. 

# Ideas

- auto generated tags :clj :cljs :cljs-only :clj-only
this is useful for filtering in the notebook explorer
better: {:meta {:kernels [clj cljs bash]}}

- A Full search on github gets 858 hits:
https://github.com/search?p=4&q=%22gorilla-repl.fileformat+%3D+1%22+extension%3Aclj&type=Code
unfortunately github api has blocked search without referring to a repo.

