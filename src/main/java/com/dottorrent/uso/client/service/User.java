package com.dottorrent.uso.client.service;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/12/13
 */
public class User {
    private long userID;
    private String passwordHash;
    private String userName;

    /**
     * @param userID 账户ID，具有唯一性，应当由服务端设定并传回，然后再将服务端传回的ID作为参数传入此构造器。如果设定为0，则为本地账户
     * @param password 如果userID为0，则密码应设为null
     * @param userName 用户名，可以重名，如果userID为0,则用户名应设为null
     */
    public User(long userID, String password, String userName) {
        this.userID = userID;
        if(userID!=0) {
            this.passwordHash = DigestUtils.sha1Hex(password);
            this.userName = userName;
        }else {
            this.passwordHash = null;
            this.userName = "LocalDefault";
        }
    }

    public long getUserID() {
        return userID;
    }

    public boolean checkPassword(String password) {
        return passwordHash.equals(DigestUtils.sha1Hex(password));
    }

    public boolean setPassword(String oldPassword,String newPassword) {
        if(this.passwordHash.equals(DigestUtils.sha1Hex(oldPassword))){
            this.passwordHash= DigestUtils.sha1Hex(newPassword);
            return true;
        }else {
            return false;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
