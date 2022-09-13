package com.example.loginapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_log.*


class FragmentLog : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entryBtn.setOnClickListener { loadFragmentField() }
    }

    private fun loadFragmentField() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.frameLayout, FragmentField(), "DetailFragment")
            ?.addToBackStack(FragmentField::class.java.canonicalName)
            ?.commit()
    }
}