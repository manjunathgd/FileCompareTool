package com.nichi.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.nichi.view.HomeScreen;

//Start
public class Luncher {

	static {
		SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		System.setProperty("current.date.time", dateFormate.format(new Date()));
	}
	
	final static Logger log = Logger.getLogger(Luncher.class);
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			new HomeScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
