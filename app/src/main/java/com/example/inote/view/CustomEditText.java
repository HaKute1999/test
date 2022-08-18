package com.example.inote.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

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
        if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == KeyEvent.ACTION_UP && (aVar = this.onkey) != null) {
            Toast.makeText(getContext(),"cdjcndcjdc",Toast.LENGTH_SHORT).show();
            aVar.onKey();
        }
        return super.onKeyPreIme(i2, keyEvent);
    }

    @Override // android.widget.TextView
    protected void onSelectionChanged(int i2, int i3) {
        super.onSelectionChanged(i2, i3);
        selectChanged bVar = this.select;
//        Toast.makeText(getContext(),"cdcdcdcdcdcn",Toast.LENGTH_SHORT).show();

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
