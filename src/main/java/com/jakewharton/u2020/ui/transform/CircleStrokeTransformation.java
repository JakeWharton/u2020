package com.jakewharton.u2020.ui.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import com.squareup.picasso.Transformation;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Shader.TileMode.CLAMP;

public class CircleStrokeTransformation implements Transformation {
  private final float strokeWidth;
  private final Paint strokePaint;
  private final int strokeColor;

  public CircleStrokeTransformation(Context context, int strokeColor, int strokeWidthDp) {
    this.strokeColor = strokeColor;
    this.strokeWidth = strokeWidthDp * context.getResources().getDisplayMetrics().density;

    strokePaint = new Paint(ANTI_ALIAS_FLAG);
    strokePaint.setStyle(Paint.Style.STROKE);
    strokePaint.setColor(strokeColor);
  }

  @Override public Bitmap transform(Bitmap bitmap) {
    int size = bitmap.getWidth();
    Bitmap rounded = Bitmap.createBitmap(size, size, ARGB_8888);
    Canvas canvas = new Canvas(rounded);

    BitmapShader shader = new BitmapShader(bitmap, CLAMP, CLAMP);
    Paint shaderPaint = new Paint(ANTI_ALIAS_FLAG);
    shaderPaint.setShader(shader);

    RectF rect = new RectF(0, 0, size, size);
    float radius = size / 2f;
    canvas.drawRoundRect(rect, radius, radius, shaderPaint);

    strokePaint.setStrokeWidth(strokeWidth);

    float strokeInset = strokeWidth / 2f;
    rect.inset(strokeInset, strokeInset);
    float strokeRadius = radius - strokeInset;
    canvas.drawRoundRect(rect, strokeRadius, strokeRadius, strokePaint);

    bitmap.recycle();
    return rounded;
  }

  @Override public String key() {
    return "circle_stroke(" + strokeColor + "," + strokeWidth + ")";
  }
}
