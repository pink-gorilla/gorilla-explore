# Notebook Explorer [![GitHub Actions status |pink-gorilla/gorilla-explore](https://github.com/pink-gorilla/gorilla-explore/workflows/CI/badge.svg)](https://github.com/pink-gorilla/gorilla-explore/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/gorilla-explore.svg)](https://clojars.org/org.pinkgorilla/gorilla-explore)

- This project is used in [Notebook](https://github.com/pink-gorilla/gorilla-notebook) and [Goldly](https://github.com/pink-gorilla/goldly).
- Tools to explore (and load) the universe of Pink Gorilla notebooks.
- End Users: this project is not for you.

# notebook index

The indexer goes through selected github user repos and gists, and searches for .cljg files. Meta data will be extracted and the notebook is added to the index. 

```
lein build-index
```

- Notebooks that have part of the path "broken" will not be included.
- After the index is built, the new index has to be pushed to github.

# UI demo

The demo runs a webserver on port 8000 with explorer ui with demos.

```
lein demo watch
```

Open in browser: http://localhost:8000/


# components

- **explorer**: the notebook explorer web handler, and its web frontend.
- **document**: a loader and displayer for notebooks (or other content) powered by files / github repo / github gist.
- **save-dialog**: use it for save and save-as operations. Gets a storage, returns a storage. 

# Ideas

- A Full search on github gets 858 hits:
https://github.com/search?p=4&q=%22gorilla-repl.fileformat+%3D+1%22+extension%3Aclj&type=Code
unfortunately github api has blocked search without referring to a repo.

