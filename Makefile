run:
	npm run dev

repl:
	npx shadow-cljs cljs-repl app

release:
	npm run release

repl-server:
	clojure -A:dev

build:
	clojure -T:build uber

build-docker-and-run:
	docker build -t my-shadow-app .
	docker run my-shadow-app
