package uni.auckland.dep.db.query;

import uni.auckland.dep.db.entity.CurrentDepNotransitive;

import javax.persistence.Query;
import java.util.ArrayList;

public class CurrentDepQuery extends DBInitializer {

    /**
     * Select query to retrieve the current dependency records for a given project ID
     * @param mvnProjectID
     * @return
     */
    public ArrayList<CurrentDepNotransitive> getCurrentDependencies(int mvnProjectID) {
        ArrayList<CurrentDepNotransitive> currentDepNotransitives = new ArrayList<>();
        initializeEntityManager();
        try {
            transaction.begin();
            Query getCurrentDep = entityManager.createNativeQuery(String.format("SELECT *  FROM current_dep_notransitive " +
                    "where mvn_project_detail_notransitive_id = %d", mvnProjectID), CurrentDepNotransitive.class);

            getCurrentDep.getResultList().forEach(result ->{
                currentDepNotransitives.add((CurrentDepNotransitive)result);
            });
            transaction.commit();
        } finally {
            closeEntityManager();
        }
        return currentDepNotransitives;
    }
}
