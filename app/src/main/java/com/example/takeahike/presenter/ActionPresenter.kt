package com.example.takeahike.presenter

interface ActionPresenter<TAction, TViewModel> : Presenter<TViewModel> {
    val updateUIAction: PresenterEvent<TAction>
}