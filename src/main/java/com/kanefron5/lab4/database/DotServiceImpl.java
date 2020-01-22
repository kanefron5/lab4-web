package com.kanefron5.lab4.database;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service()
@Repository
@Transactional
public class DotServiceImpl implements DotService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DotRepository dotRepository;

    @Override
    public List<Lab4DotsEntity> findAll(String owner) {
        return Lists.newArrayList(dotRepository.findByOwner(owner));
    }

    @Override
    public Lab4DotsEntity addDot(Lab4DotsEntity dot) {
        if (dot.getId() == null) em.persist(dot);
        else em.merge(dot);
        return dot;
    }
}
