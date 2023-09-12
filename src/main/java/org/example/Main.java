package org.example;

import org.example.logic.CellEvaluator;
import org.example.model.EvaluatedSheets;
import org.example.model.SheetsResponse;

import static org.example.webCommunication.WixCommunicationClient.getSheets;
import static org.example.webCommunication.WixCommunicationClient.postSheets;

public class Main {

    public static void main(String[] args) {
        SheetsResponse sheets = getSheets();
        System.out.println(sheets);
        EvaluatedSheets evaluatedSheets = new EvaluatedSheets(
                "julius.ivoska@gmail.com",
                new CellEvaluator().evaluateSheets(sheets.getSheets())
        );
        System.out.println(evaluatedSheets);
        System.out.println(postSheets(sheets.getSubmissionUrl(), evaluatedSheets));
    }
}