package com.kanefron5.lab4.database;

import java.util.List;

public interface UserService  {

    List<Lab4UsersEntity> findAll();
    Lab4UsersEntity findByEmailuser(String emailuser) throws Exception;
    Lab4UsersEntity putUser(Lab4UsersEntity user) throws Exception;
}