package com.nichi.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.nichi.model.ComparedResult;
import com.nichi.model.Constants;
import com.nichi.model.FileToHashMap;
import com.nichi.model.LoggerClass;
import com.nichi.util.FileOperations;
import com.nichi.util.JFilePicker;

public class HomeScreen {

	private JLabel versionLabel;

	private JFrame homeScreenFrame;
	private JLabel headerLable;

	private JButton compareButton, cancelButton;

	private JFilePicker browseFile1Picker, browseFile2Picker;
	static String filePath1, filePath2;
	private File file1, file2;

	static {
		SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		System.setProperty("current.date.time", dateFormate.format(new Date()));
	}

	static HashMap<String, ComparedResult> sortedResult;
	static HashMap<String, FileToHashMap> file1HashMap, file2HashMap;

	public HomeScreen() throws Exception {
		super();

		homeScreenFrame = new JFrame("File Comparing Tool");// creating instance of JFrame
		homeScreenFrame.setSize(670, 420);
		homeScreenFrame.setLayout(null);// using no layout managers
		homeScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		versionLabel = new JLabel(Constants.VERSION);
		versionLabel.setBounds(570, 1, 100, 40);// x axis, y axis, width, height
		homeScreenFrame.add(versionLabel);

		headerLable = new JLabel("File Comparing Tool");
		headerLable.setFont(new Font("Serif", Font.PLAIN, 40));
		headerLable.setBounds(160, 30, 500, 80);// x axis, y axis, width, height
		homeScreenFrame.add(headerLable);

		browseFile1Picker = new JFilePicker("File 1 :", "Browse");
		browseFile1Picker.setBounds(60, 130, 500, 80);// x axis, y axis, width, height
		browseFile1Picker.setMode(JFilePicker.MODE_OPEN);
		browseFile1Picker.addFileTypeFilter(".txt", "Text Files");
		homeScreenFrame.add(browseFile1Picker);

		browseFile2Picker = new JFilePicker("File 2 :", "Browse");
		browseFile2Picker.setBounds(60, 200, 500, 80);// x axis, y axis, width, height
		browseFile2Picker.setMode(JFilePicker.MODE_OPEN);
		browseFile2Picker.addFileTypeFilter(".txt", "Text Files");
		homeScreenFrame.add(browseFile2Picker);

		compareButton = new JButton("Compare");// creating instance of JButton
		compareButton.setBounds(200, 300, 100, 40);// x axis, y axis, width, height
		compareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoggerClass.logInfo("Compare button clicked ");
				filePath1 = browseFile1Picker.getSelectedFilePath();
				filePath2 = browseFile2Picker.getSelectedFilePath();

				file1 = new File(filePath1);
				file2 = new File(filePath2);
				try {
					// Validate Input fields
					boolean inputCheck = validateInputFields();
					if (inputCheck) {
						readFiles();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					LoggerClass.logError(e1.toString());
				}
			}
		});
		homeScreenFrame.add(compareButton);// adding button in JFrame

		cancelButton = new JButton("Cancel");// creating instance of JButton
		cancelButton.setBounds(310, 300, 100, 40);// x axis, y axis, width, height
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoggerClass.logInfo("Cancel button from home screen is clicked to close the application ");
				homeScreenFrame.dispose();
				System.exit(0);
			}
		});
		homeScreenFrame.add(cancelButton);

		homeScreenFrame.setVisible(true);// making the frame visible

	}

	protected void readFiles() throws Exception {

		FileOperations fileOperations = new FileOperations();

		// read file 1
		LoggerClass.logInfo("File 1 rading started ");
		file1HashMap = fileOperations.readFile(filePath1);
		LoggerClass.logInfo("File 1 rading ended  ");

		// read file 2
		LoggerClass.logInfo("File 2 rading started  ");
		file2HashMap = fileOperations.readFile(filePath2);
		LoggerClass.logInfo("File 2 rading ended  ");


		// compare both files
		LoggerClass.logInfo("Maps comparing started  ");
		HashMap<String, ComparedResult> results = fileOperations.getCompareResult(file1HashMap, file2HashMap);
		LoggerClass.logInfo("Maps comparing ended  ");

		// Sort content by line number
		sortedResult = FileOperations.sortMap(results);

		LoggerClass.logInfo("Maps printing started  ");
		ResultScreen resultScreen = new ResultScreen();
		resultScreen.drawResultScreen();
		resultScreen.writeToScreen(sortedResult, file1HashMap, file2HashMap);

		LoggerClass.logInfo("Maps printing ended  ");

	}

	private boolean validateInputFields() throws Exception {
		//Input field validation
		if (filePath1.isEmpty()) {
			JOptionPane.showMessageDialog(homeScreenFrame, Constants.HOME_WARN_001);
			LoggerClass.logInfo(Constants.HOME_WARN_001);
			return false;
		}
		if (filePath2.isEmpty()) {
			JOptionPane.showMessageDialog(homeScreenFrame, Constants.HOME_WARN_002);
			LoggerClass.logInfo(Constants.HOME_WARN_002);
			return false;
		}
		//File1 validation
		if (!file1.exists()) {
			JOptionPane.showMessageDialog(homeScreenFrame, Constants.HOME_WARN_003.replace("{0}", filePath1));
			LoggerClass.logInfo(Constants.HOME_WARN_003.replace("{0}", filePath1));
			return false;
		} else {
			String line0 = getFirstLine(filePath1);
			System.out.println("filePath1:line0:"+line0);
			if (!line0.contains("Volume in drive")) {
				JOptionPane.showMessageDialog(homeScreenFrame, Constants.HOME_WARN_005);
				LoggerClass.logInfo(Constants.HOME_WARN_005);
				return false;
			}
		}
		//File 2 validation
		if (!file2.exists()) {
			JOptionPane.showMessageDialog(homeScreenFrame, Constants.HOME_WARN_004.replace("{0}", filePath2));
			LoggerClass.logInfo(Constants.HOME_WARN_004.replace("{0}", filePath2));
			return false;
		} else {
			String line0 = getFirstLine(filePath2);
			System.out.println("filePath2:line0:"+line0);
			if (!line0.contains("Volume in drive")) {
				JOptionPane.showMessageDialog(homeScreenFrame, Constants.HOME_WARN_006);
				LoggerClass.logInfo(Constants.HOME_WARN_006);
				return false;
			}
		}
		//Check to continue the compare action
		int actionType = JOptionPane.showConfirmDialog(homeScreenFrame, Constants.HOME_CONF_001, "File Comparing Tool",
				JOptionPane.YES_NO_OPTION);
		if (actionType > 0) {
			return false;
		}

		return true;

		
	}
	public static String getFirstLine(String filePath) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(filePath)); 
	    String line = br.readLine(); 
	    br.close();
	    return line;
	}
}
