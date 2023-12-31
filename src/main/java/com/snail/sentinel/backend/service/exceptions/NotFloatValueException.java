package com.snail.sentinel.backend.service.exceptions;

import org.json.JSONException;

public class NotFloatValueException extends RuntimeException {
    public NotFloatValueException(JSONException e) {
        super("The value is not a Float! " + e);
    }
}
