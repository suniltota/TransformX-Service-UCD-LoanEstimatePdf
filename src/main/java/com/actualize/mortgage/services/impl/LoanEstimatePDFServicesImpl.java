package com.actualize.mortgage.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.springframework.stereotype.Service;

import com.actualize.mortgage.domainmodels.PDFResponse;
import com.actualize.mortgage.leform.LoanEstimate;
import com.actualize.mortgage.mismodao.MISMODocument;
import com.actualize.mortgage.services.LoanEstimatePDFServices;
@Service
public class LoanEstimatePDFServicesImpl  implements LoanEstimatePDFServices{

	private static final Logger LOG = LogManager.getLogger(LoanEstimatePDFServicesImpl.class);
	
	@Override
	public PDFResponse generateLoanEstimatePDF(MISMODocument mismoDocument) throws COSVisitorException, IOException {
		LOG.debug("generateLoanEstimatePDF Service Method Called");
		LoanEstimate leform = new LoanEstimate();
        ByteArrayOutputStream byteArrayOutputStream = leform.getPDFDocument(mismoDocument);
        PDFResponse outputResponse = new PDFResponse();
        outputResponse.setResponseData(byteArrayOutputStream.toByteArray());
        outputResponse.setFilename("LoanEstimate");
        outputResponse.setOutputType("application/pdf");
		return outputResponse;
	}

}
