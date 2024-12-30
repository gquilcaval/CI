package com.dms.tareosoft.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.tareosoft.R;

public class CustomToast extends Toast{
    private LinearLayout layout;
    private ImageView toastImage;
    private View toastView;
    private int color;
    private int durationToast; //Toast.LENGTH_LONG
    private Drawable icon;
    Context context;

    public CustomToast(Context context, LayoutInflater layoutActivity, String mensaje, int ToastLength) {
        super(context);
        this.context = context;

        toastView = layoutActivity.inflate(R.layout.toast_custom, null);
        layout = toastView.findViewById(R.id.toast_layout);
        TextView textView = toastView.findViewById(R.id.toast_message);
        toastImage = toastView.findViewById(R.id.toastImage);

        textView.setText(mensaje);
        durationToast = ToastLength;
    }

    @Override
    public void show(){
        this.setView(toastView);
        this.setDuration(durationToast);
        ((GradientDrawable) layout.getBackground()).setColor(color);
        toastImage.setImageDrawable(icon);
        super.show();
    }

    public static class Builder {
        private Context context;
        private LayoutInflater layout;
        private String mensaje;
        private int backgroundColor;
        private int duration;
        private Drawable icon ;
        CustomToast dialog;

        public Builder(Context context, LayoutInflater layout, String mensaje) {
            this.context = context;
            this.layout = layout;
            this.mensaje = mensaje; //Maximo 2 lineas 64 caracteres aprox
            this.icon =  context.getResources().getDrawable(R.drawable.ic_warning); // R.drawable.ic_warning;
            this.backgroundColor = context.getResources().getColor(R.color.colorWarning);
            this.duration = Toast.LENGTH_SHORT;
        }

        public CustomToast build() {
            dialog = new CustomToast(context, layout, mensaje,duration);
            dialog.color = backgroundColor;
            dialog.icon = icon;
            return dialog;
        }

        public Builder setBackgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }
    }
}
