# Project Notes

This repository is for a Keycloak presentation generated from AsciiDoc sources.

## Slide Structure

- `slides/main.adoc` is the root for the Reveal.js slide deck.
- `slides/lab_book.adoc` is the root for the lab/demo book.
- Main deck chapters live in `slides/chapters/*.adoc` and are included from `slides/main.adoc`.
- Lab/demo material lives in `slides/labs/*.adoc` and is included from `slides/lab_book.adoc`.
- The copied functional-programming chapter content is scaffolding only and can be replaced for the Keycloak deck.

## Rendering

Run commands from `slides/` or use the script there:

```sh
./setup-docs.sh
```

This renders:

- the Reveal.js deck from `main.adoc`
- the lab/demo book from `lab_book.adoc` using `asciidoctor -b html5`

To also render a PDF with Decktape:

```sh
./setup-docs.sh --decktape
```

The script uses Docker images `asciidoctor/docker-asciidoctor` and `astefanutti/decktape`.

## Visual Conventions

- Styles are in `slides/styles/`.
- The deck uses Reveal.js via `asciidoctor-revealjs`.
- Primary chapter/title slides should use `iStock-1181695869.jpg` as a full-cover background:

```adoc
[.chapter]
== Chapter Title
image::iStock-1181695869.jpg[background, size=cover]
```

- Secondary/subchapter divider slides should use the same `.chapter` role, a level-three heading, and `iStock-1075835580.jpg` as the full-cover background:

```adoc
[.chapter]
=== Subchapter Title
image::iStock-1075835580.jpg[background, size=cover]
```

- Normal slides use `=== Slide Title`.
- Demo slides use the standard two-column `legolab.jpg` pattern; only the title and right-column text should vary:

```adoc
[.columns]
=== Demo: Title

[.column]
--
image::legolab.jpg[]
--

[.column]
--
Demo description text.
--
```

- The existing CSS supports `.chapter`, `.notitle`, `.columns`, `.small`, `.project`, `.folder`, and `.package` conventions.
