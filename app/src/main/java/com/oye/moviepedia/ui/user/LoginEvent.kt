package com.oye.moviepedia.ui.user

sealed class LoginEvent{

    object OnGetToken : LoginEvent()
}
