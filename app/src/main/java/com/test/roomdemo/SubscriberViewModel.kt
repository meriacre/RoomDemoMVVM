package com.test.roomdemo

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.roomdemo.db.Subscriber
import com.test.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel() {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    lateinit var subscriberUpdateOrDelete: Subscriber
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
    get() = statusMessage

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveOrUpdateButton = MutableLiveData<String>()
    val clearOrDeleteButton = MutableLiveData<String>()

    init {
        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear All"
    }

    fun saveOrUpdate(){
        if (inputName.value.isNullOrEmpty()){
            statusMessage.value = Event("Please enter subscriber's name!")
        }else if (inputEmail.value.isNullOrEmpty()){
            statusMessage.value = Event("Please enter subscriber's email!")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value).matches()){
            statusMessage.value = Event("Please enter correct email address!")
        }else{
        if (isUpdateOrDelete){
            subscriberUpdateOrDelete.name = inputName.value!!
            subscriberUpdateOrDelete.email = inputEmail.value!!
            update(subscriberUpdateOrDelete)
        }else{
        val name = inputName.value!!
        val email = inputEmail.value!!
        insert(Subscriber(0, name, email))
        inputName.value = ""
        inputEmail.value = ""}
    }
    }

    fun clearAllOrDelete(){
        if (isUpdateOrDelete){
            delete(subscriberUpdateOrDelete)
        }else {
            clearAll()
        }
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
            repository.insert(subscriber)
        withContext(Dispatchers.Main){
            statusMessage.value = Event("Subscriber inserted successfully!")
        }
    }
    fun update(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(subscriber)
        withContext(Dispatchers.Main){
            inputName.value = ""
            inputEmail.value = ""
            isUpdateOrDelete = false
            saveOrUpdateButton.value = "Save"
            clearOrDeleteButton.value = "Clear All"
            statusMessage.value = Event("Subscriber updated successfully!")
        }
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(subscriber)
        withContext(Dispatchers.Main){
        inputName.value = ""
        inputEmail.value = ""
        isUpdateOrDelete = false
        saveOrUpdateButton.value = "Save"
        clearOrDeleteButton.value = "Clear All"
        statusMessage.value = Event("Subscriber deleted successfully!")
        }
    }

    fun clearAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
        withContext(Dispatchers.Main){
            statusMessage.value = Event("All subscribers deleted successfully!")
        }
    }

    fun updateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberUpdateOrDelete = subscriber
        saveOrUpdateButton.value = "Update"
        clearOrDeleteButton.value = "Delete"
    }
}