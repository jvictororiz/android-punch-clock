package br.com.jv.lembreteponto.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.jv.lembreteponto.R
import br.com.jv.lembreteponto.data.entities.ClockType
import br.com.jv.lembreteponto.databinding.ActivityMainBinding
import br.com.jv.lembreteponto.ext.cancelNotification
import br.com.jv.lembreteponto.ext.scheduleNotification
import br.com.jv.lembreteponto.viewModel.ClockViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<ClockViewModel>()
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        viewModel.init()
        setupListeners()

        viewModel.clockUpdateView.observe(this) {
            when (it.clockType) {
                is ClockType.Entrada -> binding.viewEntrada.update(it.time)
                is ClockType.Almoco -> binding.viewAlmoco.update(it.time)
                is ClockType.RetornoAlmoco -> binding.viewRetornoAlmoco.update(
                    it.time
                )
                is ClockType.Saida -> binding.viewSaida.update(it.time)
                else -> {
                    Toast.makeText(this, getString(R.string.error_default), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.clockUpdateHintViewAlmoco.observe(this) {
            binding.viewRetornoAlmoco.setHint(it)
        }

        viewModel.clockUpdateHintViewSaida.observe(this) {
            binding.viewSaida.setHint(it)
        }

        viewModel.clearClock.observe(this) {
            when (it) {
                is ClockType.Entrada -> binding.viewEntrada.clear()
                is ClockType.Almoco -> binding.viewAlmoco.clear()
                is ClockType.RetornoAlmoco -> binding.viewRetornoAlmoco.clear()
                is ClockType.Saida -> binding.viewSaida.clear()
                else -> {
                    Toast.makeText(this, getString(R.string.error_default), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.goToScreen.observe(this, {
            packageManager.getLaunchIntentForPackage(it)?.let { intent ->
                startActivity(intent)
            }
        })

        viewModel.scheduleNotification.observe(this) {
            scheduleNotification(this, it)
        }

        viewModel.cancelScheduleNotification.observe(this) {
            cancelNotification(this, it)
        }
    }

    private fun setupListeners() {
        binding.btnClock.setOnClickListener {
            viewModel.tapOnClock()
        }
        binding.btnClear.setOnClickListener {
            viewModel.tapOnClear()
        }

        binding.toolbar.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.viewEntrada.setOnLongClickListener {
            binding.timePicker.showWithListener(binding.viewEntrada.currentTime()) { hourOfDay, minute ->
                viewModel.changeTime(ClockType.Entrada, hourOfDay, minute)
            }
        }

        binding.viewAlmoco.setOnLongClickListener {
            binding.timePicker.showWithListener(binding.viewAlmoco.currentTime()) { hourOfDay, minute ->
                viewModel.changeTime(ClockType.Almoco, hourOfDay, minute)
            }
        }

        binding.viewRetornoAlmoco.setOnLongClickListener {
            binding.timePicker.showWithListener(binding.viewRetornoAlmoco.currentTime()) { hourOfDay, minute ->
                viewModel.changeTime(ClockType.RetornoAlmoco, hourOfDay, minute)
            }
        }

        binding.viewSaida.setOnLongClickListener {
            binding.timePicker.showWithListener(binding.viewSaida.currentTime()) { hourOfDay, minute ->
                viewModel.changeTime(ClockType.Saida, hourOfDay, minute)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.timePicker.isOpen) {
            binding.timePicker.dismiss()
        } else {
            super.onBackPressed()
        }
    }

    private fun bindViews() {
        binding.viewEntrada.bind(getString(R.string.message_entrada))
        binding.viewAlmoco.bind(getString(R.string.message_almoco))
        binding.viewRetornoAlmoco.bind(getString(R.string.message_retorno_almoco))
        binding.viewSaida.bind(getString(R.string.message_saida))
    }
}