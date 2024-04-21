package calvin.calvin.repsitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CalvinMemberRepoistory {
    @Autowired
    private JdbcTemplate jdbcTemplate;

}
