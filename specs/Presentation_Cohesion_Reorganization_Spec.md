# Keycloak Presentation Cohesion and Reorganization Spec

## Purpose

Rebuild the protocol portion of the deck around one coherent sequence:

1. Protect the transport.
2. Understand the token format.
3. Understand authorization and token purpose.
4. Verify signed tokens locally.
5. Add identity and browser login.
6. Protect the resulting browser session.
7. Show alternate enterprise and non-HTTP integrations.

The goal is not to add more material. The goal is to move, merge, rewrite, and
delete material until each concept is introduced once, in the order needed to
understand the next concept.

## Presentation Rules

- [ ] Keep each concept slide to one teaching job.
- [ ] Define a term before using it on a later slide or flow explanation.
- [ ] Prefer one representative example over several abstract explanation slides.
- [ ] Put implementation detail in speaker notes when it is useful for questions
      but not required for the main narrative.
- [ ] Remove duplicate explanations after content is moved.
- [ ] Do not create, redesign, extend, or edit animation SVGs.
- [ ] Existing animation slides may be moved or removed as complete units when
      reorganizing content, but their artwork remains unchanged.
- [ ] Use **Web Application**, **Access Token**, **Refresh Token**, and **ID Token**
      consistently.
- [ ] Use title case for all slide headings.
- [ ] Render after each chapter rewrite and inspect dense code, table, image, and
      admonition slides for overflow.

## Out of Scope

- [ ] Do not generate new animation frames.
- [ ] Do not revise the aesthetics, layout, labels, arrows, sprites, or timing of
      existing animation SVGs.
- [ ] Do not convert rewritten flow descriptions into artwork.
- [ ] Treat future animation or diagram replacement as a separate manual design
      task owned by the presentation author.

## Technical Corrections to Preserve

- [ ] Say that HTTPS encrypts data **in transit**. Do not imply that every JWT or
      token is itself encrypted; signed JWT payloads remain readable.
- [ ] Say that Keycloak authenticates the user during an OAuth-based flow. OAuth
      defines delegated authorization and does not define the Web Application's
      identity result.
- [ ] Do not say that a Web Application "receives an expired Access Token." The
      Web Application either tracks expiration or handles a rejected request, then
      presents its Refresh Token to Keycloak's token endpoint.
- [ ] Do not promise a Refresh Token for every OAuth grant. State that it may be
      issued by flows and clients that support refresh.
- [ ] Treat Refresh Tokens as opaque even when their encoded form resembles a JWT.
- [ ] Do not label a normal Keycloak Access Token as opaque. Keycloak normally
      issues JWT Access Tokens. Show its compact wire form, but explain that a
      Web Application should not interpret it as an identity document.
- [ ] Say that OIDC libraries load and cache discovery metadata. Do not guarantee
      that every Web Application downloads it exactly once at startup.
- [ ] Say that JWT libraries load and cache JWKS as needed. Do not require every
      Web Application to download JWKS manually at startup.
- [ ] Keep the original requested path in Web Application session state or
      request state. It is not inherently carried by the OIDC authorization code.
- [ ] Say that cookies and PKCE solve different problems. PKCE protects the
      authorization code; cookies commonly carry browser sessions, including for
      a Backend-for-Frontend architecture.
- [ ] Say that CORS is a browser-enforced origin policy, not authentication and
      not a general barrier against non-browser callers.
- [ ] Keep the formal terms Authorization Server, Client, Resource Server, and
      Resource Owner out of all teaching slides except the glossary.
- [ ] Use concrete actors instead: Keycloak, Browser, Web Application, Backend
      Service, and User.

## Target Chapter Order

- [ ] Keep the existing introduction, identity mishaps, and Keycloak vocabulary
      before the protocol sequence.
- [ ] Add the protocol sequence in this order:
  - HTTP/TLS
  - JWT
  - OAuth 2.0 and Refresh Tokens
  - JWKS and JWT Validation
  - OpenID Connect
  - PKCE and Authorization Code Safety
  - Cookies
  - CSRF
  - CORS and Web Origins
- [ ] Keep Spring and Quarkus after the browser-security material so the examples
      apply concepts already taught.
- [ ] Keep SAML and SASL + OAuthBearer as concise alternate integration chapters.
- [ ] Keep operating identity, Keycloak vs Vault, self-hosting, and the conclusion
      after the integration chapters.
- [ ] Define concrete actors near first use. Keep the formal OAuth terminology on
      one glossary slide only; do not rely on those terms in the main narrative.

## HTTP/TLS

### Add

- [ ] Add a compact `HTTP/TLS` chapter immediately before JWT.
- [ ] Add `HTTPS Is the Baseline` with these points:
  - Credentials, authorization codes, tokens, and cookies cross the network.
  - Production traffic must use HTTPS.
  - TLS protects data in transit from reading and modification.
  - A signed JWT is not encrypted and still requires HTTPS.
- [ ] Add speaker notes covering TLS termination at a reverse proxy and the need
      to preserve the original scheme and forwarding headers.

### Move or Delete

- [ ] Move the general HTTPS requirement from `Development vs Production` into
      this chapter.
- [ ] Keep only browser-specific proxy, redirect, and `Secure` cookie consequences
      in `browser_security.adoc`.
- [ ] Do not expand this into certificate management or PKI training.

## JWT

### Keep and Tighten

- [ ] Keep the JWT logo slide, but make its main statement: JWT is a compact token
      **format**, not a token purpose.
- [ ] Keep `JWT Shape` and show exactly `header.payload.signature`.
- [ ] Rename `content` to the standard term `payload` everywhere.
- [ ] Keep one small decoded example showing header, payload, and signature roles.
- [ ] Keep a concise Claims slide with the IANA registry link.
- [ ] Keep the warning that decoding does not establish trust.

### Move

- [ ] Move `JWT Validation Checklist` to the JWKS chapter after key selection and
      local signature verification have been explained.
- [ ] Move detailed `kid`, algorithm, signature, issuer, audience, expiration, and
      not-before validation explanations to JWKS.
- [ ] Move `Roles, Scopes, and Claims` to OAuth after the Access Token example, or
      to Keycloak vocabulary if it reads better during implementation.

### Delete or Merge

- [ ] Merge `What Keycloak Does with JWTs` into the JWT introduction or JWKS.
- [ ] Merge `What Applications Must Do` into the moved validation checklist.
- [ ] Merge `Never Trust Parsed Claims First` into the JWT shape/example slide as
      a warning and speaker note.
- [ ] Keep `Demo: Inspect a JWT` only if the live demonstration remains in the
      rehearsal plan.

## OAuth 2.0 and Refresh Tokens

### Establish the Model

- [ ] Define OAuth as delegated authorization.
- [ ] Define an Access Token as a credential issued by Keycloak or another
      identity system for calling an endpoint that requires that token.
- [ ] Keep the direct question and answer:
  - "Does OAuth authenticate the user?"
  - "Keycloak may authenticate the user during the flow; OAuth itself does not
    define the Web Application's identity result."
- [ ] Keep `Access Token: Job vs Format`, but reduce it to the distinction:
  - Access Token = purpose
  - JWT or opaque = format
  - Keycloak normally issues signed JWT Access Tokens
- [ ] Replace `OAuth Participants` with a concrete `Actors in This Flow` slide
      before any OAuth flow:
  - Browser carries redirects and cookies.
  - Web Application starts login, handles the callback, and stores its session.
  - Keycloak authenticates the user and issues tokens.
  - Backend Service appears only when a separate service receives an Access Token.
- [ ] Do not show the formal OAuth participant names on this slide.

### Show Token Examples

- [ ] Replace the current large decoded Access Token example with two compact
      examples:
  - Access Token on the wire: `eyJ...` presented as a bearer credential
  - Representative decoded Keycloak payload in speaker notes or a follow-up slide
- [ ] Keep a visible note that Keycloak claim sets depend on configured scopes, role
      mappings, and protocol mappers.
- [ ] Show a Refresh Token as an opaque credential, for example
      `refresh_token: eyJ...`, without teaching its internal claims.
- [ ] State that applications send Access Tokens to APIs and Refresh Tokens only
      to Keycloak's token endpoint.

### Consolidate Refresh-Token Material

- [ ] Move these slides from `token_lifetimes.adoc` into OAuth:
  - `Token Lifetimes`
  - `Two Tokens: Two Responsibilities`
  - `Refresh Token Flow`
  - the existing refresh-token animation slides, moved unchanged if retained
  - `Refresh Token Rotation`
- [ ] Rewrite the refresh sequence as:
  1. Keycloak issues an Access Token and, when applicable, a Refresh Token.
  2. The Web Application uses the Access Token until it expires or is rejected.
  3. The Web Application sends the Refresh Token to Keycloak.
  4. Keycloak issues a new Access Token and may rotate the Refresh Token.
  5. When refresh is expired, revoked, or rejected, the user must authenticate
     again.
- [ ] Explain repeated Access Token expiration and final Refresh Token rejection
      in prose or speaker notes; do not add or alter animation frames.
- [ ] State that the Refresh Token is stored securely by the server-side Web
      Application. Native and browser-only applications require storage guidance
      specific to their platform and architecture.

### Decide What to Retain

- [ ] Keep Client Credentials and Device Authorization as short optional patterns
      after the core OAuth/refresh story.
- [ ] Keep Password Flow only as a caution.
- [ ] Move Authorization Code + PKCE details to the OIDC/PKCE sequence.
- [ ] Delete the hotel-room analogy unless rehearsal proves it resolves confusion
      faster than the concrete refresh sequence.
- [ ] Remove `token_lifetimes.adoc` from `main.adoc` after all retained content has
      moved; do not leave a duplicate token chapter.

## JWKS and JWT Validation

### Core Slides

- [ ] Keep `JSON Web Key Set (JWKS)` with this concise definition:
  - Published set of public keys
  - Used to verify JWT signatures
  - Loaded and cached by applications and APIs
  - Refreshed for an unknown `kid`, cache policy, or key rotation
  - Keycloak is not called for every request to a Web Application endpoint
- [ ] Keep `Key ID (kid)` immediately before the verification sequence.
- [ ] Explicitly state that the JWT signature is not a key.
- [ ] Explain that Keycloak signs with a private key and the verifier uses the
      corresponding public key from JWKS.

### Example and Existing Flow

- [ ] Show one compact JWKS JSON example containing at least `kid`, `kty`, `alg`,
      `use`, `n`, and `e`.
- [ ] Retain or remove the existing five-frame JWT/JWKS animation as a complete,
      unchanged unit. The surrounding explanation must communicate this sequence:
  1. Web Application endpoint receives JWT.
  2. Web Application reads the untrusted header and `kid`.
  3. Library selects the matching cached public key.
  4. Unknown `kid` or cache policy triggers JWKS refresh.
  5. Library verifies the signature and validates claims.
- [ ] Ensure the surrounding text and speaker notes do not imply that the Web
      Application loops over every key or "decodes until one works."

### Validation Checklist

- [ ] Move the JWT validation table here and group it in speaking order:
  - Cryptographic: allowed algorithm and signature
  - Protocol: `iss`, `aud`, `exp`, and optional `nbf`
  - Authorization: deliberate local interpretation of scopes, roles, and claims
- [ ] Keep speaker notes distinguishing:
  - decode = read Base64URL content
  - verify = cryptographically check the signature
  - validate = verify plus enforce required claims and policy
- [ ] Decide during rehearsal whether `Passport Analogy` adds value after the
      concrete verification sequence; delete it if it merely repeats the model.

## OpenID Connect

### Introduce Identity

- [ ] Define OpenID Connect before using OIDC.
- [ ] State that OIDC adds an identity and authentication layer to OAuth 2.0.
- [ ] Define the ID Token as Keycloak's signed authentication result for the Web
      Application, identifying the authenticated user.
- [ ] State that an ID Token is always a JWT.
- [ ] Keep a concise `Three Tokens, Three Jobs` table:
  - Access Token -> call an endpoint that requires the token
  - ID Token -> authentication result for the Web Application
  - Refresh Token -> request new tokens from Keycloak

### Show OIDC Artifacts

- [ ] Add a representative decoded ID Token showing `iss`, `sub`, `aud`, `exp`,
      `iat`, `auth_time`, and `nonce`.
- [ ] Add an authorization code example as an opaque, short-lived, one-time value.
- [ ] Add a `state` example and define it as a random value created for each login
      request and validated on callback.
- [ ] Add a UserInfo response example with profile claims.
- [ ] Add a compact discovery-document excerpt containing authorization, token,
      userinfo, JWKS, and logout endpoints.
- [ ] Keep `{issuer}/.well-known/openid-configuration` visible.
- [ ] State that OIDC libraries retrieve and cache discovery metadata and
      follow its `jwks_uri`; exact refresh timing is library-specific.

### OIDC Flow Content Contract

- [ ] Teach this sequence through concise slides, speaker notes, and any existing
      animation frames that are retained unchanged:
  1. Browser requests a protected path from the Web Application.
  2. Web Application stores the original destination and creates `state` and PKCE
     values for the login request.
  3. Browser is redirected to Keycloak.
  4. Keycloak authenticates the user.
  5. Browser returns to the registered callback with authorization code and
     `state`.
  6. Web Application validates `state` and exchanges the code plus PKCE verifier at
     Keycloak's token endpoint.
  7. Keycloak returns Access Token, ID Token, and, when applicable, Refresh Token.
  8. Web Application validates the ID Token using issuer metadata and JWKS.
  9. Web Application creates server-side session state and sends a hardened session
     cookie to the browser.
  10. Web Application may call UserInfo when it needs additional profile claims.
- [ ] Do not say that Keycloak returns the original requested path unless the
      Web Application deliberately encoded or stored that state.
- [ ] Do not imply that roles must be copied into Redis. Store only the session
      and identity/authorization state required by the Web Application's design.
- [ ] Do not add or edit frames to cover missing steps. Use a static slide or
      speaker notes, or leave the visual change for the author's manual design
      pass.

### Manual OIDC Visual Storyboard

This storyboard describes a future manual visual pass. It does not authorize the
agent to create or edit animation SVGs.

#### Shared Visual Layout

- [ ] Keep the Browser on the left, Web Application in the center, and Keycloak on
      the right in the same positions on every slide.
- [ ] Add a small Session Store below the Web Application only when session state
      becomes relevant.
- [ ] Use a white background and the existing fixed slide dimensions.
- [ ] Keep prior actors visible and muted; emphasize only the actor, arrow, and
      artifact that change on the current slide.
- [ ] Use one directional arrow for each visible message.
- [ ] Label arrows with concrete values such as `GET /reports`, `302 Redirect`,
      `code`, `state`, `code_verifier`, and `Set-Cookie`.
- [ ] Do not put the formal OAuth participant names on these slides.
- [ ] Do not show Access Tokens, ID Tokens, or Refresh Tokens inside the Browser.

#### Slide 1: Browser Attempts to Access a Protected Path

- [ ] Title: `Browser Attempts to Access a Protected Path`
- [ ] Show the Browser on the left with an address bar containing a concrete path,
      such as `https://application.example.com/reports`.
- [ ] Show the Web Application in the center with the requested `/reports`
      endpoint and a small lock indicating that login is required.
- [ ] Draw one arrow from Browser to Web Application labeled `GET /reports`.
- [ ] Show no Keycloak traffic yet.
- [ ] Teaching point: the login flow begins because the requested Web Application
      path requires an authenticated session.

#### Slide 2: Web Application Redirects Browser to Keycloak

- [ ] Title: `Web Application Redirects Browser to Keycloak`
- [ ] Show the Web Application saving these temporary values beside it:
  - original path: `/reports`
  - random `state`
  - PKCE `code_verifier`
- [ ] Show the Web Application also creating a random `nonce` for later ID Token
      validation.
- [ ] Show `code_challenge` leaving the Web Application, not `code_verifier`.
- [ ] Draw a return arrow to the Browser labeled `302 Redirect` and continue the
      visual route from Browser to Keycloak.
- [ ] Label the Keycloak request with `state`, `nonce`, and `code_challenge`.
- [ ] Teaching point: the Web Application starts login, but the Browser follows the
      redirect to Keycloak.

#### Slide 3: Keycloak Authenticates the User

- [ ] Title: `Keycloak Authenticates the User`
- [ ] Emphasize Keycloak and show its login screen or authentication step.
- [ ] Keep Browser between the user and Keycloak.
- [ ] Show a successful authentication indicator only after credentials or another
      configured authentication method has been accepted.
- [ ] Do not show tokens yet.
- [ ] Teaching point: Keycloak authenticates the user; OAuth does not perform the
      authentication itself.

#### Slide 4: Keycloak Returns an Authorization Code

- [ ] Title: `Keycloak Returns an Authorization Code`
- [ ] Draw the response from Keycloak through the Browser to the Web Application's
      registered callback path.
- [ ] Label the callback with `code` and the same `state` value sent earlier.
- [ ] Show the authorization code as a small opaque receipt, not as a JWT.
- [ ] Do not show Access Token, ID Token, or Refresh Token yet.
- [ ] Teaching point: the authorization code is short-lived, one-time, and carried
      through the Browser to the Web Application.

#### Slide 5: Web Application Validates State and Exchanges the Code

- [ ] Title: `Web Application Validates State and Exchanges the Code`
- [ ] First show the returned `state` matching the value stored by the Web
      Application.
- [ ] Then draw a direct backend arrow from Web Application to Keycloak's token
      endpoint.
- [ ] Label the backend request with `authorization code` and `code_verifier`.
- [ ] Keep the Browser visible but inactive; it does not participate in this
      backend exchange.
- [ ] Teaching point: `state` protects callback correlation, while PKCE proves that
      the Web Application completing the exchange started the login request.

#### Slide 6: Keycloak Returns Tokens to the Web Application

- [ ] Title: `Keycloak Returns Tokens to the Web Application`
- [ ] Draw a direct backend arrow from Keycloak to Web Application.
- [ ] Show three distinct artifacts at the Web Application:
  - ID Token
  - Access Token
  - Refresh Token, when issued
- [ ] Keep all three artifacts outside the Browser.
- [ ] Add short labels beneath the tokens:
  - ID Token: authentication result
  - Access Token: call an endpoint that requires it
  - Refresh Token: request new tokens from Keycloak
- [ ] Teaching point: token names describe their jobs; they are not interchangeable.

#### Slide 7: Web Application Validates the ID Token

- [ ] Title: `Web Application Validates the ID Token`
- [ ] Emphasize the ID Token and Web Application.
- [ ] Show the Web Application using Keycloak issuer metadata and cached JWKS
      public keys.
- [ ] Show checks for signature, `iss`, `aud`, `exp`, and `nonce` as one compact
      validation checklist.
- [ ] If the JWKS visual is shown, label it `Cached Public Keys`; do not imply that
      keys are downloaded for every login.
- [ ] Teaching point: receiving an ID Token is not enough; the Web Application must
      validate it before trusting the identity.

#### Slide 8: Web Application Creates a Session

- [ ] Title: `Web Application Creates a Session`
- [ ] Show the Web Application writing a random session ID and required user state
      to the Session Store.
- [ ] Do not require roles or tokens to be stored unless the Web Application needs
      them for its design.
- [ ] Show a return arrow to the Browser labeled `Set-Cookie`.
- [ ] Show only a random session identifier in the cookie, with `HttpOnly`,
      `Secure`, and `SameSite` beside it.
- [ ] Do not place the ID Token, Access Token, or Refresh Token inside the cookie.
- [ ] Teaching point: the Browser normally carries the Web Application's session
      cookie, while sensitive token state remains on the backend.

#### Slide 9: Browser Returns to the Original Path

- [ ] Title: `Browser Returns to the Original Path`
- [ ] Show the Browser requesting `/reports` again with the session cookie.
- [ ] Show the Web Application looking up the session ID in the Session Store.
- [ ] Replace the lock on `/reports` with the rendered reports page or a clear
      success state.
- [ ] Keep Keycloak visible but inactive to show that it is not called for every
      Web Application request.
- [ ] Teaching point: login has produced a local Web Application session, and the
      user can now access the path that originally triggered login.

#### Optional Static Slide: Web Application Calls UserInfo

- [ ] Keep UserInfo out of the core nine-frame story unless the presentation needs
      the additional profile-data step.
- [ ] If retained, show the Web Application sending the Access Token directly to
      Keycloak's UserInfo endpoint and receiving profile claims.
- [ ] Keep the Browser inactive and do not imply that UserInfo is required for
      every OIDC login.
- [ ] Teaching point: UserInfo is an optional source of additional profile claims,
      not the proof that authentication succeeded.

## PKCE

- [ ] Place PKCE immediately before the OIDC authorization-code flow or as a
      focused subchapter within it.
- [ ] Define the authorization code as a short-lived, one-time credential
      exchanged for tokens.
- [ ] Explain that PKCE binds that code to the Web Application or browser-only
      application instance that started login.
- [ ] Keep the existing `code_verifier` and `code_challenge` numbered explanation.
- [ ] State that PKCE is required for native and browser-only applications that
      cannot safely keep a secret, and recommended for server-side Web Applications.
- [ ] Do not describe PKCE as a replacement for cookies, Web Application
      authentication to Keycloak,
      `state`, redirect URI validation, or CSRF protection.
- [ ] Keep the PKCE RFC 7636 footer.

## Cookies

- [ ] Keep Cookies after OIDC so the audience knows which Web Application session the
      cookie represents.
- [ ] State that Keycloak has its own SSO cookies and the Web Application usually has
      a separate local session cookie.
- [ ] Define `HttpOnly`, `Secure`, and `SameSite` concisely.
- [ ] Preserve the practical `Path`, `Max-Age`, and `Expires` details only if the
      slide remains visually balanced.
- [ ] Explain that native applications use platform-secure storage rather than web
      cookies and that browser-only applications require a deliberate token
      storage architecture.
- [ ] Keep the warning against exposing long-lived credentials, especially
      Refresh Tokens, to browser JavaScript.

## CSRF

- [ ] Define CSRF as a malicious site causing a logged-in browser to send an
      unwanted authenticated request because the browser attaches cookies.
- [ ] Keep one hidden-field example:

  ```html
  <input type="hidden" name="_csrf" value="...">
  ```

- [ ] Keep Web Application CSRF tokens separate from OIDC `state`:
  - CSRF token protects state-changing Web Application requests.
  - `state` protects the OIDC callback/request correlation.
- [ ] Keep the logout example only if it remains the fastest concrete illustration.

## CORS and Web Origins

- [ ] State that CORS controls which browser origins may read responses from
      cross-origin requests.
- [ ] Explain the Keycloak Web Origins setting for the Web Application and how it
      differs from Redirect URIs.
- [ ] Warn against wildcard origins in production.
- [ ] Replace "prevents unauthorized browser applications from using Keycloak"
      with the narrower and accurate browser-enforcement explanation.
- [ ] Add speaker notes stating that CORS does not stop curl, backend services, or
      malicious non-browser clients and is not an authorization mechanism.

## SAML

- [ ] Keep this chapter short:
  - Security Assertion Markup Language
  - XML-based federation and SSO standard
  - Signed assertions
  - Supported by Keycloak as Identity Provider and Service Provider
- [ ] Keep one compact SAML assertion example.
- [ ] Remove any remaining slides that deepen SAML beyond recognition and
      integration context.

## SASL + OAuthBearer

- [ ] Keep the SASL definition and examples of protocols that use it.
- [ ] Explain that Kafka implements SASL and selects `OAUTHBEARER` as one possible
      mechanism.
- [ ] Define a bearer token as a credential usable by whoever possesses it.
- [ ] State that the Kafka client presents an OAuth Access Token and the broker
      authenticates the Kafka client or represented user before applying authorization.
- [ ] Retain or remove the existing four-frame animation as a complete, unchanged
      unit. The surrounding explanation must communicate:
  1. Kafka Client obtains Access Token from Keycloak.
  2. Kafka Client starts SASL negotiation with Kafka.
  3. Kafka Client selects OAuthBearer and presents the JWT.
  4. Kafka validates the JWT with JWKS and applies authorization.
- [ ] Keep Kafka callback-handler and JAAS configuration detail on one practical
      wiring slide or in the demo, not in the conceptual flow explanation.

## Glossary and Vocabulary

- [ ] Keep the formal OAuth participant terms on the glossary slide only.
- [ ] Map the formal terms to the deck's concrete actors:
  - Authorization Server: Keycloak
  - Client: Web Application when it starts the flow and exchanges the code
  - Resource Server: Web Application or Backend Service endpoint that accepts an
    Access Token
  - Resource Owner: User; do not use this term elsewhere in the deck
  - User-Agent: Browser carrying redirects between the Web Application and Keycloak
- [ ] Explicitly note that the Browser is not the OAuth Client in the server-side
      login flow. It carries messages; the Web Application starts the flow and
      exchanges the authorization code.
- [ ] State that one Web Application can perform more than one formal role. The
      role depends on whether it is requesting tokens or accepting an Access Token.
- [ ] Add framework examples only as examples, not definitions:
  - Spring, Quarkus, Play, and http4s can implement a Web Application or Backend
    Service in these examples.
- [ ] Keep Identity Provider distinct from Authorization Server even though
      Keycloak can perform both roles.
- [ ] Keep the glossary concise and self-contained. It translates specification
      language for developers; it does not replace the concrete actor definitions
      used throughout the presentation.

## Existing File Plan

- [ ] Add `slides/chapters/http_tls.adoc`.
- [ ] Tighten `slides/chapters/jwt.adoc` to format, shape, claims, and decode warning.
- [ ] Merge refresh-token content into `slides/chapters/oauth.adoc`.
- [ ] Move PKCE and authorization-code details from OAuth into the OIDC sequence.
- [ ] Move JWT validation content into `slides/chapters/jwks.adoc`.
- [ ] Expand `slides/chapters/oidc.adoc` with concrete artifacts while deleting
      duplicate prose.
- [ ] Split `slides/chapters/browser_security.adoc` into focused Cookies, CSRF, and
      CORS sections; separate files are optional and should be chosen only if they
      improve maintenance.
- [ ] Remove `slides/chapters/token_lifetimes.adoc` after its retained content has
      moved.
- [ ] Update `slides/main.adoc` includes to match the target order.
- [ ] Update `slides/chapters/intro.adoc` agenda after the chapter order stabilizes.
- [ ] Update `slides/chapters/conclusion.adoc` only after the revised throughline is
      complete.

## Source Links

- OAuth 2.0 Access Tokens:
  <https://www.oauth.com/oauth2-servers/access-tokens/>
- ID Token and Access Token comparison:
  <https://auth0.com/blog/id-token-access-token-what-is-the-difference/>
- Authorization Code Flow with PKCE:
  <https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce>
- OAuth 2.0, RFC 6749:
  <https://www.rfc-editor.org/rfc/rfc6749>
- PKCE, RFC 7636:
  <https://www.rfc-editor.org/rfc/rfc7636>
- JWT, RFC 7519:
  <https://www.rfc-editor.org/rfc/rfc7519>
- JSON Web Key, RFC 7517:
  <https://www.rfc-editor.org/rfc/rfc7517>
- OpenID Connect Core 1.0:
  <https://openid.net/specs/openid-connect-core-1_0.html>
- OpenID Connect Discovery 1.0:
  <https://openid.net/specs/openid-connect-discovery-1_0.html>

## Acceptance Criteria

- [ ] A listener can explain the difference between JWT format, Access Token
      purpose, ID Token purpose, and Refresh Token purpose without relying on a
      later correction.
- [ ] A listener can explain that Keycloak authenticates while OAuth defines the
      authorization framework.
- [ ] A listener can trace a JWT from `kid` to cached JWKS public key to local
      signature verification and claim validation.
- [ ] A listener can trace browser login from protected request through `state`,
      PKCE, authorization code, tokens, local session, and cookie.
- [ ] A listener can state where Access Tokens, Refresh Tokens, ID Tokens, private
      keys, public keys, and session cookies belong.
- [ ] No chapter depends on a protocol term that has not yet been defined.
- [ ] No visible slide repeats another slide's primary teaching point.
- [ ] The generated deck and lab book render successfully with `./setup-docs.sh`.
- [ ] Rehearsal confirms that code examples and retained visual sequences support
      the spoken narrative instead of becoming a second narrative.
