package com.palprotech.heylaapp.interfaces;


import androidx.annotation.Nullable;

import org.joda.time.LocalDate;

/**
 * Created by Narendar on 09/11/17.
 */

public interface OnChildDateSelectedListener {
    void onDateSelectedChild(@Nullable LocalDate date);
}
