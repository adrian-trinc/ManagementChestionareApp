package com.management.chestionare.service;

import com.management.chestionare.Repository.RepoUtilizator;
import com.management.chestionare.domain.Utilizator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceUtilizator {
    private final RepoUtilizator repoUtilizator;

    @Autowired
    public ServiceUtilizator(RepoUtilizator repoUtilizator) {
        this.repoUtilizator = repoUtilizator;
    }

    @Transactional(readOnly = true)
    public List<Utilizator> findAll() {
        return repoUtilizator.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Utilizator> findUtilizatorByNumeDeUtilizator(String numeDeUtilizator) {
        return repoUtilizator.findUtilizatorByNumeDeUtilizator(numeDeUtilizator);
    }
}
