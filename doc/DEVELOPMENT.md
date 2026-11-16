# Local development

## Requirements

- JDK 21 or newer.
- Clojure CLI (`clojure` / `clj`).
- A browser.

Run commands from the project root. Dependencies download on the first run.

## Development service

```sh
clojure -M:cljs:dev:serve
```

Open http://localhost:10555. Figwheel uses port 9500 for browser REPL
communication and writes generated assets to `dev-target/`.

Save `.cljs`, `.cljc` or CSS files to update the browser automatically.
Compilation errors appear in the browser and terminal; the last working
version stays active until they are fixed. Stop with `Ctrl+C`.

To use another application port:

```sh
PORT=10556 clojure -M:cljs:dev:serve
```

## REPL

```sh
clj -M:cljs:dev
```

In the `user` namespace:

```clojure
(go)        ; start backend and Figwheel
(cljs-repl) ; connect to the open browser tab
```

Enter `:cljs/quit` to return to Clojure. Use `(reset)` to restart both services
or `(stop)` to stop them. After backend edits, reload the affected namespaces
before `(reset)`, or restart the process.

## Emacs and CIDER

Install `cider` and `clojure-mode` with `M-x package-install`. Use a CIDER
release compatible with `cider-nrepl` 0.64.1, and ensure Emacs can find
`clojure` and `java` on its PATH.

### Connect to nREPL

Stop any development instance using ports 10555/9500, then run:

```sh
clojure -M:cljs:dev:cider
```

This starts nREPL with CIDER and Piggieback on `127.0.0.1:7888`.
Append `--port 7889` to choose another port.

Open a project `.clj` file, run `M-x cider-connect-clj`, and connect to
`localhost:7888`. In the REPL:

```clojure
(require 'user)
(user/go)
```

Open http://localhost:10555. Saving files triggers hot reload as usual.

### Add a ClojureScript REPL

Evaluate this Emacs Lisp with `M-:` or add it to your Emacs configuration:

```elisp
(with-eval-after-load 'cider
  (cider-register-cljs-repl-type 'kugelmass "(user/cljs-repl)"))
```

From the Clojure REPL, run `M-x cider-connect-sibling-cljs` and select
`kugelmass`. Keep the browser tab open.

CIDER evaluates `.clj` code on the JVM and `.cljs` code in the browser:

- `C-x C-e`: evaluate the preceding expression.
- `C-c C-k`: load the current buffer.

After backend edits, load the `.clj` buffer, run `(user/reset)` in the Clojure
REPL, then reconnect the ClojureScript REPL.

To shut down, run `(user/stop)`, disconnect CIDER, and press `Ctrl+C` in the
nREPL terminal.

### Start nREPL from Emacs

For a Clojure REPL, set the aliases in the project buffer:

```elisp
(setq-local cider-clojure-cli-aliases ":cljs:dev")
```

Run `M-x cider-jack-in-clj`, choose `clojure-cli` if prompted, then evaluate
`(user/go)`. For browser evaluation, use the explicit nREPL and sibling
connection setup above.

References: [connections](https://docs.cider.mx/cider/usage/managing_connections.html),
[middleware](https://docs.cider.mx/cider/basics/middleware_setup.html),
[custom REPLs](https://docs.cider.mx/cider/cljs/configuration.html).

## Tests

JVM and shared tests:

```sh
clojure -M:test
```

ClojureScript and shared tests in a browser (port 9501):

```sh
clojure -M:cljs:dev:cljs-test -m kugelmass.test-runner
```

Both commands exit nonzero on failure. To use headless Firefox:

```sh
clojure -M:cljs:dev:cljs-test \
  -fwo '{:launch-js ["firefox" "--headless" "--no-remote" :open-url]}' \
  -m kugelmass.test-runner
```

Use a dedicated profile if Firefox is already running.

## Production build

```sh
clojure -T:build uber
java -jar target/kugelmass.jar
```

Open http://localhost:10555. The JAR includes the optimized frontend and
backend; only Java is required at runtime. Set `PORT` to change the port.

To compile only the frontend:

```sh
clojure -T:build frontend
```

Output: `target/frontend/public/js/compiled/kugelmass.js`. To serve it without
building a JAR:

```sh
clojure -Sdeps '{:paths ["src/clj" "src/cljc" "target/frontend" "resources"]}' -M:run
```

Stop development before removing generated artifacts:

```sh
clojure -T:build clean
```
