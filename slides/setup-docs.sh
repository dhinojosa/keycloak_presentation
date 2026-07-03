#!/bin/zsh

set -euo pipefail

cd "$(dirname "$0")"

basename=$(basename "$PWD")
image="asciidoctor/docker-asciidoctor"
revealjsdir="https://cdn.jsdelivr.net/npm/reveal.js"
run_decktape=false

for arg in "$@"; do
  case "$arg" in
    --decktape) run_decktape=true ;;
    *) echo "Unknown option: $arg"; exit 1 ;;
  esac
done

docker run --rm \
  -v "$PWD":/documents \
  "$image" \
  asciidoctor-revealjs \
    -a revealjsdir="$revealjsdir" \
    -a customcss=styles/custom.css \
    -a highlightjs-theme=styles/color-brewer.css \
    -o "$basename.html" \
    main.adoc

docker run --rm \
  -v "$PWD":/documents \
  "$image" \
  asciidoctor \
    -b html5 \
    -o lab_book.html \
    lab_book.adoc

if $run_decktape; then
  docker run --rm \
    -v "$PWD":/slides \
    astefanutti/decktape \
    --size '1920x1080' \
    --pause 1000 \
    reveal \
    "file:///slides/${basename}.html" \
    "/slides/${basename}.pdf"
fi
