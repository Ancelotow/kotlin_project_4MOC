package com.oye.moviepedia.ui

import androidx.fragment.app.Fragment
import com.oye.moviepedia.MainActivity

open class BaseFragment: Fragment() {

    protected fun showLoader(){
        (activity as? MainActivity)?.showLoader()
    }

    protected fun hideLoader(){
        (activity as? MainActivity)?.hideLoader()
    }

}