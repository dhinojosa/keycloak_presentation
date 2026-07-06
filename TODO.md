# TODO

Deck-wide review notes from the current Keycloak presentation pass. 

Review all flow SVG captions.
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
