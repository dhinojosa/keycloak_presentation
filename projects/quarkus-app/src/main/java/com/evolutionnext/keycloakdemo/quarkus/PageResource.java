package com.evolutionnext.keycloakdemo.quarkus;

import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Collectors;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class PageResource {

    @Inject
    SecurityIdentity identity;

    @GET
    public String index() {
        return page("Quarkus Keycloak Demo", "Public page",
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

    @GET
    @Path("/user")
    @RolesAllowed("user")
    public String user() {
        return protectedPage("User");
    }

    @GET
    @Path("/admin")
    @RolesAllowed("admin")
    public String admin() {
        return protectedPage("Admin");
    }

    @GET
    @Path("/engineering")
    @RolesAllowed("engineering")
    public String engineering() {
        return protectedPage("Engineering");
    }

    @GET
    @Path("/sales")
    @RolesAllowed("sales")
    public String sales() {
        return protectedPage("Sales");
    }

    private String protectedPage(String area) {
        String roles = identity.getRoles().stream()
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(", "));
        return page("Quarkus Keycloak Demo", area + " page",
            """
            <p>You reached the %s page at <code>%s</code>.</p>
            <dl>
              <dt>Principal</dt><dd><code>%s</code></dd>
              <dt>Roles</dt><dd><code>%s</code></dd>
            </dl>
            <p><a href="/">Home</a></p>
            <form action="/logout" method="get">
              <button type="submit">Log out of Quarkus and Keycloak</button>
            </form>
            """.formatted(area, Instant.now(), identity.getPrincipal().getName(), roles));
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
