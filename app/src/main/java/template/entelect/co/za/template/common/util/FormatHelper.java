package template.entelect.co.za.template.common.util;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Locale;

import template.entelect.co.za.template.common.log.Logger;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class FormatHelper {

    private static int CURRENCY_MAX_LENGTH = 15;
    private static int CURRENCY_DECIMALS = 2;
    private static int QUANTITY_MAX_LENGTH = 15;
    private static int QUANTITY_DECIMALS = 5;
    private static int CHEMICALS_MAX_LENGTH = 15;
    private static int CHEMICALS_DECIMALS = 5;
    private static int FIELD_AREA_MAX_LENGTH = 7;
    private static int FIELD_AREA_DECIMALS = 2;
    private static int EXPECTED_YIELD_MAX_LENGTH = 15;
    private static int EXPECTED_YIELD_DECIMALS = 5;
    private static int SMALL_QUANTITY_MAX_LENGTH = 13;
    private static int SMALL_QUANTITY_DECIMALS = 2;

    private FormatHelper() {
    }

    private static DecimalFormatSymbols createDecimalFormatSymbols() {

        Currency instance = getCurrencyInstance();
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setCurrency(instance);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(',');

        return formatSymbols;
    }

    public static DecimalFormat getQuantityFormat(int decimalPlaces) {

        if (decimalPlaces <= 0) {
            return new DecimalFormat("#0");
        }
        DecimalFormat quantityFormat = new DecimalFormat("#0.0#");
        quantityFormat.setDecimalSeparatorAlwaysShown(true);
        quantityFormat.setDecimalFormatSymbols(FormatHelper.createDecimalFormatSymbols());
        quantityFormat.setMaximumIntegerDigits(QUANTITY_MAX_LENGTH - decimalPlaces);
        quantityFormat.setMaximumFractionDigits(decimalPlaces);

        return quantityFormat;
    }

    public static Currency getCurrencyInstance() {
        try {
            return Currency.getInstance(Locale.getDefault());
        } catch (IllegalArgumentException ex) {
            Logger.e("Failed to create currenct instance for locale " + Locale.getDefault(), ex);
            return Currency.getInstance(Locale.US);
        }
    }

    public static double parseDouble(TextView textView, String value, NumberFormat formatter) {
        Number parse = parse(textView, value, formatter);
        return parse != null ? parse.doubleValue() : 0;
    }

    public static int parseInt(TextView textView, String value, NumberFormat formatter) {
        Number parse = parse(textView, value, formatter);
        return parse != null ? parse.intValue() : 0;
    }

    private static Number parse(TextView textView, String value, NumberFormat formatter) {

        if (TextUtils.isEmpty(value))
            return 0;

        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            if (textView != null) {
                textView.setError("Invalid number.");
            }
        }
        return null;
    }

    public static double tryParse(NumberFormat format, EditText editText) {

        if (TextUtils.isEmpty(editText.getText()))
            return 0;

        try {
            return format.parse(editText.getText().toString()).doubleValue();
        } catch (ParseException e) {
            Logger.e("Failed to parse value " + editText.getText(), e);
            return 0;
        }
    }

    public static DateFormat getDateFormatter() {

        return getDateFormatter(false);
    }

    public static DateFormat getDateFormatter(boolean includeTime) {

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(new Locale("ZA"));
        return new SimpleDateFormat("dd MMM yyyy" + (includeTime ? ", HH:mm" : ""), dateFormatSymbols);
    }

    public static DateFormat getTimeFormatter() {

        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(new Locale("ZA"));
        return new SimpleDateFormat("HH:mm", dateFormatSymbols);
    }
}
