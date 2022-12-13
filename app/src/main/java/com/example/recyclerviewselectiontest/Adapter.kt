package com.example.recyclerviewselectiontest

import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.contains
import androidx.core.util.remove
import androidx.core.util.size
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.recyclerviewselectiontest.databinding.ListItemBinding

class Adapter(
    private val actionListener: (itemStateArray: SparseBooleanArray) -> Unit
) : RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    private val TAG: String = "Adapter"
    private var lista = mutableListOf<Item>()
    private val itemStateArray = SparseBooleanArray()
    private var isSelectedMode = false

    fun setData(lista: MutableList<Item>) {
        this.lista = lista
        notifyItemRangeChanged(0, lista.size)
    }

    inner class ItemViewHolder(
        val binding: ListItemBinding
    ) : ViewHolder(binding.root) {
        fun bind(item: Item, isSelected: Boolean) {
            binding.textView.text = item.nome
            binding.cardView.isSelected = isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(lista[position], itemStateArray[holder.adapterPosition, false])


        holder.binding.root.setOnLongClickListener {
            if (!isSelectedMode) {
                if (itemStateArray.contains(holder.adapterPosition)) {
                    itemStateArray.remove(holder.adapterPosition, false)
                } else {
                    itemStateArray.put(holder.adapterPosition, true)
                }
                changeViewState(it)

                actionListener(itemStateArray)
            }

            isSelectedMode = true

            Log.i(TAG, "onBindViewHolder: itemStateArray: $itemStateArray")
//            Log.i(TAG, "onBindViewHolder: List: $position - Adapter ${holder.adapterPosition}")
            true
        }

        holder.binding.root.setOnClickListener {
            if (isSelectedMode) {
                if (itemStateArray.contains(holder.adapterPosition)) {
                    itemStateArray.remove(holder.adapterPosition, true)
                } else {
                    itemStateArray.put(holder.adapterPosition, true)
                }
                changeViewState(it)

                if (itemStateArray.size == 0) {
                    isSelectedMode = false
                }

                actionListener(itemStateArray)
                Log.i(TAG, "onBindViewHolder: itemStateArray: $itemStateArray")
            }
        }
    }

    override fun getItemCount() = lista.size

    private fun changeViewState(view: View) {
        view.isSelected = !view.isSelected
    }

    fun resetStateArray() {
        val arraySize = itemStateArray.size + 1
        itemStateArray.clear()
        notifyItemRangeChanged(0, arraySize)
        isSelectedMode = false
    }

    fun deleteSelectedItemns() {
        for (index in itemStateArray.size - 1 downTo 0) {
            Log.i(TAG, "deleteSelectedItemns: $index")
            Log.i(TAG, "deleteSelectedItemns: ${itemStateArray.keyAt(index)}")
            Log.i(TAG, "deleteSelectedItemns: ${lista[itemStateArray.keyAt(index)].nome}")

            val listIndex = itemStateArray.keyAt(index)
            lista.removeAt(listIndex)
            notifyItemRemoved(listIndex)
        }
        itemStateArray.clear()
        actionListener(itemStateArray)
        isSelectedMode = false
    }
}
