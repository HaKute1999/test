package com.example.inote.view.drawingview.brushes;


import android.graphics.Canvas;

import com.example.inote.view.drawingview.DrawingEvent;

public interface BrushRenderer {
    void draw(Canvas canvas);
    void onTouch(DrawingEvent drawingEvent);
    void setBrush(Brush brush);
}
