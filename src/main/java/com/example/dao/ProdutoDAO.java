package com.example.dao;

import com.example.model.Produto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class ProdutoDAO {

    public Produto salvar(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (produto.getId() == null) {
                em.persist(produto);
            } else {
                produto = em.merge(produto);
            }
            tx.commit();
            return produto;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Produto> listar() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("from Produto", Produto.class).getResultList();
        } finally {
            em.close();
        }
    }
}
