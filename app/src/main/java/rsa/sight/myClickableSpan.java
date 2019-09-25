package rsa.sight;

import android.content.Context;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

class MyClickableSpan extends ClickableSpan {
    OnClickListener mListener;

    public MyClickableSpan(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View widget) {
        mListener.onClick(widget);
    }
}