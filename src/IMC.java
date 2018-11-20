// Copyright 2018, Mauricio Hernández

// This file is part of Diet Assistant.
//
// Diet Assistant is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Diet Assistant is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Diet Assistant.  If not, see <https://www.gnu.org/licenses/>.

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import org.jpl7.Query;

public class IMC extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	Query fileConsult = new Query("consult('./prolog/diet.pl')");
	public ImageFrame character;
	JButton continueBtn;
	JLabel weight, height;
	JTextField weightField, heightField;
	Float wei, hei, imc;

	public IMC(){

		fileConsult.hasMoreSolutions();

		character = new ImageFrame("analysing.png");
		character.start();

		Panel image = new Panel(new GridLayout(1,1));
			image.add(character);
			image.setBackground(new Color(10,10,30));

		Panel right = new Panel (new GridLayout(5,1));
		right.setBackground(new Color(255,255,255));

			Panel nextBtn = new Panel(new GridLayout(1,1));
			continueBtn = new JButton("Continue");
			continueBtn.addActionListener(this);
			continueBtn.setBackground(new Color(10,10,10));
			continueBtn.setForeground(Color.WHITE);
			continueBtn.setFont(new Font("Comic Sans", Font.PLAIN, 20));
			nextBtn.add(continueBtn);

			Panel title = new Panel(new GridLayout(1,1));
			JLabel titleMsg = new JLabel("Please enter the following information:");
			title.add(titleMsg);

			Panel weightPanel = new Panel(new GridLayout(2,1));
			weight = new JLabel("Weight(kg):");
			weightField = new JTextField();
			weightPanel.add(weight);
			weightPanel.add(weightField);

			Panel heightPanel = new Panel(new GridLayout(2,1));
			height = new JLabel("Height(m):");
			heightField = new JTextField();
			heightPanel.add(height);
			heightPanel.add(heightField);

			Panel empty = new Panel(new GridLayout(1,1));

			right.add(title);
			right.add(weightPanel);
			right.add(heightPanel);
			right.add(empty);
			right.add(nextBtn);

		this.setLayout(new GridLayout(1,2));
		this.add(image);
		this.add(right);
		this.setVisible(true);
		this.setSize(500, 350);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}


	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(continueBtn)){

			try{
				wei = Float.parseFloat(weightField.getText());
				hei = Float.parseFloat(heightField.getText());
			}catch(Exception e){

				System.err.println(e.getMessage());
			}

			imc = wei/(hei*hei);
			imc = (float) (Math.round(imc*100)/100);

			try {
				setIMC(imc);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.setVisible(false);
			Assistant assistant = new Assistant();

		}
	}

	public void setIMC(double imc) throws InterruptedException{

		Query imcQuery = new Query("getIMCStatus(" + Double.toString(imc) + ", IMC).");
		if(imcQuery.hasSolution()){
			String imcStatus = imcQuery.oneSolution().get("IMC").toString();

			String fact = "imcStatus(" + imcStatus + ").";
			String file = "./prolog/personalIMC.pl";

			FileEditor fileEditor = new FileEditor(file, fact, false);
			fileEditor.start();
			fileEditor.join();

		}

	}



}
