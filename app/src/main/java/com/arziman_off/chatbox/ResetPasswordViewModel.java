package com.arziman_off.chatbox;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordViewModel extends ViewModel {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private MutableLiveData<String> exceptionText = new MutableLiveData<>();
    private MutableLiveData<Boolean> linkIsSend = new MutableLiveData<>(false);
    public void resetPassword(String email){
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        linkIsSend.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        exceptionText.setValue(e.getMessage());
                    }
                });
    }

    public LiveData<String> getExceptionText() {
        return exceptionText;
    }

    public LiveData<Boolean> getLinkIsSend() {
        return linkIsSend;
    }
}
