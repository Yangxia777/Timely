package com.xy.timetracker.view;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.view.View;
import android.widget.Checkable;

import com.xy.timetracker.R;

public class CheckablePalette extends View implements Checkable {
    private boolean mChecked;
    private ShapeDrawable bgDrawable;
    private LayerDrawable bgDrawableChecked;

    public CheckablePalette(Context context, int color) {
        super(context);

        float[] outerR = new float[]{8, 8, 8, 8, 8, 8, 8, 8};
        RectF inset = new RectF(0, 0, 0, 0);
        float[] innerRadii = new float[]{8, 8, 8, 8, 8, 8, 8, 8};
        Shape shape = new RoundRectShape(outerR, inset, innerRadii);
        Shape shapeChecked = new RoundRectShape(outerR, inset, innerRadii);

        bgDrawable = new ShapeDrawable(shape);
        bgDrawable.getPaint().setColor(color);
        setBackground(bgDrawable);

        ShapeDrawable underLayerDrawable = new ShapeDrawable(shapeChecked);
        underLayerDrawable.getPaint().setColor(color);
        Drawable[] layers = new Drawable[2];
        layers[0] = underLayerDrawable;
        layers[1] = getResources().getDrawable(R.drawable.check);
        bgDrawableChecked = new LayerDrawable(layers);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;

            if (mChecked) {
                setBackground(bgDrawableChecked);
            } else {
                setBackground(bgDrawable);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
      setChecked(!mChecked);
    }
}
