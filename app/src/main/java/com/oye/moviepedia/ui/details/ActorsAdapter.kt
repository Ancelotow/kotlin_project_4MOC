package com.oye.moviepedia.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oye.moviepedia.databinding.ItemActorBinding
import com.oye.moviepedia.domain.entities.Cast
import com.squareup.picasso.Picasso

class ActorsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var castList: List<Cast>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ActorViewHolder = holder as ActorViewHolder

        castList?.let {
            val cast = it[viewHolder.adapterPosition]
            //viewHolder.adapterPosition
            Picasso.get().load(cast.picturePath).into(viewHolder.itemBinding.actorPicture)
            viewHolder.itemBinding.actorName.text = cast.originalName
            viewHolder.itemBinding.actorRole.text = cast.character
        }
    }

    override fun getItemCount(): Int {
        return castList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(castList: List<Cast>?){
        this.castList = castList
        notifyDataSetChanged()
    }

    class ActorViewHolder(val itemBinding: ItemActorBinding): RecyclerView.ViewHolder(itemBinding.root)
}