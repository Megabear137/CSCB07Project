package com.example.cscb07project;

import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;

public class Database implements Contract.Model{

    public static Database database;

    public static User user;
    private static int userIndex;

    public static Store store;
    private static int storeIndex;

    public static ArrayList<Store> stores;
    public static int userCount;
    public static int storeCount;

    public Database() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserCount");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userCount = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref.addValueEventListener(listener);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("StoreCount");
        ValueEventListener listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeCount = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        ref1.addValueEventListener(listener1);

    }

    public static Database getInstance() {
        return database;
    }

    public void updateDatabase(){
        if(user.isStoreOwner && store != null){
            FirebaseDatabase.getInstance().getReference("Stores").child(storeIndex + "").setValue(store);
        }
        else if(stores != null && !stores.isEmpty()){
            FirebaseDatabase.getInstance().getReference("Stores").setValue(stores);
        }
        FirebaseDatabase.getInstance().getReference().child("Users").child(userIndex + "").setValue(user);
    }

    public void initializeUser(String username, Contract.Presenter presenter) {

        FirebaseDatabase.getInstance().getReference("UserCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (int i = 0; i < userCount; i++) {
                    FirebaseDatabase.getInstance().getReference("Users").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.getResult().child("username").getValue(String.class).equals(username)) {

                                task.getResult().getRef().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (user == null && task.getResult().child("isStoreOwner").getValue(Boolean.class)) {
                                            user = task.getResult().getValue(StoreOwner.class);
                                            userIndex = Integer.parseInt(task.getResult().getKey());
                                            presenter.validateLogin(user);
                                        } else if(user == null && !task.getResult().child("isStoreOwner").getValue(Boolean.class)) {
                                            user = task.getResult().getValue(Customer.class);
                                            userIndex = Integer.parseInt(task.getResult().getKey());
                                            presenter.validateLogin(user);
                                        }
                                    }
                                });

                                DatabaseReference ref = task.getResult().getRef();

                                ValueEventListener listener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(user != null && user.getUsername().equals(snapshot.child("username").getValue(String.class))){
                                            if (snapshot.child("isStoreOwner").getValue(Boolean.class)) {
                                                user = snapshot.getValue(StoreOwner.class);
                                                userIndex = Integer.parseInt(snapshot.getKey());
                                            } else {
                                                user = snapshot.getValue(Customer.class);
                                                userIndex = Integer.parseInt(snapshot.getKey());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                };
                                ref.addValueEventListener(listener);
                            }
                        }
                    });
                }
            }
        });
    }

    public void initializeStores(){

        stores = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Stores");

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(stores != null) stores.clear();
                for(int i = 0; i < storeCount; i++){
                    FirebaseDatabase.getInstance().getReference("Stores").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            stores.add(task.getResult().getValue(Store.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addValueEventListener(listener);
    }

    public void initializeStore(String storeName){

        FirebaseDatabase.getInstance().getReference("StoreCount").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int storeCount = task.getResult().getValue(Integer.class);
                FirebaseDatabase.getInstance().getReference().child("Stores").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult().exists()){
                            for (int i = 0; i < storeCount; i++) {

                                FirebaseDatabase.getInstance().getReference("Stores").child(i + "").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.getResult().child("name").getValue(String.class).equals(storeName)) {
                                            DatabaseReference ref = task.getResult().getRef();
                                            ValueEventListener listener = new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    store = snapshot.getValue(Store.class);
                                                    storeIndex = Integer.parseInt(snapshot.getKey());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            };
                                            ref.addValueEventListener(listener);
                                        }
                                    }
                                });
                            }
                        }
                        else{
                            store = null;
                        }
                    }
                });

            }
        });
    }

    public void matchPass(String username, String password,  Contract.Presenter presenter) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Passwords").child(username);
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists() && task.getResult().getValue(String.class).equals(password)) initializeUser(username, presenter);
                else if(task.getResult().getValue() != null) presenter.invalidateLogin(0);
                else presenter.invalidateLogin(1);
            }
        });
    }

    public boolean storeExists(String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName))
                return true;
        }
        return false;
    }

    public boolean productExists(String productName){

        for(Product product: store.products) {
            if (product.getName().equals(productName)) return true;
        }

        return false;
    }

    public boolean isCustomer(){
        return !user.isStoreOwner;
    }

    public boolean isStoreOwner(){
        return user.isStoreOwner;
    }

    public Product findProduct(String productName){

        for(Product product: store.products){
            if(product.getName().equals(productName)) return product;
        }

        return null;

    }

    //Returns Store object with matching storeName stored in Firebase Database. Returns null if no store has
    //matching storeName.
    public Store findStore(String storeName){
        for (Store store: stores){
            if(store.getName().equals(storeName))
                return store;
        }
        return null;
    }

    //Adds a new customer to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    public void addCustomer(String username, String password, Contract.Presenter presenter){

        FirebaseDatabase.getInstance().getReference("Passwords").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    userCount++;
                    FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

                    Customer customer = new Customer(username);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userCount - 1 + "").setValue(customer);
                    FirebaseDatabase.getInstance().getReference().child("Passwords").child(username).setValue(password);

                    user = customer;

                    presenter.validateSignup(customer);
                }
                else{
                    presenter.invalidateSignup();
                }
            }
        });
    }

    //Adds a new Store Owner to the Database. Returns true if successful, returns false if a user in the database already has the passed in username.
    public void addStoreOwner(String username, String password, Contract.Presenter presenter){

        FirebaseDatabase.getInstance().getReference("Passwords").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    userCount++;
                    FirebaseDatabase.getInstance().getReference().child("UserCount").setValue(userCount);

                    StoreOwner storeOwner = new StoreOwner(username);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userCount - 1 + "").setValue(storeOwner);
                    FirebaseDatabase.getInstance().getReference().child("Passwords").child(username).setValue(password);
                    FirebaseDatabase.getInstance().getReference().child("StoreOwners").child(username).setValue("");

                    user = storeOwner;

                    presenter.validateSignup(storeOwner);
                }
                else{
                    presenter.invalidateSignup();
                }
            }
        });

    }


    /*
    Adds a new store to the database and assigns it to the storeOwner whose username matches the passed in username.
    Returns 1 if successful
    four possible failures:
    */
    public void addStore(String storeName, String ownerName, RegisterStoreActivity rsa){

        FirebaseDatabase.getInstance().getReference("StoreOwners").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                boolean storeExists = false;
                for(DataSnapshot snapshot: task.getResult().getChildren()){
                    if(snapshot.getValue(String.class).equals(storeName)){
                        rsa.verifyRegisterStore(0);
                        storeExists = true;
                    }
                }

                if(!storeExists){
                    Store store = new Store(storeName, ownerName);
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userIndex + "").child("storeName").setValue(storeName);
                    FirebaseDatabase.getInstance().getReference().child("StoreOwners").child(ownerName).setValue(storeName);
                    FirebaseDatabase.getInstance().getReference().child("Stores").child(storeCount + "").setValue(store);
                    storeCount++;
                    FirebaseDatabase.getInstance().getReference().child("StoreCount").setValue(storeCount);
                    rsa.verifyRegisterStore(1);
                }

            }
        });
    }

    //Adds product to store with storeName.
    //returns 1 if successful
    //return 0 if no store has storeName as its name
    public int addProductToStore(String storeName, Product product){

        store.products.add(product);
        updateDatabase();
        return 1;
    }

    /* Adds a product from the store of specified quantity to cart belonging to specified customer.
        return 1 if successful.
        return 0 if no customer has a matching username
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addProductToCart(String storeName, String productName, int quantity, ViewStoreActivity vsa) {
        if(!isCustomer()) {
            return;
        }

        Customer customer = (Customer)user;

        for(Order order: customer.getCart()){
            if(order.getStoreName().equals(storeName)) {
                if(order.products.containsKey(productName)){
                    int productNum = order.products.get(productName);
                    order.products.replace(productName, productNum + quantity);
                }
                else{
                    order.products.put(productName, quantity);
                }
                updateDatabase();
                vsa.updateCartSpinner();
                vsa.setCanOrder();
                vsa.updateTotal();
                return;
            }
        }

        FirebaseDatabase.getInstance().getReference("OrderID").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Order order = new Order(customer.getUsername(), storeName, task.getResult().getValue(Integer.class));
                FirebaseDatabase.getInstance().getReference("OrderID").setValue(task.getResult().getValue(Integer.class) + 1);
                order.products.put(productName, quantity);
                customer.cart.add(order);


                updateDatabase();
                vsa.updateCartSpinner();
                vsa.setCanOrder();
                vsa.updateTotal();
            }
        });
    }

    /* Deletes entire product from cart belonging to specified customer.
    return 1 if successful.
    return 0 if no customer has a matching username.
    Assumption: all products in cart will have count at least 1.
    Assumption: The function is only called when there is at least one such product in cart. */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public int deleteProductFromCart(String productName, String storeName) {

        Customer customer = (Customer)user;
        if(!isCustomer()) {
            return 0;
        }

        Order orderToRemove = null;
        String productToRemove = "";

        for (Order order: customer.cart) {
            if(order.getStoreName().equals(storeName)) {
                for(String name: order.getProducts().keySet()) {
                    if(name.equals(productName)) {
                        productToRemove = name;
                        orderToRemove = order;
                    }
                }
            }
        }


        if(orderToRemove != null){
            if(!productToRemove.equals("")){ orderToRemove.products.remove(productToRemove);}
            if(orderToRemove.products.isEmpty()) customer.cart.remove(orderToRemove);
        }


        updateDatabase();
        return 1;
    }


    /* Make all products from the store in customer's cart into an order. Then add it
        to customer.pendingOrder and store.incomingOrder.
        return 1 if successful.
          return -1 if customer not found.
          return -2 if store not found.
          return -3 if cart has no order to be realized from the store.*/
    public int makeOrder(String storeName) {
        if (!isCustomer())
            return -1;

        if (findStore(storeName) == null)
            return -2;

        Customer customer = (Customer)user;
        Order transferedOrder = null;
        for(Order order: customer.getCart()) {
            if(order.getStoreName().equals(storeName)) {
                transferedOrder = order;
            }
        }
        if (transferedOrder == null) {
            return -3;
        }

        customer.cart.remove(transferedOrder);
        customer.pendingOrders.add(transferedOrder);
        Store store = findStore(storeName);
        store.incomingOrders.add(transferedOrder);
        updateDatabase();
        return 1;
    }

    /*Remove the order from the customer's completedOrder.
    Return 1 if successful.
    Return -1 if no such order found.
     */
    public int customerDeleteHistory(Order order) {
        Customer customer = (Customer)user;
        if (customer.completedOrders.contains(order)) {
            customer.completedOrders.remove(order);
            updateDatabase();
            return 1;
        }
        else
            return -1;
    }

    /*Remove the order from the store's outgoingOrder.
    Return 1 if successful.
    Return -1 if no such order found.
     */
    public int storeDeleteHistory(Order order) {
        if (store.outgoingOrders.contains(order)) {
            store.outgoingOrders.remove(order);
            updateDatabase();
            return 1;
        }
        else
            return -1;
    }

    public int editProduct(Product product, String storeName, String newName, String newBrand,
                           double newPrice) {
        if (product == null)
            return -1;
        Store store = this.store;
        if (store == null || storeName == null || storeName.isEmpty())
            return -2;
        if (newName.isEmpty())
            return -3;
        if(newBrand.isEmpty()){
            return -4;
        }
        if(newPrice < 0) {
            return -5;
        }

        store.editProductInfo(product,newName, newBrand,newPrice);
        updateDatabase();
        return 1;
    }


    //Returns Product object in database given a store name and product name. Returns null if
    // store or product does not exist
    public Product findProductInStore(String productName, String storeName){

        for(Store store: stores){
            if(store.getName().equals(storeName)){
                ArrayList<Product> products = store.getProducts();
                for (Product product: products){
                    if (product.getName().equals(productName)) {
                        return product;
                    }
                }
            }
        }

        return null;

    }

    //Returns true iff product is found in a store, given the store name and product name
    public boolean productExists(String storeName, String productName){
        if (findProductInStore(productName, storeName) == null) {
            return false;
        }
        return true;
    }

    // Returns incoming Order if given orderID corresponding to order is found, null otherwise
    public Order findIncomingOrder(int orderID) {
        for (Order order: store.incomingOrders) {
            if (orderID == order.getId()){
                return order;
            }
        }
        return null;

    }


    //Returns 1 iff order has been successfully removed from incoming orders,
    // and added to outgoing orders, and returns -1 otherwise
    public void storeCompleteOrder(int orderID, StoreOwnerViewIncomingActivity s) {
        Order order = findIncomingOrder(orderID);

        if (store.incomingOrders.contains(order)) {
            order.setFulfilled(true);
            store.incomingOrders.remove(order);
            store.outgoingOrders.add(order);
            FirebaseDatabase.getInstance().getReference("Users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    for (int i = 0; i < userCount; i++) {
                        if (task.getResult().child(i + "").child("pendingOrders").exists()) {
                            for (DataSnapshot snapshot : task.getResult().child(i + "").child("pendingOrders").getChildren()) {
                                if (snapshot.child("id").getValue(Integer.class) == orderID) {
                                    Customer customer = task.getResult().child(i + "").getValue(Customer.class);
                                    Order orderToRemove = null;
                                    for (Order order : customer.pendingOrders) {
                                        if (order.id == orderID) {
                                            orderToRemove = order;
                                        }
                                    }
                                    orderToRemove.isFulfilled = true;
                                    customer.completedOrders.add(orderToRemove);
                                    customer.pendingOrders.remove(orderToRemove);
                                    FirebaseDatabase.getInstance().getReference("Users").child(i + "").setValue(customer);
                                    s.validateComplete(1);
                                    updateDatabase();
                                }
                            }
                        }
                    }
                }
            });
        }
        else s.validateComplete(-1);
    }

    // Returns outgoing Order if given orderID corresponding to order is found, null otherwise
    public Order findOutgoingOrder(int orderID) {
        for (Order order: store.outgoingOrders) {
            if (orderID == order.getId()){
                return order;
            }
        }
        return null;

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
