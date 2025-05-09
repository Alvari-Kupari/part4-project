package uni.auckland.dep.db.query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class DBInitializer {
    public static EntityManagerFactory entityManagerFactory;
    public static EntityManager entityManager;
    public static EntityTransaction transaction;

    public void initializeEntityManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
        transaction = entityManager.getTransaction();
    }

    public void closeEntityManager() {
        if (transaction.isActive()) {
            transaction.rollback();
        }
        entityManager.close();
        entityManagerFactory.close();
    }
}
