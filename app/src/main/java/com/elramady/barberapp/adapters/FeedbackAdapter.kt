package com.elramady.barberapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.barberapp.R
import com.elramady.barberapp.databinding.FeedbackLayoutBinding
import com.elramady.barberapp.models.CustomersBooking
import com.elramady.barberapp.models.Feedback

class FeedbackAdapter (val context: Context):
    RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {
    var feedbackList= ArrayList<Feedback>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : FeedbackLayoutBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.feedback_layout,parent,false)
        return FeedbackViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback: Feedback =feedbackList[position]
        holder.bind(feedback,context)
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }
    fun setList(listFeedback: ArrayList<Feedback>) {
        this.feedbackList= listFeedback
        notifyDataSetChanged()

    }


    inner class FeedbackViewHolder(val binding: FeedbackLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(feedback: Feedback, context: Context) {
            binding.commentFeedback.text=feedback.comment.toString()



        }

    }

}