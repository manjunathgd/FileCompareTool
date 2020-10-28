package com.nichi.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.log4j.Logger;

import com.nichi.model.ComparedResult;
import com.nichi.model.Constants;
import com.nichi.model.FileToHashMap;
import com.nichi.model.LoggerClass;
import com.nichi.util.FileOperations;

public class ResultScreen extends MouseAdapter {

	public void mouseClicked(MouseEvent event) {

	}

	private JFrame resultFrame;

	private JLabel resultScreenHeaderLabel;

	private JButton exportButton;
	private JRadioButton radioBtnCSV;
	private JScrollPane scrolPane;
	private JTable resultTable;
	private DefaultTableModel resultTableModel;

	Border paneEdge = BorderFactory.createLineBorder(Color.black);
	ButtonGroup exportFileTypesRadioButtonGroup = new ButtonGroup();

	// private Border padding;

	public void drawResultScreen() throws Exception {
		// padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);

		resultFrame = new JFrame();
		resultFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		resultFrame.setVisible(true);

		createOptionsPanel();

	}

	public void createOptionsPanel() throws Exception {
		Font headerFont = new Font("Verdana", Font.PLAIN, 40);

		resultScreenHeaderLabel = new JLabel("Result Screen");
		resultScreenHeaderLabel.setFont(headerFont);

		radioBtnCSV = new JRadioButton("CSV", true);
		exportFileTypesRadioButtonGroup.add(radioBtnCSV);

		exportButton = new JButton("Export");// creating instance of JButton
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				LoggerClass.logInfo("Export button is clicked ");

				int actionType = JOptionPane.showConfirmDialog(resultFrame, Constants.RESULT_CONF_002,
						"File Comparing Tool", JOptionPane.YES_NO_OPTION);
				if (actionType > 0) {
					return;
				}

				JFileChooser fileChooser = new JFileChooser();
				int option = fileChooser.showSaveDialog(resultFrame);
				if (option == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						writeDataTofile(file.getAbsoluteFile());
						LoggerClass.logInfo("Data has been exported to the path  " + file.getAbsoluteFile());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						LoggerClass.logError(e1.toString());
					}
				}
			}

		});
	}

	private void createTable() throws Exception {
		resultTable = new JTable(resultTableModel) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
				return false;
			}

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);
				Font zeroFont = new Font("Verdana", Font.PLAIN, 0);
				if (col == 0) {
					comp.setFont(zeroFont);
					comp.setBackground(Color.white);
					comp.resize(0, 0);
					return comp;
				}

				Object value = getModel().getValueAt(row, 0);

				if (col == 1) {
					if (value.equals(Integer.toString(Constants.RESULT_DIFFERENT))) {
						comp.setBackground(Color.yellow);
					} else if (value.equals(Integer.toString(Constants.RESULT_LEFT_ONLY))) {
						comp.setBackground(Color.yellow);
					} else if (value.equals(Integer.toString(Constants.RESULT_RIGHT_ONLY))) {
						comp.setBackground(Color.gray);
					} else if (value.equals(Integer.toString(Constants.RESULT_SAME))) {
						comp.setBackground(Color.white);
					}
				} else if (col == 2) {
					if (value.equals(Integer.toString(Constants.RESULT_DIFFERENT))) {
						comp.setBackground(Color.yellow);
					} else if (value.equals(Integer.toString(Constants.RESULT_LEFT_ONLY))) {
						comp.setBackground(Color.gray);
					} else if (value.equals(Integer.toString(Constants.RESULT_RIGHT_ONLY))) {
						comp.setBackground(Color.yellow);
					} else if (value.equals(Integer.toString(Constants.RESULT_SAME))) {
						comp.setBackground(Color.white);
					}
				}

				return comp;
			}
		};

		resultTable.setFocusable(false);
		// resultTable.disable();
		resultTable.getColumnModel().getColumn(0).setMaxWidth(0);
		resultTable.getColumnModel().getColumn(0).setMinWidth(0);
		resultTable.getColumnModel().getColumn(0).setWidth(0);
		resultTable.setCellSelectionEnabled(false);

		TableColumnModel jTableColumnModel = resultTable.getColumnModel();
		TableColumn column = jTableColumnModel.getColumn(0);
		column.setWidth(0);

	}

	public void writeToScreen(HashMap<String, ComparedResult> sortedResult, HashMap<String, FileToHashMap> file1map,
			HashMap<String, FileToHashMap> file2map) throws Exception {

		resultTableModel = new DefaultTableModel(0, 0);
		String header[] = new String[] { "", "File1", "File2" };
		resultTableModel.setColumnIdentifiers(header);

		try {
			for (HashMap.Entry<String, ComparedResult> entry : sortedResult.entrySet()) {

				ComparedResult result = entry.getValue();
				String key = entry.getKey();

				String file1Row = "\t\t\t\t\t\t\t\t";
				String file2Row = "\t\t\t\t\t\t\t\t";
				String statusResult = Integer.toString(result.getDiffStatus());

				if (file1map.containsKey(key)) {
					FileToHashMap itemFromFile1 = file1map.get(key);
					file1Row = itemFromFile1.getRawDataOfLine();

				}
				if (file2map.containsKey(key)) {
					FileToHashMap itemFromFile2 = file2map.get(key);
					file2Row = itemFromFile2.getRawDataOfLine();

				}
				if (file1Row.trim().length() > 0 || file2Row.trim().length() > 0) {
					resultTableModel.addRow(new Object[] { statusResult, file1Row, file2Row });
				}

			}

			createTable();

			resultFrame.setLayout(null);
			resultFrame.add(resultScreenHeaderLabel);
			resultFrame.add(radioBtnCSV);
			resultFrame.add(exportButton);
			scrolPane = new JScrollPane(resultTable);

			resultScreenHeaderLabel.setVisible(true);
			int x80PerFrame = (int) (resultFrame.getWidth() * 0.80);
			int xCenterFrame = (int) (resultFrame.getWidth() - resultFrame.getWidth() * 0.50 - 200);
			resultScreenHeaderLabel.setBounds(xCenterFrame, 30, 500, 80);
			radioBtnCSV.setBounds(x80PerFrame, 60, 60, 50);
			exportButton.setBounds(x80PerFrame + 60, 60, 120, 50);

			int yTableLocation = (int) Math.round(resultFrame.getHeight() - resultFrame.getHeight() * 0.80);
			scrolPane.setBounds(10, yTableLocation, resultFrame.getWidth() - 60,
					(int) (resultFrame.getHeight() * 0.8) - 60);
			resultFrame.add(scrolPane);
			resultFrame.setVisible(true);
			JOptionPane.showMessageDialog(resultFrame, Constants.RESULT_INFO_001);
			LoggerClass.logInfo(Constants.RESULT_INFO_001);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			LoggerClass.logError(e1.toString());
		}

	}

	private void writeDataTofile(File absoluteFile) throws Exception {

		OutputStreamWriter myWriter;
		String delimeter = "\",\"";

		FileOutputStream fileOutputStream = new FileOutputStream(absoluteFile);

		myWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
		Writer out = new BufferedWriter(myWriter);
		out.write('\ufeff');
		
		String fileHeader = "Result" + delimeter + "ParentFullPath" + delimeter + HomeScreen.filePath1 + delimeter
				+ HomeScreen.filePath2;

		out.write("\"" + fileHeader + "\"" + '\n');

		for (Map.Entry<String, ComparedResult> entry : HomeScreen.sortedResult.entrySet()) {
			ComparedResult hashMapObj = entry.getValue();
			String status = "";

			String leftString = "", rightString = "";
			String parentPath = "";

			switch (hashMapObj.getDiffStatus()) {
			case Constants.RESULT_LEFT_ONLY:
				status = "Left Only";
				leftString = HomeScreen.file1HashMap.get(entry.getKey()).getRawDataOfLine();
				parentPath = HomeScreen.file1HashMap.get(entry.getKey()).getParentPath();
				break;
			case Constants.RESULT_RIGHT_ONLY:
				status = "Right Only";
				rightString = HomeScreen.file2HashMap.get(entry.getKey()).getRawDataOfLine();
				parentPath = HomeScreen.file2HashMap.get(entry.getKey()).getParentPath();
				break;
			case Constants.RESULT_DIFFERENT:
				status = "Different";
				leftString = HomeScreen.file1HashMap.get(entry.getKey()).getRawDataOfLine();
				rightString = HomeScreen.file2HashMap.get(entry.getKey()).getRawDataOfLine();
				parentPath = HomeScreen.file1HashMap.get(entry.getKey()).getParentPath();
				break;
			case Constants.RESULT_SAME:
				status = "Same";
				leftString = HomeScreen.file1HashMap.get(entry.getKey()).getRawDataOfLine();
				rightString = HomeScreen.file2HashMap.get(entry.getKey()).getRawDataOfLine();
				parentPath = HomeScreen.file1HashMap.get(entry.getKey()).getParentPath();
				break;
			default:
				break;
			}

			if (leftString.trim().length() > 0 || rightString.trim().length() > 0) {
				String temp = status + delimeter + parentPath + delimeter + leftString + delimeter + rightString;
				out.write("\"" + temp + "\"" + '\n');
			}
		}
		out.close();

		JOptionPane.showMessageDialog(resultFrame, Constants.RESULT_INFO_002);
		LoggerClass.logInfo(Constants.RESULT_INFO_002);
	}

}
