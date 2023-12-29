# Start from a Java image. Shadow-CLJS outputs JavaScript, which can be run in any Node.js environment
# FROM clojure:openjdk-17-tools-deps-1.10.3.855-slim-buster
FROM node:alpine

# Create directories for your application and set it as workdir
WORKDIR /src

# Copy your project.clj, deps.edn, shadow-cljs.edn, etc. for downloading dependencies
COPY deps.edn shadow-cljs.edn package.json package-lock.json .

# Add your source directories and resources
COPY . .

# Install the necessary dependencies for Clojure
RUN apk add --no-cache openjdk11-jdk curl bash git

# Download and install Clojure
RUN curl -O https://download.clojure.org/install/linux-install-1.10.3.967.sh && chmod +x linux-install-1.10.3.967.sh && ./linux-install-1.10.3.967.sh

# Install Shadow-CLJS globally (-g) and other npm dependencies
RUN npm install -g shadow-cljs && npm install

# Compile Clojure backend first
# RUN clojure -M:uberjar

# Compile your Shadow-CLJS frontend
RUN npm run release

RUN clojure -P
RUN clojure -A:build -P

EXPOSE 3448 8020

RUN clojure -T:build uber

RUN cp /src/target/*-standalone.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
