# Notebook Explorer [![GitHub Actions status |pink-gorilla/gorilla-explore](https://github.com/pink-gorilla/gorilla-explore/workflows/CI/badge.svg)](https://github.com/pink-gorilla/gorilla-explore/actions?workflow=CI)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/gorilla-explore.svg)](https://clojars.org/org.pinkgorilla/gorilla-explore)

- End Users: this project is not for you.
- This project is used in [Notebook](https://github.com/pink-gorilla/notebook).
- ui and ring-handler to load and save notebook
- notebook explorer ui
- notebook discovery.

# notebook index

The indexer goes through selected github user repos and gists, and searches for .cljg files. Meta data will be extracted and the notebook is added to the index. 

```
lein build-index
```

- Notebooks that have part of the path "broken" will not be included.
- After the index is built, the new index has to be pushed to github.

# UI demo

```
lein webly watch
```

Open in browser: http://localhost:8000/


# components

- **explorer**: the notebook explorer web handler, and its web frontend.
- **document**: a loader and displayer for notebooks (or other content) powered by files / github repo / github gist.
- **save-dialog**: use it for save and save-as operations. Gets a storage, returns a storage. 
