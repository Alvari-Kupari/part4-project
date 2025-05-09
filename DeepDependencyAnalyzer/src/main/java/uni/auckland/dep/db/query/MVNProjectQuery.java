package uni.auckland.dep.db.query;

import uni.auckland.dep.db.entity.MvnProjectDetailsNotransitive;

import javax.persistence.*;
import java.util.ArrayList;

public class MVNProjectQuery extends DBInitializer {

    /**
     * Select Query to get build successful projects
     * @return A list of {@link MvnProjectDetailsNotransitive}
     */
    public ArrayList<MvnProjectDetailsNotransitive> getMVNProjects() {
        ArrayList<MvnProjectDetailsNotransitive> mvnProjectDetailsNotransitives = new ArrayList<>();
        initializeEntityManager();
        try {
            transaction.begin();
            // stuck on 2569, 2794, 3026, 3292
            Query getMvnProjects = entityManager.createNativeQuery("SELECT * FROM " +
                    "dependency_analysis.mvn_project_details_notransitive where mvn_project_detail_notransitive_id > 3292 and build_status = 'Success' and " +
                    "java_version = '1.8' and has_src_dir = 'True' limit 100000", MvnProjectDetailsNotransitive.class);

            getMvnProjects.getResultList().forEach(result -> mvnProjectDetailsNotransitives.add((MvnProjectDetailsNotransitive)result));
            transaction.commit();
        } finally {
            closeEntityManager();
        }
        return mvnProjectDetailsNotransitives;
    }


}
