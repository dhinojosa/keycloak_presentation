# AGENTS.md

Guidance for AI agents working on this repository.

This repository is a Keycloak technical presentation generated from AsciiDoc
sources. It contains a Reveal.js slide deck and a lab/demo book. Keep the deck
pragmatic, direct, technically careful, and instructor friendly.

## Core Rules

- If the user says "no edits" or "review only", do not modify files.
- Read the target file before editing; the user may have made manual edits.
- Preserve the owner's presentation voice: concise, practical, and precise.
- Push back when an example would teach the wrong security or design habit.
- Do not introduce Scala or functional-programming-specific conventions copied
  from the source scaffold unless the user explicitly asks for them.
- The copied functional-programming chapter content is scaffolding only and can
  be replaced for the Keycloak deck.

## Repository Structure

- `slides/main.adoc` is the root for the Reveal.js slide deck.
- `slides/lab_book.adoc` is the root for the lab/demo book.
- Main deck chapters live in `slides/chapters/*.adoc` and are included from
  `slides/main.adoc`.
- Lab/demo material lives in `slides/labs/*.adoc` and is included from
  `slides/lab_book.adoc`.
- Styles live in `slides/styles/`.
- Images live in `slides/images/`.

## Rendering and Validation

Run commands from `slides/`:

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

The script uses Docker images `asciidoctor/docker-asciidoctor` and
`astefanutti/decktape`.

After slide edits, render when practical. The normal render validates
Asciidoctor structure, includes, and generated HTML. It does not guarantee that
slides are visually balanced or free from overflow. For dense slides, especially
slides with multiple code blocks, diagrams, tables, or bottom admonitions,
inspect the rendered HTML or PDF visually when possible.

Passing the render is not the same as passing the presentation. If content is
cut off or crowded, split it into focused slides instead of shrinking text or
weakening the example.

Use `./setup-docs.sh --decktape` only when a PDF render is explicitly requested
or visual PDF output must be validated.

## Slide Voice

- This is a technical workshop, not a marketing deck.
- Assume experienced developers. Do not over-explain basic syntax or common web
  security terms, but define new protocol or Keycloak concepts before relying on
  them.
- Prefer small claims backed by nearby examples over long explanatory prose.
- Use concise bullets and precise technical language.
- Avoid motivational filler and vague meta-commentary.
- Avoid forward references unless the slide explicitly says a later chapter will
  name or deepen the concept.
- Use examples that teach the shape of the idea, not just a product trick.
- When discussing security, be concrete about trust boundaries, token handling,
  browser behavior, and operational consequences.

## Slide Structure

- Normal slides use `=== Slide Title`.
- Use title case for slide and chapter headings: capitalize meaningful words and
  keep short joiners such as `and`, `or`, `to`, `of`, `in`, `for`, `with`,
  `over`, `into`, and `vs` lowercase unless they start the title. Preserve
  protocol acronyms such as OIDC, JWT, JWKS, OAuth, SAML, SASL, and MFA. For
  example, prefer `Password and Identity Mishaps`, not `Password and identity
  Mishaps`.
- Keep slides clean and deliberate. Every bullet should earn its place.
- Prefer tables only when they clarify contrast.
- If a term is new, define it before using it in later slides.
- When a code block directly demonstrates a bullet point, chain it to that
  bullet with `+`.
- When a slide has one standalone code block at the bottom that demonstrates the
  whole slide, leave it as its own block.
- Put admonitions such as `NOTE:` and `WARNING:` near the bottom of a slide when
  possible. They should read like important footnotes, not interrupt the main
  teaching flow.
- Use `NOTE:` for short one-line notes. Avoid large admonition blocks unless the
  content is genuinely multi-paragraph.
- Use bold labels for before/after or paired comparisons, for example
  `* *Before:* ...` and `* *After:* ...`.
- Diagram labels should use deliberate title-style casing when presenting named
  concepts, for example `Authorization Code, Token Endpoint, Resource Server`.

## Code and Technical Examples

- Keep examples short, but real enough to teach.
- Prefer complete protocol or configuration fragments over abstract pseudocode.
- Show the full form before shorthand when shorthand hides the lesson.
- Prefer before/after examples when teaching refactoring, migration, or safer
  configuration.
- Keep code close to the bullet it supports.
- Use compact examples with strong naming.
- Avoid comments inside code snippets when a slide bullet can explain the point.
- Clearly mark experimental features, previews, or version-specific behavior.
- For Keycloak examples, avoid insecure shortcuts unless the point of the slide
  is to explain why they are unsafe.

## Visual Conventions

- The deck uses Reveal.js via `asciidoctor-revealjs`.
- The existing CSS supports `.chapter`, `.notitle`, `.columns`, `.small`,
  `.project`, `.folder`, and `.package` conventions.
- Primary chapter/title slides should use `iStock-1181695869.jpg` as a
  full-cover background:

```adoc
[.chapter]
== Chapter Title
image::iStock-1181695869.jpg[background, size=cover]
```

- Secondary/subchapter divider slides should use the same `.chapter` role, a
  level-three heading, and `iStock-1075835580.jpg` as the full-cover background:

```adoc
[.chapter]
=== Subchapter Title
image::iStock-1075835580.jpg[background, size=cover]
```

- Demo slides use the standard two-column `legolab.jpg` pattern; only the title
  and right-column text should vary:

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

## Lab and Demo Material

- Avoid creating lab material unless the user explicitly asks for it.
- Demo and lab signifier slides are fine without supporting handout material.
- Labs should reinforce the slide sequence, not introduce unrelated concepts.
- Keep instructions direct and task oriented.
- Prefer small, complete exercises over large open-ended assignments.
- State the goal of the lab plainly.
- Give enough setup context for the learner to start without hunting.
- Keep steps ordered and executable.
- Prefer concrete file paths, package names, commands, URLs, realm names, client
  names, and environment variables.
- Avoid long explanations inside steps; use short notes before or after a step.
- Labs should have a visible feedback loop, preferably a command, browser check,
  token inspection, or test to run.
- Use existing project conventions, packages, and build tools.
- Do not add unrelated abstractions just to make the lab feel larger.
- If a lab is about refactoring or hardening configuration, preserve behavior
  first and then improve shape.
- Keep optional extensions clearly separate from required work.

## Asciidoctor Formatting

- Keep Asciidoctor syntax simple and predictable.
- Use code blocks for commands and snippets.
- Use `[source,java]`, `[source,kotlin]`, `[source,bash]`, `[source,json]`,
  `[source,yaml]`, etc. for code blocks.
- Use `+` continuation carefully so related blocks stay attached to bullets or
  steps.
- If a code block directly supports a bullet or numbered step, chain it with
  `+`.
- Keep admonitions near the bottom of a section when possible.
