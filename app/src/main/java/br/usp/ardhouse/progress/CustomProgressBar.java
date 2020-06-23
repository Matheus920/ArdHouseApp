package br.usp.ardhouse.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {
    public CustomProgressBar(Context paramContext) {
        super(paramContext);
    }

    public CustomProgressBar(Context paramContext,
                             AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public CustomProgressBar(Context paramContext,
                             AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void draw(Canvas paramCanvas) {
        int i = getMeasuredWidth();
        int j = getMeasuredHeight();
        paramCanvas.save();
        paramCanvas.rotate(135.0F, i / 2, j / 2);
        super.draw(paramCanvas);
        paramCanvas.restore();
    }
}