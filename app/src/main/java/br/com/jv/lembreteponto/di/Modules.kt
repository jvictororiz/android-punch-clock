package br.com.jv.lembreteponto.di

import android.content.Context
import br.com.jv.lembreteponto.data.Preferences
import br.com.jv.lembreteponto.useCase.ClockUseCase
import br.com.jv.lembreteponto.viewModel.ClockViewModel
import br.com.jv.lembreteponto.viewModel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    factory {
        androidContext().applicationContext.getSharedPreferences(
            Preferences.PREFERENCES_APP,
            Context.MODE_PRIVATE
        )
    }
    single { Preferences(get()) }
    single { ClockUseCase(get(), get()) }
    viewModel { ClockViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}