package com.actualize.loanestimate.sevices.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;

import com.actualize.loanestimate.domainmodels.PDFResponse;
import com.actualize.loanestimate.leform.LoanEstimate;
import com.actualize.loanestimate.mismodao.MISMODocument;
import com.actualize.loanestimate.sevices.LoanEstimateServices;

public class LoanEstimateServicesImpl implements LoanEstimateServices{

	@Override
	public PDFResponse generateLoanEstimatePDF(MISMODocument mismoDocument) throws COSVisitorException, IOException {
		LoanEstimate leform = new LoanEstimate();
        ByteArrayOutputStream byteArrayOutputStream = leform.getPDFDocument(mismoDocument);
        PDFResponse outputResponse = new PDFResponse();
        outputResponse.setResponseData(byteArrayOutputStream.toByteArray());
        outputResponse.setFilename("LoanEstimate");
        outputResponse.setOutputType("application/pdf");
		return outputResponse;
	}

}
