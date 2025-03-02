package br.com.picpay.backend.data.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AuthStatus {

    @JsonProperty("fail")
    Fail,

    @JsonProperty("success")
    Success

}
