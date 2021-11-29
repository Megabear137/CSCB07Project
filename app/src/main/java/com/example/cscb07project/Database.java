package com.example.cscb07project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Database implements Contract.Model{

    private static Database database = null;
    private ArrayList<User> users;
    private ArrayList<Store> stores;
    private static int userCount;
    private static int storeCount;

    private Database() {

        users = new ArrayList<>();
        stores = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        //Initializes userCount variable by getting currently stored value in Firebase Database
        ref.child("UserCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("demo", "Error getting data");
                } else {
                    userCount = Integer.parseInt(task.getResult().getValue().toString());
                    //Initializes stores array list by getting currently stored stores in Firebase Database
                    for (int i = 0; i < userCount; i++) {
                        int finalI = i;
                        ref.child("Users").child(i + "").child("isStoreOwner").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(!task.getResult().getValue(Boolean.class)){
                                    ref.child("Users").child(finalI + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            users.add(task.getResult().getValue(Customer.class));
                                        }
                                    });
                                }
                                else{
                                    ref.child("Users").child(finalI + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            users.add(task.getResult().getValue(StoreOwner.class));
                                        }
                                    });
                                }
                            }
                        });

                    }

                }
            }
        });

        ref.child("StoreCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("demo", "Error getting data");
                } else {
                    storeCount = Integer.parseInt(task.getResult().getValue().toString());
                    //Initializes stores array list by getting currently stored stores in Firebase Database
                    for (int i = 0; i < storeCount; i++) {
                        ref.child("Stores").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                stores.add(task.getResult().getValue(Store.class));
                            }
                        });
                    }

                }
            }
        });

    }

    //Function that is used to get an instance of the database. Getting an instance allows usage of functions in this class
    public static Database getInstance(){
        if(database == null)
            database = new Database();
        return database;
    }

    void updateDatabase(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").setValue(users);
        ref.child("Stores").setValue(stores);
        ref.child("UserCount").setValue(users.size());
        ref.child("StoreCount").setValue(stores.size());
    }

    public boolean userExists(String username){
        for(User user: users){
            if(user.getUsername().equals(username))
                return true;
        }
        return false;
    }

    boolean storeExists(String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName))
                return true;
        }
        return false;
    }

    boolean isCustomer(String username){
        for(User user: users){
            if(user.getUsername().equals(username) && !user.isStoreOwner)
                return true;
        }

        return false;
    }

    boolean isStoreOwner(String username){
        for(User user: users){
            if(user.getUsername().equals(username) && user.isStoreOwner)
                return true;
        }

        return false;
    }

    //Returns Customer/StoreOwner object with matching username stored in Firebase Database. Returns null if no user has
    //matching username.
    Customer findCustomer(String username){

        for (User user: users){
            if(user.getUsername().equals(username) && isCustomer(username))
                return (Customer)user;
        }

        return null;

    }

    StoreOwner findStoreOwner(String username){
        for (User user: users){
            if(user.getUsername().equals(username) && !isCustomer(username))
                return (StoreOwner)user;
        }

        return null;
    }

    //Returns Store object with matching storeName stored in Firebase Database. Returns null if no store has
    //matching storeName.
    Store findStore(String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName))
                return store;
        }
        return null;
    }

    //Adds a new customer to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    boolean addCustomer(String username, String password){

        if(userExists(username))
            return false;

        userCount++;
        FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

        Customer customer = new Customer(username, password);
        users.add(customer);
        updateDatabase();
        return true;
    }

    //Adds a new Store Owner to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    boolean addStoreOwner(String username, String password){
        if(userExists(username))
            return false;

        userCount++;
        FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

        StoreOwner storeOwner = new StoreOwner(username, password);
        users.add(storeOwner);
        updateDatabase();
        return true;
    }


    /*
    Adds a new store to the database and assigns it to the storeOwner whose username matches the passed in username.
    Returns 1 if successful
    four possible failures:
    returns -1 if a store in the database already has the passed in storeName
    returns -2 if StoreOwner already has a store
    returns -3 if passed in username belongs to a customer, not a StoreOwner.
    returns -4 if passed in username belongs to no one
    */
    int addStore(String storeName, String ownerName){

        if(storeExists(storeName)) return -1;
        if(!findStoreOwner(ownerName).getStoreName().equals("")) return -2;
        if(!userExists(ownerName)) return -3;
        if(isCustomer(ownerName)) return -4;

        storeCount++;
        FirebaseDatabase.getInstance().getReference().child("StoreCount").setValue(storeCount);

        Store store = new Store(storeName, ownerName);
        findStoreOwner(ownerName).setStoreName(storeName);
        stores.add(store);
        updateDatabase();
        return 1;
    }

    //Adds product to store with storeName.
    //returns 1 if successful
    //return 0 if no store has storeName as its name
    int addProductToStore(String storeName, Product product){
        if(!storeExists(storeName)) return 0;

        findStore(storeName).products.add(product);
        updateDatabase();
        return 1;
    }

    //Adds product to cart belonging to customer with matching username.
    //returns 1 if successful
    //return 0 if no customer has a matching username
    int addProductToCart(String customerName, Product product){
        if(isCustomer(customerName)){
            findCustomer(customerName).cart.add(product);
            updateDatabase();
            return 1;
        }
        return 0;
    }

    void makeOrder(Order order){

    }



    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public static int getStoreCount() {
        return storeCount;
    }

    int getUserCount(){
        return userCount;
    }


}
