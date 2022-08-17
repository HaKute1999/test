package com.example.inote.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/* loaded from: classes.dex */
public class CustomEditText extends EditText {
    selectChanged select;
    keyPreIme onkey;

    /* loaded from: classes.dex */
    public interface keyPreIme {
        void onKey();
    }

    /* loaded from: classes.dex */
    public interface selectChanged {
        void onChange(int i2, int i3);
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public void a(selectChanged bVar) {
        if (this.select == bVar) {
            this.select = null;
        }
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int i2, KeyEvent keyEvent) {
        keyPreIme aVar;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1 && (aVar = this.onkey) != null) {
            aVar.onKey();
        }
        return super.onKeyPreIme(i2, keyEvent);
    }

    @Override // android.widget.TextView
    protected void onSelectionChanged(int i2, int i3) {
        super.onSelectionChanged(i2, i3);
        selectChanged bVar = this.select;
        if (bVar != null) {
            bVar.onChange(i2, i3);
        }
    }

    public void setOnBackButtonPressedListener(keyPreIme aVar) {
        this.onkey = aVar;
    }

    public void setOnSelectionChangedListener(selectChanged bVar) {
        this.select = bVar;
    }

    public CustomEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomEditText(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
    }

    @TargetApi(21)
    public CustomEditText(Context context, AttributeSet attributeSet, int i2, int i3) {
        super(context, attributeSet, i2, i3);
    }
}
