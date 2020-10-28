package com.nichi.model;

public class FileToHashMap {
	private int lineNumber;
	private String fileSize="";
	private String fileName;
	private String rawDataOfLine;
	private String key;
	private int status = 0;
	private String parentPath;
	private String itemType;
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	
	public String getFileName() {
		if(fileName == null)
			return "";
		return fileName;
	}
	public void setFileName(String fileName) {
		if(fileName == null)
			return;
		this.fileName = fileName;
	}
	
	
	public String getRawDataOfLine() {
		if(rawDataOfLine == null)
			return "\r\n";
		return rawDataOfLine;
	}
	public void setRawDataOfLine(String rawDataOfLine) {
		if(rawDataOfLine == null)
			return;
		this.rawDataOfLine = rawDataOfLine;
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getStatus() {
		if(status <= 0)
			return 0;
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "FileToHashMap [lineNumber=" + lineNumber + ", fileSize=" + fileSize + ", fileName=" + fileName
				+ ", rawDataOfLine=" + rawDataOfLine + ", directoryPath=" + key
				+ ", status=" + status + "]";
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

}
