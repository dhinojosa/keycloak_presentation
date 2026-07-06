# OIDC Flow SVG Assets

This directory contains a fixed-size SVG sequence for the OIDC authorization
code flow.

Each numbered SVG is a complete `1280 x 720` image designed to be shown as one
Reveal.js slide:

```adoc
[.notitle]
=== OIDC Login Flow

image::keycloak-flows/oidc/01-oidc-start.svg[width=100%]
```

The animation effect comes from slide-to-slide state changes. There is no
JavaScript animation, SMIL animation, or frame-by-frame timing.

The `sprites/` directory contains source versions of the key, lock, and JWT
graphics. The final numbered SVGs inline equivalent shapes in their own `<defs>`
sections so they remain portable when exported to PDF or opened outside this
repository.

Teaching model:

- The private signing key stays inside Keycloak.
- The public verification key represents the JWKS key used by the application to
  verify token signatures.
- The public key graphic may move toward the application, but the token is not
  being encrypted or decrypted in this diagram.

To add another state, copy one numbered SVG, change the title text and the
transforms for the moving groups, and include it from an Asciidoctor slide with
`image::keycloak-flows/oidc/name.svg[width=100%]`.
