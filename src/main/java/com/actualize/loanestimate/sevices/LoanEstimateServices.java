package com.actualize.loanestimate.sevices;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;

import com.actualize.loanestimate.domainmodels.PDFResponse;
import com.actualize.loanestimate.mismodao.MISMODocument;

public interface LoanEstimateServices {
	
	public PDFResponse generateLoanEstimatePDF(MISMODocument mismoDocument) throws COSVisitorException, IOException;

}
