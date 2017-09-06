package dao;


import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;



import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {

        this.dsl = DSL.using(jooqConfig);

    }

    public int insert(String tagName, int receiptID) {

        boolean doesReceiptExist = dsl.fetchExists(
                dsl.selectFrom(RECEIPTS)
                .where(RECEIPTS.ID.eq(receiptID))
        );

        checkState(doesReceiptExist, "Receipt does not exist");

        TagsRecord tagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG_NAME, TAGS.RECEIPT_ID)
                .values(tagName, receiptID)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(tagsRecord != null && tagsRecord.getId() != null, "Insert failed");

        return tagsRecord.getId();
    }

    public int exists(String tagName, int receiptID) {
        TagsRecord tagsRecord = dsl.selectFrom(TAGS)
                .where(TAGS.TAG_NAME.eq(tagName))
                .and(TAGS.RECEIPT_ID.eq(receiptID))
                .fetchOne();

        if (tagsRecord != null) {
            return  tagsRecord.getId();
        }
        return 0;
    }

    public void delete(int tagID) {
        dsl.deleteFrom(TAGS)
                .where(TAGS.ID.eq(tagID))
                .execute();
    }
}
