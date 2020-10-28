package com.nichi.model;

public class ComparedResult {

	private int lineNumOfFile;
	private String sizeOfItemInFile1;
	private String ItemNameFromFile1;
	private String rawDataOfItemInFile1;
	private int diffStatus;

	private String sizeOfItemInFile2;
	private String ItemNameFromFile2;
	private String rawDataOfItemInFile2;
	
	public int getLineNumOfFile() {
		return lineNumOfFile;
	}
	public void setLineNumOfFile(int lineNumOfFile) {
		this.lineNumOfFile = lineNumOfFile;
	}
	public String getSizeOfItemInFile1() {
		return sizeOfItemInFile1;
	}
	public void setSizeOfItemInFile1(String sizeOfItemInFile1) {
		this.sizeOfItemInFile1 = sizeOfItemInFile1;
	}
	public String getItemNameFromFile1() {
		return ItemNameFromFile1;
	}
	public void setItemNameFromFile1(String itemNameFromFile1) {
		ItemNameFromFile1 = itemNameFromFile1;
	}
	public String getRawDataOfItemInFile1() {
		return rawDataOfItemInFile1;
	}
	public void setRawDataOfItemInFile1(String rawDataOfItemInFile1) {
		this.rawDataOfItemInFile1 = rawDataOfItemInFile1;
	}
	
	public String getSizeOfItemInFile2() {
		return sizeOfItemInFile2;
	}
	public void setSizeOfItemInFile2(String sizeOfItemInFile2) {
		this.sizeOfItemInFile2 = sizeOfItemInFile2;
	}
	public String getItemNameFromFile2() {
		return ItemNameFromFile2;
	}
	public void setItemNameFromFile2(String itemNameFromFile2) {
		ItemNameFromFile2 = itemNameFromFile2;
	}
	public String getRawDataOfItemInFile2() {
		return rawDataOfItemInFile2;
	}
	public void setRawDataOfItemInFile2(String rawDataOfItemInFile2) {
		this.rawDataOfItemInFile2 = rawDataOfItemInFile2;
	}
	
	public int getDiffStatus() {
		return diffStatus;
	}
	public void setDiffStatus(int diffStatus) {
		this.diffStatus = diffStatus;
	}
	@Override
	public String toString() {
		return "CompareResult [lineNumOfFile1=" + lineNumOfFile + ", sizeOfItemInFile1=" + sizeOfItemInFile1
				+ ", ItemNameFromFile1=" + ItemNameFromFile1 + ", rawDataOfItemInFile1=" + rawDataOfItemInFile1
				+ ", diffStatus=" + diffStatus + ",  sizeOfItemInFile2="
				+ sizeOfItemInFile2 + ", ItemNameFromFile2=" + ItemNameFromFile2 + ", rawDataOfItemInFile2="
				+ rawDataOfItemInFile2 + "]";
	}
}
