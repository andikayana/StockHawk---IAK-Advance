package com.udacity.stockhawk.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by andika on 09/12/17.
 */

public final class MyNumberFormat {

    public static DecimalFormat dollarFormat(){
        return (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US);
    }

    public static DecimalFormat dollarFormatWithPlus(){
        DecimalFormat df = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US);
        df.setPositivePrefix("+$");
        return df;

    } public static DecimalFormat percentageFormat(){
        DecimalFormat df = (DecimalFormat)NumberFormat.getPercentInstance(Locale.getDefault());
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setPositivePrefix("+");
        return df;
    }
}
