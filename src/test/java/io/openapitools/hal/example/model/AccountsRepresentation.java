package io.openapitools.hal.example.model;

import java.util.Collection;
import java.util.Collections;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;

/**
 * Represents a set of accounts from the REST service exposure.
 */
@Resource
public class AccountsRepresentation {

    @Link
    private HALLink self;

    @EmbeddedResource("accounts")
    private Collection<AccountRepresentation> accounts;

    private EmptyRepresentation emptyRepresentation;

    public AccountsRepresentation() {
    }

    public HALLink getSelf() {
        return self;
    }

    public Collection<AccountRepresentation> getAccounts() {
        return Collections.unmodifiableCollection(accounts);
    }

    public EmptyRepresentation getEmptyRepresentation() {
        return emptyRepresentation;
    }
}
