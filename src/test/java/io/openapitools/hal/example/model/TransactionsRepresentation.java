package io.openapitools.hal.example.model;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a set of transactions as returned by the REST service.
 */
@Resource
public class TransactionsRepresentation {

    @EmbeddedResource("transactions")
    private Collection<TransactionRepresentation> transactions;

    private HALLink self;

    public Collection<TransactionRepresentation> getTransactions() {
        return Collections.unmodifiableCollection(transactions);
    }

    @Link
    public HALLink getSelf() {
        return self;
    }
}
