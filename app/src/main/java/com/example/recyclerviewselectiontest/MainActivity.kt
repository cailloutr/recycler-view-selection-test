package com.example.recyclerviewselectiontest

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.util.size
import com.example.recyclerviewselectiontest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var adapter: Adapter
    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = Adapter(this) {
            when (it.size) {
                1 -> {
                    if (actionMode == null) {
                        actionMode = startSupportActionMode(callback)
                    }
                    actionMode?.title = it.size.toString()
                }
                0 -> {
                    actionMode?.finish()
                    actionMode = null
                }
                else -> {
                    actionMode?.title = it.size.toString()
                }
            }
        }

        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        _binding.recyclerView.adapter = adapter
        adapter.setData(getData())
    }

    private fun getData(): MutableList<Item> {

        val lista = mutableListOf<Item>()

        for (i in 1..100) {
            lista.add(Item("Teste $i"))
        }

        return lista

    }

    private val callback = object : ActionMode.Callback {

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.contextual_action_bar, menu)
            Log.i("ActionModeCallback", "onCreateActionMode: ")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            Log.i("ActionModeCallback", "onPrepareActionMode: ")
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.share -> {
                    // Handle share icon press
                    true
                }
                R.id.delete -> {
                    adapter.deleteSelectedItemns()
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            adapter.resetStateArray()
            actionMode = null
            Log.i("ActionModeCallback", "onDestroyActionMode: ")
        }
    }

}