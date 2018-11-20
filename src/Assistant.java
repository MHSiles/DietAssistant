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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import javax.swing.*;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;

public class Assistant extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	public ImageFrame character, response;
	JButton sendBtn, quitBtn;
	JLabel instructions, question;
	JTextField questionField;
	private String SENTENCE;
	String answer;

	public Assistant(){


  		Panel topContent = new Panel (new GridLayout(1,2));
  		topContent.setBackground(new Color(255,255,255));

	  		Panel image = new Panel(new BorderLayout());
		  		character = new ImageFrame("recommendation.png");
		  		character.start();
	  			image.add(character);
	  			image.setBackground(new Color(10,10,30));

  			Panel right = new Panel(new GridLayout(4,1));

	          Panel instructionsPanel = new Panel(new GridLayout(1,1));
	    			instructions = new JLabel("<html>Please, do one of the following:<br/><br/>1. What you like or dont <br/>2. Is _____ healthy/unhealthy</html>");
	          instructionsPanel.add(instructions);

	          Panel optionsPanel = new Panel(new GridLayout(1,1));
	          question = new JLabel("<html>3. Can I eat ____<br/>4. Ask for recommendations<br/>5. How frecuently can I eat _____<br/></html>");
	    			optionsPanel.add(question);

			  Panel questionPanel = new Panel(new GridLayout(1,1));
				  questionField = new JTextField();
				  questionPanel.add(questionField);

	          Panel askPanel = new Panel(new GridLayout(1,1));
		          sendBtn = new JButton("Ask");
		          sendBtn.addActionListener(this);
		          sendBtn.setBackground(new Color(10,10,10));
		          sendBtn.setForeground(Color.WHITE);
		          sendBtn.setFont(new Font("Comic Sans", Font.PLAIN, 20));
	          askPanel.add(sendBtn);

	          right.add(instructionsPanel);
	          right.add(optionsPanel);
	          right.add(questionPanel);
	          right.add(askPanel);

		topContent.add(image);
		topContent.add(right);

  		Panel bottomContent = new Panel (new BorderLayout());
  		bottomContent.setBackground(new Color(255,255,255));
	  		quitBtn = new JButton("Quit");
			quitBtn.addActionListener(this);
			quitBtn.setBackground(new Color(150,10,10));
			quitBtn.setForeground(Color.WHITE);
			quitBtn.setFont(new Font("Comic Sans", Font.PLAIN, 20));
			response = new ImageFrame();
			response.start();
		bottomContent.add(response);
		bottomContent.add(quitBtn, BorderLayout.SOUTH);

  		this.setLayout(new GridLayout(2,1));
  		this.add(topContent);
  		this.add(bottomContent);
  		this.setVisible(true);
  		this.setSize(500, 600);

  		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}


	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(sendBtn)){
			Query fileConsult = new Query("consult('./prolog/diet.pl')");
			fileConsult.hasMoreSolutions();

//			GET THE INPUT
			SENTENCE =  questionField.getText();
//			CHANGE ALL THE WORDS TO LOWER CASE
			SENTENCE = SENTENCE.toLowerCase();
//			CHANGE THE STRING TO AN ARRAY OF WORDS
			String wordArray[] = SENTENCE.split(" ");
//			System.out.println(Arrays.toString(wordArray));

			String inputType = identifyQuestion(wordArray);
//			System.out.println(inputType);

			if(inputType.equals("statement")){
				response.disableMultipleLines();
				setStatement(wordArray);
			}else if(inputType.equals("health")){
				response.disableMultipleLines();
				healthQuestion(wordArray);
			}else if(inputType.equals("question")){
				response.disableMultipleLines();
				answerSuggestion(wordArray);
			}else if(inputType.equals("recommendation")){
				response.enableMultipleLines();
				recommendations();
			}else if(inputType.equals("frecuency")){
				response.enableMultipleLines();
				eatingFrecuency(wordArray);
			}else if(inputType.equals("error")){
				response.changeText("I couldn't understand what you are asking.");
			}
		}else if(ae.getSource().equals(quitBtn)){

			String file = "./prolog/personalPreference.pl" ;
			String file2 = "./prolog/personalNoPreference.pl" ;
			String file3 = "./prolog/personalIMC.pl" ;

			FileEditor fileEditor = new FileEditor(file, "", false);
			FileEditor fileEditor2 = new FileEditor(file2, "", false);
			FileEditor fileEditor3 = new FileEditor(file3, "", false);

			fileEditor.start();
			fileEditor2.start();
			fileEditor3.start();

			try{
				fileEditor.join();
				fileEditor2.join();
				fileEditor3.join();
			}catch(Exception e){
				e.printStackTrace();
			}

			this.setVisible(false);
		}
	}

	public String identifyQuestion(String[] sentence){

		Query phraseQuery = new Query("readPhrase(" + Arrays.toString(sentence) + ", Ans).");
		if(phraseQuery.hasSolution()){
			answer = phraseQuery.oneSolution().get("Ans").toString();
			return answer;
		}else{
			return "error";
		}


	}

	public void setStatement(String[] sentence){

//		System.out.println("IN");
		Query phraseQuery = new Query("statement(" + Arrays.toString(sentence) + ",Negation,Preference,Food).");
		if(phraseQuery.hasSolution()){
			String negation = phraseQuery.oneSolution().get("Negation").toString();
			String preference = phraseQuery.oneSolution().get("Preference").toString();
			String food = phraseQuery.oneSolution().get("Food").toString();

			if(preference.equals("preference")){
				preference = (Boolean.parseBoolean(negation))? "nopreference" : "preference" ;
			}else{
				preference = (Boolean.parseBoolean(negation))? "preference" : "nopreference" ;
			}

			System.out.println("previouslySaid(" + preference + ", preference(" + food + "), nopreference(" + food + ")).");

			Query previousKnowledge = new Query("previouslySaid(" + preference + ", preference(" + food + "), nopreference(" + food + ")).");
			if(previousKnowledge.hasSolution()){

				response.changeText("Oh, I already knew that.");

			}else{

				Query previousKnowledgeInverted = new Query("previouslySaid(" + preference + ", nopreference(" + food + "), preference(" + food + ")).");
				if(previousKnowledgeInverted.hasSolution()){

					if(preference.equals("preference")){
						response.changeText("Make up your mind, you told me you don't like " + food + ".");
					}else{
						response.changeText("Make up your mind, you told me you like " + food + ".");
					}

				}else{

					String fact, file;

					if(preference.equals("preference")){
						fact = "preference(" + food + ").";
						file = "./prolog/personalPreference.pl" ;
					}else{
						fact = "nopreference(" + food + ").";
						file = "./prolog/personalNoPreference.pl" ;
					}

					FileEditor fileEditor = new FileEditor(file, fact);
					fileEditor.start();
					try{
						fileEditor.join();
					}catch(Exception e){
						e.printStackTrace();
					}

					Random random = new Random();

					int rand = random.nextInt((3-1) + 1) + 1;

					character.changeImage("surprised.png");

					if(rand == 1){
						response.changeText("Good to know.");
					}else if(rand == 2){
						response.changeText("Ok, nice.");
					}else if(rand == 3){
						response.changeText("Oh, all right then.");
					}
				}
			}
		}
	}

	public void healthQuestion(String[] sentence){


		Query phraseQuery = new Query("identifyFoodHealth(" + Arrays.toString(sentence) + ")");
		Query healthQuery = new Query("getQuestion(" + Arrays.toString(sentence) + ", Response)");
		if(healthQuery.hasSolution()){
			String health = healthQuery.oneSolution().get("Response").toString();
			if(health.equals("healthy")){
				if(phraseQuery.hasSolution()){
					response.changeText("Of course, it is healthy.");
				}else{
					response.changeText("No, that should be avoided.");
				}
			}else{
				if(phraseQuery.hasSolution()){
					response.changeText("Of course, that should be avoided.");
				}else{
					response.changeText("No, that's healthy.");
				}
			}

		}


	}

	public void answerSuggestion(String[] sentence){

		Query phraseQuery = new Query("suggestion(" + Arrays.toString(sentence) + ", Suggestion)");
		if(phraseQuery.hasSolution()){
			String suggestion = phraseQuery.oneSolution().get("Suggestion").toString();
			if(suggestion.equals("dislike")){
				character.changeImage("analysing.png");
				response.changeText("Well, you told me you don�t like it...");
			}else if(suggestion.equals("sure")){
				character.changeImage("food.png");
				response.changeText("Yes, of course you can eat it.");
			}else if(suggestion.equals("no")){
				character.changeImage("analysing.png");
				response.changeText("Right now I don't think that's the best thing for you to eat.");
			}else if(suggestion.equals("yes")){
				character.changeImage("recommendation.png");
				response.changeText("Yes, but always keep a balance.");
			}else if(suggestion.equals("never")){
				character.changeImage("surprised.png");
				response.changeText("No, just try to avoid eating it for a while.");
			}
		}
	}

	public void recommendations(){

		Query imcQuery = new Query("imcStatus(IMC).");
		if(imcQuery.hasSolution()){
			String imc = imcQuery.oneSolution().get("IMC").toString();

			Query predicateExist = new Query("predicateExists(nopreference(_)).");
			String flag = (predicateExist.hasSolution())? "true" : "false";

			Variable Group = new Variable("Group");
			Variable Food = new Variable("Food");

			LinkedList<String> groupList = new LinkedList<String>();

			LinkedList<String[]> foodList = new LinkedList<String[]>();

			String[] groupFoodList = new String[15];

			int j = 0;


			for(Map m : new Query( "getRecommended", new Term[] {new Atom(flag), new Atom(imc), Group, Food} )){
				if(!groupList.isEmpty()){
					if(!groupList.getLast().equals(m.get("Group").toString())){
						foodList.add(groupFoodList);
						j = 0;
						groupFoodList = new String[15];
						groupList.add(m.get("Group").toString());
						groupFoodList[j] = m.get("Food").toString();
					}else{
						groupFoodList[j] = m.get("Food").toString();
						j++;
					}
				}else{
					groupList.add(m.get("Group").toString());
					groupFoodList[j] = m.get("Food").toString();
					j++;
				}
			}

			Variable Type = new Variable("Type");
			String types;

			for(int i = 0; i < groupList.size() ; i++){
				types = "(";
				for(Map m : new Query( "pyramid", new Term[] {new Atom(groupList.get(i)), Type} )){
					types += m.get("Type").toString() + ", ";
				}
				types += ") ";
				groupList.set(i, types);

			}

			foodList.add(groupFoodList);

			int printPosition = 1;

			response.resetLines();

			for(int i = 0; i< groupList.size() ; i++){
				String groupInfo = groupList.get(i) + ": ";
				String groupInfoContinue = "";

				Boolean overflow = false;

//				System.out.println("OV1: "  + overflow);

				for(int k = 0; k < foodList.get(i).length ; k++){
					if(foodList.get(i)[k] != null){

						overflow = (k>7)? true : false ;

						if(!overflow){
							groupInfo += foodList.get(i)[k] + ", ";
						}else{
							if(k <= 7){
								groupInfo += foodList.get(i)[k] + ", ";
							}else{
								groupInfoContinue += foodList.get(i)[k] + ", ";
							}
						}

					}
				}
				character.changeImage("food.png");
				response.changeMultipleLinesText(printPosition, groupInfo);
				printPosition++;
				if(overflow){
					response.changeMultipleLinesText(printPosition, groupInfoContinue);
				}
				printPosition++;

			}

		}else{
			response.changeMultipleLinesText(3, "Maybe something went wrong");
		}

	}

	public void eatingFrecuency(String[] sentence){

		Query foodQuery = new Query("identifyFood(" + Arrays.toString(sentence) + ",Food).");
		if(foodQuery.hasSolution()){
			String food = foodQuery.oneSolution().get("Food").toString();

			Query frecuencyQuery = new Query("eatFrecuency(" + food + ",Preference, Group, Frecuency).");
			if(frecuencyQuery.hasSolution()){
				System.out.println(frecuencyQuery.oneSolution().get("Preference").toString());
				Boolean preference = Boolean.parseBoolean(frecuencyQuery.oneSolution().get("Preference").toString());
				String group = frecuencyQuery.oneSolution().get("Group").toString();
				String frecuency = frecuencyQuery.oneSolution().get("Frecuency").toString();


				Query predicateExist = new Query("predicateExists(nopreference(_)).");
				String flag = (predicateExist.hasSolution())? "true" : "false";
				Boolean exists = Boolean.parseBoolean(flag);

				Variable Food = new Variable("Food");

				Query groupFoodQuery;

				if(exists){
					groupFoodQuery = new Query( "getGroupFood", new Term[] {new Atom(group), Food} );
				}else{
					groupFoodQuery = new Query( "getGroupFood2", new Term[] {new Atom(group), Food} );
				}

				LinkedList<String[]> foodList = new LinkedList<String[]>();

				String[] groupFoodList = new String[15];

				int j = 0;


				for(Map m : groupFoodQuery){
					groupFoodList[j] = m.get("Food").toString();
					j++;
				}

				System.out.println(preference);

				response.resetLines();

				Boolean overflow = false;
				String groupInfo = "";
				String groupInfoContinue = "";

				for(int k = 0; k < groupFoodList.length ; k++){
					if(groupFoodList[k] != null){
						overflow = (k>8)? true : false ;

						if(!groupFoodList[k].equals(food)){
							if(!overflow){
								groupInfo += groupFoodList[k] + ", ";
							}else{
								if(k <= 7){
									groupInfo += groupFoodList[k] + ", ";
								}else{
									groupInfoContinue += groupFoodList[k] + ", ";
								}
							}
						}
					}

				}
				character.changeImage("food.png");

				if(preference){
					response.changeMultipleLinesText(1, "I see you love " + food + ".");
					if(frecuency.equals("0")){
						response.changeMultipleLinesText(3, "It's not recommended on a daily basis, so try eating at most 2 times a week.");
					}else if(frecuency.equals("1")){
							response.changeMultipleLinesText(3, "You can eat it just " + frecuency + " time a day.");
							response.changeMultipleLinesText(4, "But try alternating the " + food + " with some of these, also:");
							response.changeMultipleLinesText(6, groupInfo);
							if(overflow){
								response.changeMultipleLinesText(7, groupInfoContinue);
							}
					}else{
						response.changeMultipleLinesText(3, "You can eat it up to " + frecuency + " times a day.");
						response.changeMultipleLinesText(4, "If you want something different, also you can eat these:");
						response.changeMultipleLinesText(6, groupInfo);
						if(overflow){
							response.changeMultipleLinesText(7, groupInfoContinue);
						}
					}

				}else{
					if(frecuency.equals("0")){
						response.changeMultipleLinesText(1,  food + " is not recommended on a daily basis. At most 2 times a week.");
					}else if(frecuency.equals("1")){
							response.changeMultipleLinesText(1, "You can eat " + food + " just " + frecuency + " time per day.");
							response.changeMultipleLinesText(4, "But try alternating the " + food + " with some of these, also:");
							response.changeMultipleLinesText(6, groupInfo);
							if(overflow){
								response.changeMultipleLinesText(7, groupInfoContinue);
							}
					}else{
						response.changeMultipleLinesText(1, "You can eat " + food + " up to " + frecuency + " times a day.");
						response.changeMultipleLinesText(4, "If you want something different, also you can eat these:");
						response.changeMultipleLinesText(6, groupInfo);
						if(overflow){
							response.changeMultipleLinesText(7, groupInfoContinue);
						}
					}

				}

			}
		}

	}

}
