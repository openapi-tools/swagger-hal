package io.openapitools.hal.example;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.openapitools.hal.example.model.AccountRepresentation;
import io.openapitools.hal.example.model.AccountUpdateRepresentation;
import io.openapitools.hal.example.model.AccountsRepresentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Exposing account as REST service.
 */
@Path("/accounts")
@OpenAPIDefinition(tags = @Tag(name = "/accounts"), security = @SecurityRequirement(name = "oauth2"))
public class AccountServiceExposure {

    @GET
    @Produces({"application/hal+json"})
    @Operation(description = "List all accounts")
    public AccountsRepresentation list(@Context UriInfo uriInfo, @Context Request request) {
        return new AccountsRepresentation();
    }

    @GET
    @Path("{regNo}-{accountNo}")
    @Produces({"application/hal+json"})
    @Operation(description = "Get single account")
    public AccountRepresentation get(@PathParam("regNo") @Pattern(regexp = "^[0-9]{4}$") String regNo,
            @PathParam("accountNo") @Pattern(regexp = "^[0-9]+$") String accountNo,
            @Context UriInfo uriInfo, @Context Request request) {
        return new AccountRepresentation();
    }

    @PUT
    @Path("{regNo}-{accountNo}")
    @Produces({"application/hal+json"})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(description = "Create new or update existing account")
    public Response createOrUpdate(@PathParam("regNo") @Pattern(regexp = "^[0-9]{4}$") String regNo,
            @PathParam("accountNo") @Pattern(regexp = "^[0-9]+$") String accountNo,
            @Valid AccountUpdateRepresentation account,
            @Context UriInfo uriInfo, @Context Request request) {
        return Response.ok().build();
    }

}
