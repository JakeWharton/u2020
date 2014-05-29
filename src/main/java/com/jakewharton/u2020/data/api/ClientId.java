package com.jakewharton.u2020.data.api;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** The Imgur application client ID. */
@Qualifier @Retention(RUNTIME)
public @interface ClientId {
}
