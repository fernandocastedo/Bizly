package com.bizly.app.presentation.base;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * Clase base para todos los ViewModels del proyecto.
 * Proporciona manejo común de estados y errores.
 */
public abstract class BaseViewModel extends ViewModel {

    protected MutableLiveData<BaseState> state = new MutableLiveData<>();
    protected MutableLiveData<String> errorMessage = new MutableLiveData<>();
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<BaseState> getState() {
        return state;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    protected void setLoading(boolean loading) {
        isLoading.postValue(loading);
    }

    protected void setError(String error) {
        errorMessage.postValue(error);
        setLoading(false);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Limpiar recursos si es necesario
    }
}

