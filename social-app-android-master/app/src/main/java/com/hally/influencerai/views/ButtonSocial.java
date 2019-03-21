package com.hally.influencerai.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.hally.influencerai.R;
import com.hally.influencerai.utils.Utils;

/**
 * Created by HallyTran on 3/21/2019.
 * transon97uet@gmail.com
 */
public class ButtonSocial extends AppCompatButton {

    private Bitmap mIcon;
    private Paint mPaint;
    private Rect mSrcRect;
    private int textColor = Color.WHITE;
    private int mIconPadding;
    private int mIconSize;
    private int mRoundedCornerRadius;
    private boolean mIconCenterAligned;
    private boolean mRoundedCorner;
    private boolean mTransparentBackground;

    public ButtonSocial(Context context) {
        super(context);
    }

    public ButtonSocial(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Recalculate width and amount to shift by, taking into account icon size
        int shift = (mIconSize + mIconPadding) / 2;

        canvas.save();
        canvas.translate(shift, 0);

        super.onDraw(canvas);

        float textWidth = getPaint().measureText(getText().toString());
        int left = (int) ((getWidth() / 2f) - (textWidth / 2f) - mIconSize - mIconPadding);
        int top = getHeight() / 2 - mIconSize / 2;
        if (!mIconCenterAligned) left = 0;

        Rect destRect = new Rect(left, top, left + mIconSize, top + mIconSize);
        canvas.drawBitmap(mIcon, mSrcRect, destRect, mPaint);

        canvas.restore();
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.SocialButton,
                0, 0);
        int social = array.getInteger(R.styleable.SocialButton_social, 0);
        String buttonClass = getResources().getStringArray(R.array.social_available_buttons)[social];
        TypedArray ar = context.getResources().obtainTypedArray(getResources().getIdentifier(buttonClass,
                "array", context.getPackageName()));
        int colorNormal = ar.getColor(0, 0);
        @SuppressLint("ResourceType") Drawable logo = ar.getDrawable(1);
        ar.recycle();

        // Initialize variables to default values
        setDefaultValues(context, logo);
        setStyle(context, colorNormal);

        // Don't add padding when text isn't present
        String text = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
        mIconPadding = (int) Utils.convertDpToPixel(text != null ? 20 : 0, context);

        textColor = array.getColor(R.styleable.SocialButton_android_textColor, Color.WHITE);
        mIconPadding = array.getDimensionPixelSize(R.styleable.SocialButton_iconPadding, mIconPadding);
        mIconCenterAligned = array.getBoolean(R.styleable.SocialButton_iconCenterAligned, mIconCenterAligned);
        mIconSize = array.getDimensionPixelSize(R.styleable.SocialButton_iconSize, mIconSize);
        mRoundedCorner = array.getBoolean(R.styleable.SocialButton_roundedCorner, mRoundedCorner);
        mRoundedCornerRadius = array.getDimensionPixelSize(R.styleable.SocialButton_roundedCornerRadius, mRoundedCornerRadius);
        mTransparentBackground = array.getBoolean(R.styleable.SocialButton_transparentBackground, mTransparentBackground);
        array.recycle();

        if (mIcon != null) {
            mPaint = new Paint();
            mSrcRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
        }
    }

    private void setStyle(Context context, int color) {
        setTextColor(this.textColor);

        setBackgroundResource(R.drawable.round_corner);
        GradientDrawable drawable = (GradientDrawable) getBackground().mutate();
        drawable.setColor(color);
        drawable.setCornerRadius(0);

        if (mRoundedCorner)
            drawable.setCornerRadius(mRoundedCornerRadius);

        if (mTransparentBackground) {
            drawable.setColor(Color.TRANSPARENT);
            drawable.setStroke(8, getResources().getColor(color));
        }

        drawable.invalidateSelf();

        setPadding((int) Utils.convertDpToPixel(30, context), 0,
                (int) Utils.convertDpToPixel(30, context), 0);
    }

    private void setDefaultValues(Context context, Drawable logo) {
        mIcon = Utils.drawableToBitmap(logo);
        mIconSize = (int) Utils.convertDpToPixel(25, context);
        mIconCenterAligned = false;
        mRoundedCorner = true;
        mTransparentBackground = false;
        mRoundedCornerRadius = (int) Utils.convertDpToPixel(40, context);
    }
}
