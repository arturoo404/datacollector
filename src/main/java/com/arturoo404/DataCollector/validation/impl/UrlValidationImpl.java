package com.arturoo404.DataCollector.validation.impl;

import com.arturoo404.DataCollector.validation.UrlValidation;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UrlValidationImpl implements UrlValidation {

    private final String EMAIL_PATTERN = "^https://www\\.otodom\\.pl/pl/oferta/.*$";

    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public boolean validate(String url) {
        if (url == null){
            return false;
        }
        return pattern.matcher(url).matches();
    }
}
