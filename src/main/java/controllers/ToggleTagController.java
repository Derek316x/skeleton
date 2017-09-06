package controllers;

import api.ReceiptResponse;
import dao.TagDao;
import dao.ReceiptDao;

import generated.tables.records.ReceiptsRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Path("/tags/{tag}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ToggleTagController {
    final TagDao tags;
    final ReceiptDao receipts;

    public ToggleTagController(TagDao tagDao, ReceiptDao receiptDao) {
        this.tags = tagDao;
        this.receipts = receiptDao;
    }

    @PUT
    public void toggleTag(@PathParam("tag") String tagName, int receiptID) {
        int tagID = tags.exists(tagName, receiptID);

        if (tagID == 0) {
            tags.insert(tagName, receiptID);
        } else {
            tags.delete(tagID);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReceiptResponse> getTag(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptRecords = receipts.getAllReceiptsByTag(tagName);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }

}