package com.arziman_off.chatbox;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<List<User>> usersList = new MutableLiveData<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference referenceUsers;

    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUsers = firebaseDatabase.getReference("Users");
        showAllUsers();
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<List<User>> getUsersList() {
        return usersList;
    }

    public void logout() {
        setUserOnlineStatus(false);
        auth.signOut();
    }
    private void showAllUsers() {
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if (currentUser == null) {
                    return;
                }
                List<User> usersFromDBList = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    User userFromDB = ds.getValue(User.class);
                    if (userFromDB == null){
                        return;
                    }
                    if (!userFromDB.getId()
                            .equals(currentUser.getUid())) {
                        usersFromDBList.add(userFromDB);
                    }
                }

                usersList.setValue(usersFromDBList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUserOnlineStatus(boolean onlineStatus){
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null){
            return;
        }

        referenceUsers
                .child(firebaseUser.getUid())
                .child("onlineStatus")
                .setValue(onlineStatus);

    }
}
