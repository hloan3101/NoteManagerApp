package com.example.notemanagerapp.repository;

import com.example.notemanagerapp.api.API;
import com.example.notemanagerapp.model.Account;
import com.example.notemanagerapp.model.BaseResponse;
import com.example.notemanagerapp.service.AccountService;

import retrofit2.Call;

public class AccountRepository {
    private AccountService accountService;

    public AccountRepository() {
        this.accountService = getAccountService();
    }

    public AccountService getAccountService() {
        return API.getAccount().create(AccountService.class);
    }

    public Call<BaseResponse> logIn(Account account){
        return accountService.signIn(account.getEmail(), account.getPassword());
    }

    public Call<BaseResponse> signUp(Account account){
        return accountService.signUp(account.getEmail(), account.getPassword(),
                account.getFirstName(), account.getLastName());
    }
}
