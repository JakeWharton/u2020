package com.jakewharton.u2020.data;

import java.lang.annotation.Retention;
import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier @Retention(RUNTIME)
public @interface NetworkVariancePercent {
}
