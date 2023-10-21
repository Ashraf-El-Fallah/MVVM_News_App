package com.af.newsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.af.newsapp.databinding.FragmentSavedNewsBinding
import com.af.newsapp.ui.NewsActivity
import com.af.newsapp.ui.NewsViewModel

class SavedNewsFragment : Fragment() {
    private var _binding: FragmentSavedNewsBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}