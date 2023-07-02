package com.oye.moviepedia.ui

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.oye.moviepedia.MainActivity

open class BaseFragment: Fragment() {

    protected fun showLoader(){
        (activity as? MainActivity)?.showLoader()
    }

    protected fun hideLoader(){
        (activity as? MainActivity)?.hideLoader()
    }

    protected fun setupSupportActionBar(toolbar: Toolbar){
        (activity as? MainActivity)?.setSupportActionBar(toolbar)
    }

    protected fun onSupportNavigateUp(){
        (activity as? MainActivity)?.navController?.navigateUp()
    }

}