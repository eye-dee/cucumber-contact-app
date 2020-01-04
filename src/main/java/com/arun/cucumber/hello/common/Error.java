package com.arun.cucumber.hello.common;

import java.util.HashMap;
import java.util.Map;

public class Error {

    Map<String, String> errors = new HashMap<>();

    public Error() {
        super();
    }

    public Error(Map<String, String> errors) {
        super();
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "Error [errors=" + this.errors + "]";
    }

}
