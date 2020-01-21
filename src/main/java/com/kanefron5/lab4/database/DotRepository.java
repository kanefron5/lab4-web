package com.kanefron5.lab4.database;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DotRepository extends CrudRepository<Lab4DotsEntity, Integer> {
    List<Lab4DotsEntity> findByOwner(String owner);

}