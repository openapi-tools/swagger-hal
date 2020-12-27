package io.openapitools.hal.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a set of accounts from the REST service exposure.
 */
@Resource
public class AccountsRepresentation {

    @Link
    private HALLink self;

    @JsonProperty("accountList")
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
