package com.example.whatsappclone.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Adapter.ChatsRecyclerViewAdapter
import com.example.whatsappclone.Model.ChatsModel
import com.example.whatsappclone.R
import com.example.whatsappclone.ViewModel.UsersViewModel
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    lateinit var viewModel : UsersViewModel
    var list = ArrayList<ChatsModel>()
    var adapter = ChatsRecyclerViewAdapter(list, this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UsersViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        users_rv.layoutManager = LinearLayoutManager(context)
        users_rv.adapter = adapter
        viewModel.getSohbet()
        observeViewModel()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeViewModel() {
        viewModel.list.observe(viewLifecycleOwner, Observer {
            it?.let {
                list = it
                adapter.updateList(list)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    users_progressBar.visibility = View.VISIBLE
                }else {
                    users_progressBar.visibility = View.GONE
                }
            }
        })
    }

}