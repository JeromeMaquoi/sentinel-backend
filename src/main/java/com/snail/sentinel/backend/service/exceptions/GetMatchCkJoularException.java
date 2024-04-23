package com.snail.sentinel.backend.service.exceptions;

public class GetMatchCkJoularException extends IllegalStateException {
    public GetMatchCkJoularException(String line) {super("Matched ck joular is not present for " + line);}
}
