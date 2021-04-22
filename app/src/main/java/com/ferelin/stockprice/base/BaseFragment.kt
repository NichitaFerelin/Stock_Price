package com.ferelin.stockprice.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ferelin.shared.CoroutineContextProvider
import com.ferelin.stockprice.dataInteractor.DataInteractor
import com.ferelin.stockprice.ui.MainActivity
import kotlinx.coroutines.launch

/*
* BaseFragment is the fundament for fragment where is needed:
*   - Data source
*   - View Model
*   - View Helper
* */
abstract class BaseFragment<out T : BaseDataViewModel, out V : BaseViewHelper>(
    protected val mCoroutineContext: CoroutineContextProvider = CoroutineContextProvider()
) : Fragment() {

    protected abstract val mViewModel: T
    protected abstract val mViewHelper: V
    protected lateinit var mDataInteractor: DataInteractor

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataInteractor = (activity as MainActivity).dataInteractor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.triggerCreate()
        setUpViewComponents(savedInstanceState)
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewHelper.invalidate()
    }

    protected open fun setUpViewComponents(savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch(mCoroutineContext.IO) {
            mViewHelper.prepare(requireContext())
        }
    }

    protected open fun initObservers() {
        mViewModel.initObservers()
    }

    protected fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}