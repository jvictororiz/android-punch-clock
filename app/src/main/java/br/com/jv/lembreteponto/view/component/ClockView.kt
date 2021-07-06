package br.com.jv.lembreteponto.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import br.com.jv.lembreteponto.R
import br.com.jv.lembreteponto.databinding.ComponentClockViewBinding

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var hasPreview: Boolean = false

    private val binding by lazy {
        ComponentClockViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setOnLongClickListener(listener: () -> Unit) {
        binding.llBodyView.setOnLongClickListener {
            listener.invoke()
            true
        }
    }

    fun bind(label: String) {
        binding.tvLabel.text = label
    }

    fun setHint(label: String) {
        hasPreview = true
        binding.tvValue.alpha = 0.5F
        binding.tvValue.setTextColor(ContextCompat.getColor(context, R.color.gray_600))
        binding.tvValue.text = label
    }

    fun update(time: String) {
        hasPreview = false
        binding.tvValue.alpha = 1F
        binding.tvValue.setTextColor(ContextCompat.getColor(context, R.color.color_primary))
        binding.tvValue.text = time
    }

    fun clear() {
        hasPreview = false
        binding.tvValue.alpha = 0.5F
        binding.tvValue.setTextColor(ContextCompat.getColor(context, R.color.color_primary))
        binding.tvValue.text = context.getString(R.string.clear_time)
    }

    fun currentTime() = binding.tvValue.text.toString()
}