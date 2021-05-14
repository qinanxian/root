package com.vekai.auth.holder;


import com.vekai.auth.entity.User;

public abstract class AuthHolder {
    private static final ThreadLocal<User> userHolder = new ThreadLocal<User>();

    public static User getUser(){
        return userHolder.get();
    }
    public static void setUser(User user){
        userHolder.set(user);
    }
    public static void clear(){
        userHolder.remove();
    }
}
