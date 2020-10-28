package com.nichi.util;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.nichi.model.ComparedResult;
import com.nichi.model.Constants;
import com.nichi.model.FileToHashMap;
import com.nichi.model.LoggerClass;

public class FileOperations {

	static public String encodingType;
	// ===========================File reading starts here========================
	public HashMap<String, FileToHashMap> readFile(String file) throws Exception {
		HashMap<String, FileToHashMap> mapValue = new HashMap<String, FileToHashMap>();
		
		FileInputStream inputStream = null;
		InputStreamReader isr = null;
		Scanner reader = null;
		int lineNum = 1;
		String directoryName = "";

		inputStream = new FileInputStream(file);
		isr = new InputStreamReader(inputStream);
		
		encodingType = isr.getEncoding();
		LoggerClass.logInfo("Reading file : "+file+";with encoding type :"+encodingType);
		if (encodingType.equals("MS932") || encodingType.contains("JIS") || encodingType.contains("Cp125")) {
			reader = new Scanner(inputStream, "Shift_JIS");
		} else if (encodingType.contains("EUC")) {
			reader = new Scanner(inputStream, "EUC-JP");
		} else {
			reader = new Scanner(inputStream, "UTF-8");
		}
		
		while (reader.hasNext()) {
			
			FileToHashMap lineInfo = new FileToHashMap();
			String currentLine = reader.nextLine();
			if (currentLine.startsWith(" Directory of")) {
				directoryName = currentLine.substring(currentLine.indexOf(" Directory of") + " Directory of".length());

				lineInfo.setLineNumber(lineNum);
				lineInfo.setFileName("");
				lineInfo.setKey(currentLine);
				lineInfo.setFileSize("");
				lineInfo.setRawDataOfLine(currentLine);

			} else if (currentLine.length() > 9
					&& Pattern.matches("^[0-9]{4}/[0-9]{2}/[0-9]{2}", currentLine.substring(0, 10))) {

				int indexOfDir = 17;

				String tempLine = currentLine.substring(indexOfDir);
				tempLine = tempLine.trim();
				int firstOccSpace = tempLine.indexOf(" ");
				String tempLine2 = tempLine.substring(firstOccSpace + 1).trim();

				// Read directory info
				if (currentLine.contains("<DIR>")) {

					lineInfo.setLineNumber(lineNum);
					lineInfo.setFileName(tempLine2);
					lineInfo.setFileSize("");
					lineInfo.setParentPath(directoryName);
					lineInfo.setKey(Constants.DIRECTORY+":"+directoryName + "\\" + tempLine2);
					lineInfo.setRawDataOfLine(currentLine);
					lineInfo.setItemType(Constants.DIRECTORY);

				}
				// Read file info
				else {

					String[] lineString = currentLine.split(" ");
					lineInfo.setLineNumber(lineNum);
					lineInfo.setFileName(tempLine2);
					lineInfo.setFileSize(lineString[lineString.length - 2]);
					lineInfo.setParentPath(directoryName);
					lineInfo.setKey(Constants.FILE+":"+directoryName + "\\" + tempLine2);
					lineInfo.setRawDataOfLine(currentLine);
					lineInfo.setItemType(Constants.FILE);
				}
			}
			
			if (mapValue.containsKey(lineInfo.getKey())) {
				
				mapValue.put(lineNum + lineInfo.getKey(), lineInfo);
			} else {
				mapValue.put(lineInfo.getKey(), lineInfo);
			}

			lineNum++;
		}
		
		reader.close();
		return mapValue;
	}
	// ===========================File reading ends here=========================

	// ============================Sorting the given hash map starts here ==========
	public static HashMap<String, ComparedResult> sortMap(HashMap<String, ComparedResult> map) throws Exception {
		// Create a list from elements of HashMap
		List<Map.Entry<String, ComparedResult>> list = new LinkedList<Map.Entry<String, ComparedResult>>(
				map.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, ComparedResult>>() {
			public int compare(Map.Entry<String, ComparedResult> o1, Map.Entry<String, ComparedResult> o2) {
				int status = 0;
				if (o1.getValue().getLineNumOfFile() < o2.getValue().getLineNumOfFile()) {
					status = -1;
				} else if (o1.getValue().getLineNumOfFile() > o2.getValue().getLineNumOfFile()) {
					status = 1;
				}
				return status;
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, ComparedResult> temp = new LinkedHashMap<String, ComparedResult>();
		for (Map.Entry<String, ComparedResult> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}
	// ============================Sorting the given hash maps ends here ===========

	// ============Comparing the 2 hash maps starts here======================
	public HashMap<String, ComparedResult> getCompareResult(HashMap<String, FileToHashMap> hashFile1,
			HashMap<String, FileToHashMap> hashFile2) throws Exception {
		HashMap<String, ComparedResult> resultHash = new HashMap<String, ComparedResult>();

		for (Map.Entry<String, FileToHashMap> entry1 : hashFile1.entrySet()) {
			ComparedResult compareResult = new ComparedResult();
			String key = entry1.getKey();
			FileToHashMap rowFromFile1 = entry1.getValue();

			if (hashFile2.containsKey(key)) {
				if (rowFromFile1.getFileSize().equals(hashFile2.get(key).getFileSize())) {
					compareResult.setDiffStatus(Constants.RESULT_SAME);
				} else {
					compareResult.setDiffStatus(Constants.RESULT_DIFFERENT);
				}
			} else {
				compareResult.setDiffStatus(Constants.RESULT_LEFT_ONLY);
			}
			compareResult.setLineNumOfFile(rowFromFile1.getLineNumber());

			resultHash.put(key, compareResult);
		}

		// check from file 2
		for (Map.Entry<String, FileToHashMap> entry2 : hashFile2.entrySet()) {
			ComparedResult compareResult = new ComparedResult();
			String key = entry2.getKey();
			FileToHashMap rowFromFile2 = entry2.getValue();

			// skipping items which been already checked
			if (resultHash.containsKey(key)) {
				continue;
			}

			compareResult.setDiffStatus(Constants.RESULT_RIGHT_ONLY);
			compareResult.setLineNumOfFile(rowFromFile2.getLineNumber());
			resultHash.put(key, compareResult);
		}

		return resultHash;
	}
	// ===Comparing of 2 hash maps ends here ===================

}
