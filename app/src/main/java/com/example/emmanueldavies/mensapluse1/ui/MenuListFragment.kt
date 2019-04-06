package com.example.emmanueldavies.mensapluse1.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.emmanueldavies.mensapluse1.R
import com.example.emmanueldavies.mensapluse1.databinding.FragmentMenuListBinding
import com.example.emmanueldavies.mensapluse1.di.Injectable

private const val TAB_TITLE = "param2"

class MenuListFragment : Fragment(), Injectable {

    private var tabTitle: String? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var binding: FragmentMenuListBinding
    private var canteemNames: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabTitle = it.getString(TAB_TITLE)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_list, container, false)

        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTextView.text = tabTitle

        initLayout()

        (activity as MainActivity).viewState.reobserve(activity as MainActivity
        ) {
        }

    }

    private fun initLayout() {
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = (activity as MainActivity).mensaViewModel.mealAdapter
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(pageTitle: String) =
            MenuListFragment().apply {
                arguments = Bundle().apply {
                    putString(TAB_TITLE, pageTitle)
                }
            }
    }


    override fun onPause() {
        super.onPause()
        (activity as MainActivity).viewState.removeObservers(this)
    }


}

//extension function
inline fun <T> LiveData<T>.reobserve(owner: LifecycleOwner, crossinline func: (T?) -> (Unit)) {
    removeObservers(owner)
    observe(owner, Observer<T> { t -> func(t) })
}

