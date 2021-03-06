package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class ReceiptDao {
    DSLContext dsl;

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptsRecord> getAllReceipts() {
        return dsl.selectFrom(RECEIPTS).fetch();
    }

    public List<ReceiptsRecord> getAllReceiptsByTag(String tagName) {
        return dsl.select()
                .from(RECEIPTS)
                .join(TAGS).on(TAGS.RECEIPT_ID.eq(RECEIPTS.ID))
                .where(TAGS.TAG_NAME.eq(tagName))
                .fetchInto(RECEIPTS);

    }

    public List<TagsRecord> getAllTagsByReceipt(int receiptID) {
        return dsl.select()
                .from(TAGS)
                .join(RECEIPTS).on(TAGS.RECEIPT_ID.eq(RECEIPTS.ID))
                .where(TAGS.RECEIPT_ID.eq(receiptID))
                .fetchInto(TAGS);
    }
}
