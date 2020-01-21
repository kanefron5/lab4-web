package com.kanefron5.lab4.database;

import java.util.List;

public interface DotService {
    List<Lab4DotsEntity> findAll(String owner);
    Lab4DotsEntity addDot(Lab4DotsEntity dot);
}
