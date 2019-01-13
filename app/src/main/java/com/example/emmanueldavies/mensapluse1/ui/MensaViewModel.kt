package com.example.emmanueldavies.mensapluse1.ui

import android.arch.lifecycle.ViewModel
import com.example.emmanueldavies.mensapluse1.data.Person
import com.example.emmanueldavies.newMensaplus.resipotory.MensaRepository
import javax.inject.Inject

class MensaViewModel @Inject constructor (val repository: MensaRepository) : ViewModel(){

}
