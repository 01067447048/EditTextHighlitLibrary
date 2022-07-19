package com.jaehyeon.compose.highlightedittext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.jaehyeon.compose.highlightedittext.databinding.EdittextTagBinding

/**
 * Created by Jaehyeon on 2022/07/20.
 */
@SuppressLint("AppCompatCustomView", "SetTextI18n")
class TagEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private var regex = Regex("#([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]{1,15})")
    private val binding = EdittextTagBinding.inflate(LayoutInflater.from(context), this, true)
    var contents = ""
        get() {
            field = binding.editText.text.toString()
            return field
        }
        set(value) {
            binding.editText.setText(value)
        }

    init {
        binding.editText.addTextChangedListener {
            removeSpannable(it.toString())
            handleTag(it.toString())
        }
    }

    fun setRegex(regex: String) {
        this.regex = Regex(regex)
    }

    fun setRegex(count: Int) {
        val regexString = "#([ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]{1,$count})"
        this.regex = Regex(regexString)
    }

    private fun removeSpannable(text: String) {
        binding.editText.text?.getSpans(0, text.length, ForegroundColorSpan::class.java)?.let {
            it.forEach {span ->
                binding.editText.text?.removeSpan(span)
            }
        }
    }

    private fun handleTag(text: String?) {
        var lastIndex = 0

        val match = regex.findAll(text.toString())
        if (match.count() > 0) {

            match.forEach { tag ->
                val foregroundSpan =
                    ForegroundColorSpan(Color.RED)
                binding.editText.text?.setSpan(
                    foregroundSpan,
                    text.toString().indexOf(tag.value, lastIndex),
                    text.toString().indexOf(tag.value, lastIndex) + tag.value.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                lastIndex = text.toString().indexOf(tag.value, lastIndex) + tag.value.length - 1
            }
        }
    }

}