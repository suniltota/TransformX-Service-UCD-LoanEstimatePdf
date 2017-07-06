/**
 * 
 */
package com.actualize.mortgage.services;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;

import com.actualize.mortgage.domainmodels.PDFResponse;
import com.actualize.mortgage.mismodao.MISMODocument;

/**
 * @author sboragala
 *
 */
public interface LoanEstimatePDFServices {

	public PDFResponse generateLoanEstimatePDF(MISMODocument mismoDocument) throws COSVisitorException, IOException;

}
