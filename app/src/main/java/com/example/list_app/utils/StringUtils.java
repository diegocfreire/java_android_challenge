package com.example.list_app.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.text.Collator;

public class StringUtils {

    private static final Collator sCollator;

    static {
        sCollator = Collator.getInstance();
        sCollator.setStrength(Collator.TERTIARY);
    }

    public static float toFloat(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0f;
        }

        try {
            value = value.replace(",", ".");
            return Float.parseFloat(value.trim());
        } catch (NumberFormatException ex) {
            return Float.NaN;
        }
    }

    public static double toDouble (String value){
        if (TextUtils.isEmpty(value)) {
            return 0f;
        }

        try{
            value = value.replace(",", ".");
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex){
            return Double.NaN;
        }
    }

    public static int toPositiveInt(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    public static Collator getCollatorInstance() {
        return sCollator;
    }

    /**
     * Compara dois argumentos {@link String} e retorna um integer indicando se o primeiro
     * argumento é menor, igual ou maior que o segundo.
     *
     * @param input1 Primeiro valor string a ser comparado.
     * @param input2 Segundo valor string a ser comparado.
     * @return Retorna -1, 0 ou 1 indicando se o primeiro argumento é menor, igual ou maior que o
     * segundo.
     */
    public static int compare(String input1, String input2) {
        return getCollatorInstance().compare(input1, input2);
    }

    public static CharSequence join(CharSequence... content) {
        return apply(content);
    }

    /**
     * Returns a CharSequence that applies boldface to the concatenation
     * of the specified CharSequence objects.
     */
    public static CharSequence bold(CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.BOLD));
    }

    /**
     * Returns a CharSequence that applies italics to the concatenation
     * of the specified CharSequence objects.
     */
    public static CharSequence italic(CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.ITALIC));
    }

    /**
     * Returns a CharSequence that applies a foreground color to the
     * concatenation of the specified CharSequence objects.
     */
    public static CharSequence color(int color, CharSequence... content) {
        return apply(content, new ForegroundColorSpan(color));
    }

    /**
     * Returns a CharSequence that concatenates the specified array of CharSequence
     * objects and then applies a list of zero or more tags to the entire range.
     *
     * @param content an array of character sequences to apply a style to
     * @param tags    the styled span objects to apply to the content
     *                such as android.text.style.StyleSpan
     */
    private static CharSequence apply(CharSequence[] content, Object... tags) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        openTags(text, tags);
        for (CharSequence item : content) {
            text.append(item);
        }
        closeTags(text, tags);
        return text;
    }

    /**
     * Iterates over an array of tags and applies them to the beginning of the specified
     * Spannable object so that future text appended to the text will have the styling
     * applied to it. Do not call this method directly.
     */
    private static void openTags(Spannable text, Object[] tags) {
        for (Object tag : tags) {
            text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
        }
    }

    /**
     * "Closes" the specified tags on a Spannable by updating the spans to be
     * endpoint-exclusive so that future text appended to the end will not take
     * on the same styling. Do not call this method directly.
     */
    private static void closeTags(Spannable text, Object[] tags) {
        int len = text.length();
        for (Object tag : tags) {
            if (len > 0) {
                text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                text.removeSpan(tag);
            }
        }
    }
}
