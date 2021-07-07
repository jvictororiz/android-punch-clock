package br.com.jv.lembreteponto.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.jv.lembreteponto.R
import br.com.jv.lembreteponto.databinding.ActivitySettingsBinding
import br.com.jv.lembreteponto.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private val viewModel by viewModel<SettingsViewModel>()
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivitySettingsBinding>(this, R.layout.activity_settings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel.init()

        viewModel.currentSettings.observe(this) {
            binding.edtHourAlmoco.setText(it.timeMinuteAlmoco.toString())
            binding.edtPackage.setText(it.urlOpen)
            binding.edtHourTotal.setText(it.totalTime.toString())
        }

        viewModel.successSave.observe(this, {
            finish()
        })

        binding.btnTutorial.setOnClickListener {

        }

        binding.btnSave.setOnClickListener {
            viewModel.saveChanges(
                binding.edtPackage.text.toString(),
                binding.edtHourAlmoco.text.toString(),
                binding.edtHourTotal.text.toString(),
            )
        }
    }
}