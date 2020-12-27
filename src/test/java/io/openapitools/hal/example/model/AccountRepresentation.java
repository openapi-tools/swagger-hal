package io.openapitools.hal.example.model;

import io.openapitools.jackson.dataformat.hal.HALLink;
import io.openapitools.jackson.dataformat.hal.annotation.Curie;
import io.openapitools.jackson.dataformat.hal.annotation.Curies;
import io.openapitools.jackson.dataformat.hal.annotation.EmbeddedResource;
import io.openapitools.jackson.dataformat.hal.annotation.Link;
import io.openapitools.jackson.dataformat.hal.annotation.Resource;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a single as returned from REST service.
 */
@Resource
@Curies({@Curie(href = "http://docs.my.site/{rel}", prefix = "account")})
public class AccountRepresentation {

    private String regNo;
    private String accountNo;
    private String name;

    @EmbeddedResource("transactions")
    private Collection<TransactionRepresentation> transactions;

    @Link(curie = "account", value = "transactions")
    private HALLink transactionsResource;

    @Link
    private HALLink self;

    public String getRegNo() {
        return regNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getName() {
        return name;
    }

    @Schema(description = "Embeds the latest transaction of account.")
    public Collection<TransactionRepresentation> getTransactions() {
        if (transactions == null) {
            return null;
        } else {
            return Collections.unmodifiableCollection(transactions);
        }
    }

    public HALLink getTransactionsResource() {
        return transactionsResource;
    }

    public HALLink getSelf() {
        return self;
    }
}
