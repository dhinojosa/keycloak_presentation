# TODO

Deck-wide review notes from the current Keycloak presentation pass.

## Priority 1: Fix Before Continuing

1. Tighten `slides/chapters/oidc.adoc`.
   - Current OIDC intro is weaker than the JWT/OAuth/JWKS chapters.
   - Replace `We need more information!` with a sharper OAuth-to-identity bridge.
   - Define the difference between Access Token, ID Token, and Refresh Token before the OIDC images.
   - Remove or rewrite `Sharing Public Keys`; it duplicates JWKS and currently says little.
   - Add a short slide for OIDC discovery that says: issuer metadata tells clients where authorization, token, userinfo, logout, and JWKS endpoints live.

2. Fix `slides/chapters/identity_is_hard.adoc`.
   - Current line is awkward: `"Who are you?" It is a different question as "What are you allowed to do?"`
   - Preferred wording: `"Who are you?" and "What are you allowed to do?" are different questions.`
   - Consider whether the breach slides are too many. They are good, but this chapter may be long relative to the rest of the deck.

3. Rewrite `slides/chapters/wrapup.adoc`.
   - Current wrap-up has placeholder text:
     - `Cookies, how to store? I suppose...`
     - `How to get to use refresh tokens`
   - Replace with a final "Monday Morning Checklist" slide.
   - Suggested checklist:
     - Use Authorization Code + PKCE for browser login
     - Validate JWT signature, issuer, audience, and expiration
     - Cache JWKS and refresh on unknown `kid`
     - Keep Access Tokens short-lived
     - Store Refresh Tokens only where the client can protect them
     - Use `HttpOnly`, `Secure`, and appropriate `SameSite` cookies
     - Prefer centralized identity over per-application auth logic

4. Rewrite `slides/chapters/keycloak_vs_vault.adoc`.
   - Current analogy is incorrect/misleading: `Keycloak` is listed as storing photos, addresses, identity records.
   - Pushback: Keycloak may store profile attributes, but the point should be identity and access, not personal data storage.
   - Better shape:
     - Keycloak: users, login, tokens, sessions, roles, federation
     - Vault: secrets, encryption keys, leases, rotation, dynamic credentials
     - Rule: Keycloak answers "who are you and what can you access?"; Vault protects secrets and issues secret material.
   - Replace `#SelfHosted` with a real slide title, probably `Self-Hosted Tradeoffs`.

## Priority 2: Monday Morning Gaps

1. Add a "Validation Checklist" slide near JWT/JWKS or wrap-up.
   - Developers need this exact list:
     - Verify signature
     - Check `iss`
     - Check `aud`
     - Check `exp` and optionally `nbf`
     - Reject unknown algorithms
     - Map claims to local permissions deliberately
   - This is one of the most practical takeaways in the whole deck.

2. Add a "Token Storage" slide.
   - Current deck talks about cookies and refresh tokens separately, but does not give a direct recommendation.
   - Clarify:
     - Browser app session: prefer server-side session with `HttpOnly` cookies
     - Access Token: short-lived; do not treat it like a password
     - Refresh Token: protect aggressively; do not expose casually to browser JavaScript
     - Native/CLI/device clients have different storage tradeoffs

3. Add "Realm and Client Setup Defaults" somewhere around Keycloak or Java integration.
   - Monday morning developers need to know what to configure first:
     - Realm
     - Client type
     - Redirect URI
     - Web origins / CORS
     - PKCE
     - Roles/scopes
     - Token lifetimes
   - This could be a compact checklist slide before the Spring/Quarkus demos.

4. Add "Roles vs Scopes vs Claims".
   - The deck uses all three terms, but does not clearly separate them.
   - Suggested framing:
     - Claim: data inside a token
     - Scope: requested/granted access boundary
     - Role: authority often mapped into claims
     - Application still decides what claims mean locally

5. Add "Do Not Introspect Every JWT" or "Local Validation First".
   - JWKS says Keycloak is not called on every request, which is good.
   - It may deserve a stronger operational statement:
     - JWT validation is normally local
     - Introspection is for opaque tokens or special revocation needs
     - Do not build a Keycloak network call into every API request unless you mean to

## Priority 3: Chapter-Specific Review

1. `slides/chapters/token_lifetimes.adoc`
   - Good concept, but it ends with two empty divider slides:
     - `Trust Boundaries and Token Validation`
     - `Piecing Everything Together`
   - Either fill them or remove them.
   - Add refresh-token rotation/reuse detection if you want practical Keycloak hardening.
   - Clarify that refresh tokens go to Keycloak, not resource servers.

2. `slides/chapters/browser_security.adoc`
   - Strong chapter overall.
   - Add one concrete "OIDC callback safety" slide:
     - Validate `state`
     - Use PKCE
     - Lock down redirect URIs
     - Use HTTPS in production
   - Consider adding CORS/Web Origins because Keycloak users hit this immediately.

3. `slides/chapters/java_integrations.adoc`
   - Spring slide mixes resource-server configuration with login concepts.
   - Need split:
     - Spring Resource Server: validates bearer JWTs for APIs
     - Spring OAuth2 Login: redirects browser users to Keycloak
   - Same concern for Quarkus: clarify service/API JWT validation vs interactive login.
   - Demo titles could be more direct:
     - `Demo: Spring Resource Server`
     - `Demo: Quarkus OIDC`

4. `slides/chapters/saml.adoc`
   - Good start, but "The predecessor to today's JWT" is oversimplified.
   - Better: "Similar job to an ID Token, older XML-based format."
   - `Centralizing Trust` currently ends with `Spring API`, which is incomplete.
   - Add the missing result: Keycloak maps external identity to local roles/claims and issues tokens to the application.

5. `slides/chapters/sasl_oauthbearer.adoc`
   - Chapter has duplicated explanation:
     - OAuthBearer over SASL
     - Keycloak and SASL
     - OAuthBearer
     - OAuthBearer Authentication
     - OAuthBearer Purpose
   - It should be compressed.
   - Pushback: "the only relevant communication is OAuthBearer" is too strong. For this talk, OAuthBearer is the relevant SASL mechanism; SASL itself supports many.
   - JAAS slide is too wordy and generic. It should focus on Kafka client/broker configuration and callback handlers.

6. `slides/chapters/other_features.adoc`
   - Good outline, but it feels like a grab bag.
   - "Other Features" could become "Operating Identity" or "Beyond Login".
   - `Checklist for DIY` is one of the most useful slides; consider moving it earlier or repeating a shorter version in wrap-up.
   - Add one warning: DIY auth means owning callback validation, token validation, storage, logout, and lifecycle behavior.

7. `slides/chapters/intro.adoc`
   - Intro agenda should match the revised deck:
     - Why identity is hard
     - Keycloak vocabulary
     - JWT, OAuth 2.0, JWKS, OIDC
     - Token lifetime and browser safety
     - Spring, Quarkus, Kafka
     - Operating identity

## Priority 4: Consistency and Polish

1. Standardize "Access Token", "Refresh Token", "ID Token".
   - Headings and major bullets use title case.
   - Body text can use sentence style, but token names should be consistent when presented as concepts.

2. Standardize "application" instead of "app" in teaching text.
   - The user prefers "Application" in the password-flow slide.
   - Scan for casual `app` wording where it would read better as `application`.

3. Replace casual demo titles.
   - `Let's Link up to...` is weaker than the rest of the deck.
   - Prefer direct demo titles that name the task.

4. Add primary spec links sparingly.
   - Already done for JWKS.
   - Consider adding footers for:
     - OAuth 2.0 / OAuth 2.1 note
     - OIDC Core
     - JWT RFC 7519
     - PKCE RFC 7636
   - Do not overload every slide with URLs.

5. Review all flow SVG captions.
   - The diagrams are useful, but some labels may still be too generic.
   - Ensure labels use natural capitalization and consistently say Access Token, Refresh Token, ID Token, JWT, JWKS.

## Bigger Content Idea

Add a recurring "Who validates what?" thread:

- Keycloak authenticates users and issues tokens
- Applications redirect users and hold local sessions
- APIs validate Access Tokens locally with JWKS
- Browsers carry redirects and cookies
- Developers configure redirect URIs, token validation, roles/scopes, cookies, and logout

This is probably the strongest "Monday morning" throughline for the deck. It turns protocols into operational responsibility.
