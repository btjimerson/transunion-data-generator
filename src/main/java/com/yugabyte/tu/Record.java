package com.yugabyte.tu;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Record implements Serializable {

    @JsonProperty("acct_id")
    Long acctId;

    @JsonProperty("party_id")
    Long partyId;
}
