package com.kanefron5.lab4.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Lab4UsersEntity, Integer> {

    List<Lab4UsersEntity> findFirstByEmailuser(String emailuser);
}