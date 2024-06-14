package com.kky.pokedex.main.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kky.pokedex.feature.main.databinding.ItemPokemonInfoBinding

class PokemonInfoRecyclerViewAdapter(
): RecyclerView.Adapter<PokemonInfoRecyclerViewAdapter.InfoViewHolder>() {

    var data: List<String> = listOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class InfoViewHolder(private val binding: ItemPokemonInfoBinding): ViewHolder(binding.root) {
        fun bind(info: String) {
            binding.tvContent.text = info
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding =
            ItemPokemonInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.bind(data[position])
    }
}