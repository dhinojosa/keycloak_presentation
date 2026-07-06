# Codex Spec: OIDC Flow SVG Diagram

Create a small SVG-based slide sequence that explains an OIDC authorization code
login flow using reusable SVG source sprites and slide-by-slide states.

This repository uses Asciidoctor Reveal.js, not Markdown slides. The generated
SVGs are image assets that should be included from `.adoc` chapter files with
`image::...[]`.

## Goal

Produce multiple fixed-size, self-contained SVG images. Each SVG represents one
meaningful state in the OIDC flow. Do not create frame-by-frame animation.
Instead, move objects between separate slide images to imply motion.

Example sequence:

- Slide 01: browser starts at the web application
- Slide 02: browser is redirected to Keycloak
- Slide 03: user authenticates with Keycloak
- Slide 04: browser returns to the application with an authorization code
- Slide 05: web server exchanges the code for tokens
- Slide 06: web server retrieves JWKS and verifies token signatures
- Slide 07: user receives an authenticated application session

## Repository Placement

Keep generated assets under `slides/images/keycloak-flows/oidc/`.

Create these reusable source sprite files:

```text
slides/images/keycloak-flows/oidc/sprites/key.svg
slides/images/keycloak-flows/oidc/sprites/lock.svg
```

The key and lock should be simple, clean SVG illustrations:

- `key.svg`: silver private signing key sprite
- `lock.svg`: silver public verification key sprite

Also create these final slide SVG assets:

```text
slides/images/keycloak-flows/oidc/01-oidc-start.svg
slides/images/keycloak-flows/oidc/02-redirect-to-keycloak.svg
slides/images/keycloak-flows/oidc/03-user-authenticates.svg
slides/images/keycloak-flows/oidc/04-authorization-code-returned.svg
slides/images/keycloak-flows/oidc/05-token-exchange.svg
slides/images/keycloak-flows/oidc/06-jwks-verification-key.svg
slides/images/keycloak-flows/oidc/07-session-created.svg
```

Create a short asset README:

```text
slides/images/keycloak-flows/oidc/README.md
```

Do not place these generated SVGs directly in `slides/`; that directory already
contains deck sources, generated output, styles, images, and lab material.

## Asciidoctor Reveal.js Integration

The SVGs should be included from an Asciidoctor chapter file, normally
`slides/chapters/oidc.adoc` or another relevant chapter.

Use one Asciidoctor slide per flow state:

```adoc
[.notitle]
=== OIDC Login Flow

image::keycloak-flows/oidc/01-oidc-start.svg[width=100%]
```

For subsequent states, keep the same slide title or use short state-specific
titles:

```adoc
[.notitle]
=== OIDC Login Flow

image::keycloak-flows/oidc/02-redirect-to-keycloak.svg[width=100%]
```

The deck sets `imagesdir`, so image paths should be relative to
`slides/images/`.

## Fixed-Size Requirement

Every final slide SVG must use a stable 16:9 canvas:

```xml
<svg xmlns="http://www.w3.org/2000/svg"
     width="1280"
     height="720"
     viewBox="0 0 1280 720"
     role="img">
```

The diagram should be designed for `1280 x 720` and should not depend on
browser layout, CSS from the Reveal deck, JavaScript, SMIL animation, or external
SVG imports.

## Portability Requirement

Each final slide SVG should be self-contained.

Do not rely on external SVG imports in the final slide files, because they may
break when exporting to PDF, PowerPoint, Keynote, OpenOffice, or browser-based
presentation tools.

Preferred approach:

- Keep `sprites/key.svg` and `sprites/lock.svg` as source assets.
- Inline their SVG shapes into each final slide SVG's `<defs>` section.
- Use `<use href="#private-signing-key">` and
  `<use href="#public-verification-key">` inside the slide.

## Visual Style

Use a clean technical diagram style.

Canvas:

```text
1280 x 720
```

Use rounded rectangles for the main participants:

```text
User Machine / Browser
Web Server / Application
Keycloak / Identity Provider
```

Color scheme:

- User Machine: light blue
- Web Server: mid-blue
- Keycloak: dark blue
- Key and lock: silver / gray
- Arrows: dark gray or blue-gray
- Background: white or very light blue-gray

All participant rectangles should have:

- Rounded corners
- Clear labels
- Subtle shadow or border
- Consistent spacing

## Teaching Model

Use this analogy carefully:

```text
Private signing key = silver key, kept inside Keycloak
Public verification key = silver lock, published through JWKS
JWT / ID Token = stamped document or envelope
Authorization Code = small ticket/card
```

The private signing key should visually stay inside Keycloak.

The public verification key may move from Keycloak toward the Web Server,
representing JWKS discovery. Do not imply that the token is encrypted with the
public key or decrypted with the private key. In this flow, Keycloak signs tokens
with a private key, and the application verifies token signatures using public
keys from JWKS.

Use labels such as:

```text
Private signing key stays in Keycloak
Public verification key comes from JWKS
Application verifies signature and claims
```

Avoid using `Public Lock is shared` as the main teaching label; it is visually
acceptable but imprecise as the primary concept.

## Slide Flow

### Slide 01: OIDC Start

Show:

- User Machine on the left
- Web Server in the center
- Keycloak on the right
- User wants to access the web app

Text label:

```text
User opens the web application
```

Arrow:

```text
User Machine -> Web Server
```

### Slide 02: Redirect to Keycloak

Show the browser being redirected to Keycloak.

Arrow:

```text
User Machine -> Keycloak
```

Label:

```text
Redirect to Keycloak Authorization Endpoint
```

### Slide 03: User Authenticates

Show credentials or a login form near Keycloak.

Label:

```text
User authenticates with Keycloak
```

Private signing key sprite should be visible inside Keycloak but not moving.

### Slide 04: Authorization Code Returned

Show an authorization code moving back through the browser to the application
redirect URI.

Arrow:

```text
Keycloak -> User Machine -> Web Server
```

Label:

```text
Authorization Code returned to the application
```

This is still a browser redirect. The code appears at the application's redirect
URI; the browser does not receive tokens in this simplified server-side flow.

### Slide 05: Token Exchange

Show the Web Server exchanging the authorization code with Keycloak.

Arrow:

```text
Web Server -> Keycloak
```

Label:

```text
Web server exchanges code at the Token Endpoint
```

Then show token response:

```text
Keycloak -> Web Server
```

Label:

```text
ID Token / Access Token returned
```

### Slide 06: JWKS Verification Key

Show the public verification key moving from Keycloak to the Web Server.

Important:

- The private signing key remains inside Keycloak.
- The public verification key moves or appears near the Web Server.
- The Web Server verifies token signatures and validates claims.

Label:

```text
Web server retrieves public keys from JWKS
```

Also show:

```text
Private signing key stays secret
Public verification key is published
Signature and claims are validated
```

### Slide 07: Session Created

Show the user machine accessing the web server successfully.

Label:

```text
Authenticated application session established
```

Show:

- Token/document verified at Web Server
- Public verification key near Web Server
- Private signing key still inside Keycloak
- Application session or session cookie associated with the browser

## SVG Implementation Notes

Use groups and transforms so objects can be moved easily:

```xml
<g id="user-machine" transform="translate(80 260)">
  ...
</g>

<g id="web-server" transform="translate(480 260)">
  ...
</g>

<g id="keycloak" transform="translate(880 260)">
  ...
</g>

<use href="#private-signing-key" transform="translate(1010 335) scale(0.8)" />
<use href="#public-verification-key" transform="translate(620 335) scale(0.8)" />
```

Use reusable symbols or groups in `<defs>`:

```xml
<defs>
  <g id="private-signing-key">
    ...
  </g>

  <g id="public-verification-key">
    ...
  </g>
</defs>
```

Avoid JavaScript animation. Avoid SMIL animation. The animation is created by
slide-to-slide state changes only.

## Deliverables

Create:

```text
slides/images/keycloak-flows/oidc/sprites/key.svg
slides/images/keycloak-flows/oidc/sprites/lock.svg
slides/images/keycloak-flows/oidc/01-oidc-start.svg
slides/images/keycloak-flows/oidc/02-redirect-to-keycloak.svg
slides/images/keycloak-flows/oidc/03-user-authenticates.svg
slides/images/keycloak-flows/oidc/04-authorization-code-returned.svg
slides/images/keycloak-flows/oidc/05-token-exchange.svg
slides/images/keycloak-flows/oidc/06-jwks-verification-key.svg
slides/images/keycloak-flows/oidc/07-session-created.svg
slides/images/keycloak-flows/oidc/README.md
```

The `README.md` should explain:

- How the sprites work
- How each SVG represents a flow state
- That final slide SVGs are self-contained
- That the private signing key remains inside Keycloak
- That the public verification key represents the JWKS key used for signature
  verification
- How to add another slide by copying an existing slide and changing transforms
- How to include a generated SVG from an Asciidoctor Reveal.js slide using
  `image::keycloak-flows/oidc/name.svg[width=100%]`
