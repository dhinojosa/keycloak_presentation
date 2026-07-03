package com.evolutionnext.keycloakdemo.spring;

import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PageController {

    @GetMapping({"/", "/public"})
    String index() {
        return page("Spring Keycloak Demo", "Public page",
            """
            <p>This page is public. Use the protected links to trigger the OIDC Authorization Code flow.</p>
            <ul>
              <li><a href="/user">User page</a></li>
              <li><a href="/admin">Admin page</a></li>
              <li><a href="/engineering">Engineering page</a></li>
              <li><a href="/sales">Sales page</a></li>
            </ul>
            """);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('user')")
    String user(Authentication authentication, CsrfToken csrfToken) {
        return protectedPage("User", authentication, csrfToken);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    String admin(Authentication authentication, CsrfToken csrfToken) {
        return protectedPage("Admin", authentication, csrfToken);
    }

    @GetMapping("/engineering")
    @PreAuthorize("hasRole('engineering')")
    String engineering(Authentication authentication, CsrfToken csrfToken) {
        return protectedPage("Engineering", authentication, csrfToken);
    }

    @GetMapping("/sales")
    @PreAuthorize("hasRole('sales')")
    String sales(Authentication authentication, CsrfToken csrfToken) {
        return protectedPage("Sales", authentication, csrfToken);
    }

    private String protectedPage(String area, Authentication authentication, CsrfToken csrfToken) {
        String name = authentication.getName();
        String roles = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(", "));
        String subject = authentication.getPrincipal() instanceof OidcUser oidcUser
            ? oidcUser.getSubject()
            : "unknown";
        return page("Spring Keycloak Demo", area + " page",
            """
            <p>You reached the %s page at <code>%s</code>.</p>
            <dl>
              <dt>Principal</dt><dd><code>%s</code></dd>
              <dt>Subject</dt><dd><code>%s</code></dd>
              <dt>Authorities</dt><dd><code>%s</code></dd>
            </dl>
            <p><a href="/">Home</a></p>
            <form action="/logout" method="post">
              <input type="hidden" name="%s" value="%s">
              <button type="submit">Log out of Spring and Keycloak</button>
            </form>
            """.formatted(area, Instant.now(), name, subject, roles,
                csrfToken.getParameterName(), csrfToken.getToken()));
    }

    private String page(String title, String heading, String body) {
        return """
            <!doctype html>
            <html lang="en">
            <head>
              <meta charset="utf-8">
              <title>%s</title>
              <style>
                body { font-family: system-ui, sans-serif; margin: 3rem; line-height: 1.5; }
                code { background: #f2f3f4; padding: 0.1rem 0.25rem; }
                dt { font-weight: 700; margin-top: 0.75rem; }
              </style>
            </head>
            <body>
              <h1>%s</h1>
              %s
            </body>
            </html>
            """.formatted(title, heading, body);
    }
}
