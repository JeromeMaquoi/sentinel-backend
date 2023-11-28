package com.snail.sentinel.backend.service.exceptions;

import org.json.JSONException;

public class AstElemNotKnownException extends IllegalArgumentException {
    public AstElemNotKnownException(JSONException e) {
        super("Ast element not known! " + e);
    }
}
