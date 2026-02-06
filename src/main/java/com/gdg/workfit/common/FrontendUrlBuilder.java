package com.gdg.workfit.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FrontendUrlBuilder {

    private final String baseUrl;

    public FrontendUrlBuilder(@Value("${app.frontend.base-url}") String baseUrl) {
        this.baseUrl = normalize(baseUrl);
    }

    public String submissionResult(Long submissionId) {
        return baseUrl + "/submissions/" + submissionId + "/result";
    }

    public String submissionComplete(Long submissionId) {
        return baseUrl + "/submissions/" + submissionId + "/submitted";
    }

    private String normalize(String value) {
        if (value.endsWith("/")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }
}
