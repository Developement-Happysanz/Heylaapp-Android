package com.palprotech.heylaapp.interfaces;

/**
 * Created by Narendar on 09/11/17.
 */
import org.joda.time.LocalDate;

public interface TitleValueCallback {
    void onTitleValueReturned(LocalDate date);
}