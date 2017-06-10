package com.actualize.mortgage.domainmodels;

import java.io.Serializable;

/**
 * This class defines the response body for PDF response
 * @author sboragala
 *
 */
public class PDFResponse implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5362561241204544202L;
	
	private byte[] responseData;
    private String outputType;
    private String filename;
	/**
	 * @return the responseData
	 */
	public byte[] getResponseData() {
		return responseData;
	}
	/**
	 * @param responseData the responseData to set
	 */
	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}
	/**
	 * @return the outputType
	 */
	public String getOutputType() {
		return outputType;
	}
	/**
	 * @param outputType the outputType to set
	 */
	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

    

}
