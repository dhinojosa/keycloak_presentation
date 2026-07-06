# Keycloak Flow Animations

This spec defines the next set of stop-motion SVG diagrams for the Keycloak
slide deck. The goal is to explain security flows by showing state changes from
slide to slide, matching the existing OIDC sequence.

The deck uses Asciidoctor Reveal.js, so each animation frame is a standalone SVG
included from an AsciiDoc slide:

```adoc
[.notitle]
=== Refresh Token Flow

image::keycloak-flows/refresh-token/01-initial-tokens.svg[width=100%]
```

No Markdown slides, JavaScript animation, SMIL, or runtime animation should be
required.

## Visual Rules

- Use fixed-size `1280 x 720` SVGs.
- Use a white background.
- Keep the same teaching style as the OIDC animation:
  - boxes for actors
  - arrows for messages
  - compact labels using natural capitalization
  - one state change per slide
- Keep arrowheads small and flush with the line tip.
- Reuse the existing visual vocabulary where sensible:
  - JWT document
  - key / JWKS / verification key
  - lock / protected service
- Add small vector sprites only when the existing sprites are not expressive
  enough, such as device, CLI, service account, XML assertion, and Kafka/service.

## Flows to Build

### Refresh Token Flow

Placement: `slides/chapters/token_lifetimes.adoc`, immediately after
`=== Refresh Token Flow`.

Frames:

1. Keycloak issues an access token and refresh token.
2. Application/API requests use the access token.
3. Access token expires.
4. Application sends the refresh token to Keycloak.
5. Keycloak returns a new access token.

Teaching point: refresh tokens are presented to Keycloak, not to resource
servers.

### Client Credentials Flow

Placement: `slides/chapters/oauth.adoc`, after the OAuth participants slide and
before the authorization code flow.

Frames:

1. Backend service identifies itself to Keycloak.
2. Keycloak validates the client credentials.
3. Keycloak issues an access token.
4. Backend service calls an API with that token.

Teaching point: machine-to-machine authentication does not involve a browser or
human user.

### Device Authorization Flow

Placement: `slides/chapters/oauth.adoc`, after the authorization code flow.

Frames:

1. Device asks Keycloak for a device code.
2. Keycloak returns a user code and verification URL.
3. User completes login in a browser.
4. Device polls Keycloak.
5. Keycloak returns tokens after the user authorizes the device.

Teaching point: this is the browser-friendly login pattern for CLIs, TVs,
terminals, and constrained devices.

### JWT Verification and JWKS Refresh Flow

Placement: `slides/chapters/jwks.adoc`, after the JWKS introductory slide.

Frames:

1. API receives a JWT.
2. API reads the token header and `kid`.
3. API verifies the signature with cached JWKS.
4. Unknown `kid` or expired cache triggers a JWKS refresh.
5. API verifies the JWT with the refreshed key set.

Teaching point: normal JWT validation is local; JWKS refresh is only needed when
the verifier needs updated signing keys.

### SAML Flow

Placement: `slides/chapters/saml.adoc`, after `=== SAML and Keycloak`.

Frames:

1. Browser requests a SAML service provider.
2. Service provider redirects the browser to Keycloak as SAML IdP.
3. Keycloak authenticates the user and signs a SAML assertion.
4. Browser posts the assertion back to the service provider.
5. Service provider creates an application session.

Teaching point: SAML uses signed XML assertions rather than JWTs.

### OAuthBearer Flow

Placement: `slides/chapters/sasl_oauthbearer.adoc`, after
`=== OAuthBearer over SASL [Maybe Animate?]`.

Frames:

1. Client obtains an access token from Keycloak.
2. Client starts a SASL connection to Kafka/service.
3. Client presents the JWT using the OAuthBearer mechanism.
4. Service verifies the JWT using JWKS and accepts the connection.

Teaching point: OAuthBearer carries a Keycloak-issued token inside a non-HTTP
protocol authentication exchange.

## Deliberately Not Built as Full Animations

### Authorization Code Flow

Do not build a separate full animation for generic Authorization Code Flow. The
existing OIDC animation already teaches the browser redirect, authorization code,
token exchange, and local JWT verification. Duplicating it in the OAuth chapter
would weaken the pace of the deck.

### Password Flow

Do not build a positive full animation for Resource Owner Password Credentials.
For this deck, treat it as a cautionary slide only. It is legacy behavior and
should not be presented as a default modern Keycloak pattern.

Recommended placement: `slides/chapters/oauth.adoc`, after the modern OAuth
flows, as a single static warning diagram.

Teaching point: avoid collecting user passwords in client applications; prefer
redirect-based flows, device authorization, or client credentials depending on
the use case.

