package br.com.jv.lembreteponto.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import br.com.jv.lembreteponto.R
import br.com.jv.lembreteponto.databinding.ComponentChangeClockBinding
import br.com.jv.lembreteponto.ext.*
import java.util.*

class ChangePickerTimeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var isOpen: Boolean = false

    private val binding by lazy {
        ComponentChangeClockBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        binding.timePicker.setIs24HourView(true)
    }


    fun showWithListener(time: String, listener: (hourOfDay: Int, minute: Int) -> Unit) {
        isOpen = true
        var currentDate = time
        if (isClearText(currentDate)) {
            currentDate = Date().dateToString("HH:mm")
        }
        binding.timePicker.hour = currentDate.toDate("HH:mm").hours()
        binding.timePicker.minute = currentDate.toDate("HH:mm").minutes()
        binding.llBodyPicker.isVisible = true
        binding.btnOk.setOnClickListener {
            dismiss()
            listener.invoke(binding.timePicker.hour, binding.timePicker.minute)
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun isClearText(text: String) = text == context.getString(R.string.clear_time)


    fun dismiss() {
        isOpen = false
        binding.llBodyPicker.isVisible = false
    }
}