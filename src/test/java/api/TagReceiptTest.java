package api;

import dao.ReceiptDao;
import dao.TagDao;
import io.dropwizard.jersey.validation.Validators;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

public class TagReceiptTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testTag() {

        final String jdbcUrl = "jdbc:h2:mem:test;MODE=MySQL;INIT=RUNSCRIPT from 'classpath:schema.sql'";
        JdbcConnectionPool cp = JdbcConnectionPool.create(jdbcUrl, "sa", "sa");

        // This sets up jooq to talk to whatever database we are using.
        org.jooq.Configuration jooqConfig = new DefaultConfiguration();
        jooqConfig.set(SQLDialect.MYSQL);   // Lets stick to using MySQL (H2 is OK with this!)
        jooqConfig.set(cp);
        TagDao tagDao = new TagDao(jooqConfig);
        ReceiptDao receiptDao = new ReceiptDao(jooqConfig);

        int receiptid = receiptDao.insert("Krusty Krab", new BigDecimal(2.99));
        tagDao.insert("burger",receiptid);

        assert(tagDao.exists("burger",receiptid) == 1);
    }

}