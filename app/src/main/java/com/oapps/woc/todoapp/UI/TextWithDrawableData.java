package com.oapps.woc.todoapp.UI;

public class TextWithDrawableData {
    String text;
    int drawable, color;
    public long countPrimary;

    public TextWithDrawableData(String text, int drawable, int color) {
        this(text, drawable, color, 0);
    }

    public TextWithDrawableData(String text, int drawable, int color, long countPrimary) {
        this.text = text;
        this.drawable = drawable;
        this.color = color;
        this.countPrimary = countPrimary;
    }
}
