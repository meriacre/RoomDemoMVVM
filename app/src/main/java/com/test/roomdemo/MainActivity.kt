package com.test.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.roomdemo.databinding.ActivityMainBinding
import com.test.roomdemo.db.Subscriber
import com.test.roomdemo.db.SubscriberDAO
import com.test.roomdemo.db.SubscriberDataBase
import com.test.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = SubscriberDataBase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory)[SubscriberViewModel::class.java]
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecycleView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecycleView(){
        binding.rvSubs.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter({selectedItem:Subscriber ->clickedSub(selectedItem)})
        binding.rvSubs.adapter = adapter
        displaySubscribersList()

    }

    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.d("MyLog", it.toString())
            adapter.setList(it)
        })

    }

    private fun clickedSub(subscriber: Subscriber){
//        Toast.makeText(this, "Clicked: ${subscriber.name}", Toast.LENGTH_SHORT).show()
        subscriberViewModel.updateAndDelete(subscriber)
    }
}