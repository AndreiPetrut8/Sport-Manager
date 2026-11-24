#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$ROOT_DIR"

# Load local secrets (DB/broker passwords, dev login) from .env, if present.
if [ -f "$ROOT_DIR/.env" ]; then
  set -a
  # shellcheck disable=SC1091
  . "$ROOT_DIR/.env"
  set +a
fi

if [ -d "$ROOT_DIR/.jdk" ]; then
  JDK_DIR="$(find "$ROOT_DIR/.jdk" -mindepth 1 -maxdepth 1 -type d | head -n 1)"
  if [ -n "${JDK_DIR:-}" ]; then
    export JAVA_HOME="$JDK_DIR"
    export PATH="$JAVA_HOME/bin:$PATH"
  fi
fi

export MAVEN_USER_HOME="$ROOT_DIR/.m2-home"
MVN_CMD=("./mvnw" "-B" "-Dmaven.repo.local=$ROOT_DIR/.m2-home/repository")

# Services depend on common-contracts. When a service is run alone with -pl,
# Maven resolves it from the local repo, so install the shared module (and the
# parent POM via -am) first.
install_contracts() {
  "${MVN_CMD[@]}" -q -pl common-contracts -am install -DskipTests
}

run_service() {
  install_contracts
  "${MVN_CMD[@]}" -pl "$1" spring-boot:run
}

case "${1:-help}" in
  build)
    "${MVN_CMD[@]}" clean package -DskipTests
    ;;
  test)
    "${MVN_CMD[@]}" verify
    ;;
  core)
    run_service core-service
    ;;
  realtime)
    run_service realtime-service
    ;;
  mcp)
    run_service mcp-server
    ;;
  agent)
    run_service tactical-agent
    ;;
  client)
    # Serves the static HTML/JS dashboard on http://localhost:3000
    exec python3 -m http.server 3000 --directory "$ROOT_DIR/web-client/public"
    ;;
  *)
    echo "Usage: ./run-dev.sh {build|test|core|realtime|mcp|agent|client}"
    ;;
esac
