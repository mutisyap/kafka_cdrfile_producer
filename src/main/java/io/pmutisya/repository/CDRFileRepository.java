package io.pmutisya.repository;

import io.pmutisya.config.HibernateConfiguration;
import io.pmutisya.domain.CDRFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class CDRFileRepository {
    private static final Logger logger = LogManager.getLogger(CDRFileRepository.class);

    public void save(CDRFile cdrFile) {
        logger.info("Saving cdrFile : {}", cdrFile);

        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(cdrFile);
        session.getTransaction().commit();
        session.close();
        logger.info("Successfully saved cdr file");
    }

    public boolean findDuplicateByDataFeedAndFilename(String dataFeed, String filename) {
        logger.info("Finding duplicate file given dataFeed : {} : and filename: {}", dataFeed, filename);

        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();

        Query duplicateQuery = session.createQuery("select id from CDRFile c where c.filename = :filename AND dataFeed = :dataFeed AND totalRecords > 0");
        duplicateQuery.setParameter("filename", filename);
        duplicateQuery.setParameter("dataFeed", dataFeed);

        int duplicateRecords = duplicateQuery.getResultList().size();
        session.close();
        
        return duplicateRecords > 0;
    }
}
