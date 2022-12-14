package com.example.notemanagerapp.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notemanagerapp.constants.Constants;
import com.example.notemanagerapp.livedata.RefreshLiveData;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.model.DetailItemNote;
import com.example.notemanagerapp.repository.DetailItemNoteRepository;

import java.util.List;

import retrofit2.Call;

public class PriorityViewModel extends AndroidViewModel {
    private DetailItemNoteRepository repository;
    private RefreshLiveData<List<DetailItemNote>> liveData;

    public PriorityViewModel(@NonNull Application application) {
        super(application);
        repository = new DetailItemNoteRepository();
        liveData = repository.getListDetailItemNote(Constants.TAB_PRIORITY);
    }

    public LiveData<List<DetailItemNote>> getListPriorityItem (){
        return liveData;
    }

    public Call<BaseResponse> addPriorityItem(String name){
        return repository.addDetailItemNote(Constants.TAB_PRIORITY, name);
    }

    public Call<BaseResponse> updatePriorityItem (String name, String newName){
        return repository.updateDetailItemNote(Constants.TAB_PRIORITY, name, newName);
    }

    public Call<BaseResponse> deletePriorityItem (DetailItemNote detailItemNote){
        return repository.deleteDetailItemNote(Constants.TAB_PRIORITY, detailItemNote);
    }

    public void refreshLiveData (){
        liveData.refresh();
    }
}