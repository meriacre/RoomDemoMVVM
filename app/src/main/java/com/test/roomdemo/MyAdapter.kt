package com.test.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.roomdemo.databinding.ListItemBinding
import com.test.roomdemo.db.Subscriber

class MyAdapter( private val clickListener:(Subscriber) -> Unit): RecyclerView.Adapter<MyViewHolder>() {

    private var subscribersList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribersList[position])
    }

    override fun getItemCount(): Int = subscribersList.size

    fun setList(list: List<Subscriber>){
        subscribersList.clear()
        this.subscribersList = list as ArrayList<Subscriber>
        notifyDataSetChanged()
    }

}

class MyViewHolder(val binding: ListItemBinding,val clickListener: (Subscriber) -> Unit): RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber: Subscriber){
        binding.tvName.text = subscriber.name
        binding.tvEmail.text = subscriber.email
        binding.root.setOnClickListener { clickListener(subscriber) }
    }


}