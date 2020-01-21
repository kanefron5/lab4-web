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
public class UserServiceImpl implements UserService {

    @Autowired
    public UserServiceImpl() {
    }

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Lab4UsersEntity> findAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    public Lab4UsersEntity putUser(Lab4UsersEntity user) {
        if (user.getId() == null) em.persist(user);
        else em.merge(user);
        return user;
    }



    @Override
    public Lab4UsersEntity findByEmailuser(String emailuser) throws Exception {
        List<Lab4UsersEntity> firstByEmailuser = userRepository.findFirstByEmailuser(emailuser);
        if (firstByEmailuser.size()==0) {
            throw new Exception("Пользователь не зарегистрирован!");
        }
        return firstByEmailuser.get(0);
    }
}
