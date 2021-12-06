package com.example.cscb07project;

public abstract class User {
    String username;
    boolean isStoreOwner;

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (!username.equals(other.username))
            return false;
        return true;
    }

    public String getUsername() {
        return username;
    }

    public boolean getIsStoreOwner() {
        return isStoreOwner;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStoreOwner() {
        return isStoreOwner;
    }

    public void setStoreOwner(boolean storeOwner) {
        isStoreOwner = storeOwner;
    }

    public void setIsStoreOwner(boolean isStoreOwner) {
        this.isStoreOwner = isStoreOwner;
    }
}
