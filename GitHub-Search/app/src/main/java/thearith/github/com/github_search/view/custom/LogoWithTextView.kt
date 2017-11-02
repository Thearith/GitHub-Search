package thearith.github.com.github_search.view.custom

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import thearith.github.com.github_search.R

/**
 * Created by Thearith on 11/1/17.
 */

class LogoWithTextView : LinearLayout {

    private val ivLogo : ImageView
    private val tvMessage : TextView

    constructor(context : Context, attrs : AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_logo_with_text_view, this);
        ivLogo = findViewById(R.id.iv_logo) as ImageView
        tvMessage = findViewById(R.id.tv_message) as TextView

        gravity = Gravity.CENTER
        orientation = VERTICAL

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogoWithTextViewAttrs)
        val backgroundId = typedArray.getResourceId(R.styleable.LogoWithTextViewAttrs_background, R.drawable.logo_welcome)
        val text = typedArray.getString(R.styleable.LogoWithTextViewAttrs_text)
        setBackground(backgroundId)
        setText(text)
    }

    public fun setBackground(@DrawableRes resId : Int) {
        ivLogo.setBackgroundResource(resId)
    }

    public fun setText(message : String) {
        tvMessage.text = message
    }



}