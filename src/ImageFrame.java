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

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageFrame extends JPanel implements Runnable{

	private static final long serialVersionUID = 1L;
	Thread thread = null;
	ImageIcon character;
	Color color = new Color(255,255,255);
	public String text, line1 = "", line2 = "", line3 = "", line4 = "", line5 = "", line6 = "";
	public String line7 = "", line8 = "", line9 = "", line10 = "", line11 = "", line12 = "";
	public boolean flag = true, characterFlag = false, textFlag = false, multipleLines = false;


	public ImageFrame(String image){
		this.character = new ImageIcon(getClass().getResource(image));
		characterFlag = true;
	}

	public ImageFrame(){
		textFlag = true;
		this.text = "Hi, feel free to ask whatever you want.";
	}

	public void paint(Graphics gra){
		gra.setColor(color);
		gra.fillRect(0, 0, 500, 500);
		if (characterFlag){
			gra.setColor(new Color(0,0,0));
			gra.drawImage(character.getImage(), 20, 40, 200, 200, this);
		}
		if(textFlag){
			gra.setColor(new Color(0,0,0));
			if(multipleLines){
				gra.drawString(line1, 10, 50);
				gra.drawString(line2, 10, 65);
				gra.drawString(line3, 10, 80);
				gra.drawString(line4, 10, 95);
				gra.drawString(line5, 10, 110);
				gra.drawString(line6, 10, 125);
				gra.drawString(line7, 10, 140);
				gra.drawString(line8, 10, 155);
				gra.drawString(line9, 10, 170);
				gra.drawString(line10, 10, 185);
				gra.drawString(line11, 10, 200);
				gra.drawString(line12, 10, 215);

			}else{
				gra.drawString(text, 10, 120);
			}

		}
//		gra.setColor(new Color(0,0,0));

	}

	public void run() {
		while(true){
			try{
				Thread.sleep(200);
			}catch(Exception e){

			}
			repaint();
		}
	}

	public void start(){
//		if(thread == null){
		thread = new Thread(this);
		thread.start();
//		}
	}

	public void changeImage(String image){
		this.character = new ImageIcon(getClass().getResource(image));
	}

	public void changeText(String text){
		this.text = text;
	}

	public synchronized void resetLines(){
		this.line1 = "";
		this.line2 = "";
		this.line3 = "";
		this.line4 = "";
		this.line5 = "";
		this.line6 = "";
		this.line7 = "";
		this.line8 = "";
		this.line9 = "";
		this.line10 = "";
		this.line11 = "";
		this.line12 = "";
	}

	public synchronized void changeMultipleLinesText(int line, String text){
//		resetLines();
//		System.out.println(line);
		if(line == 1){ this.line1 = text;
		}else if(line == 2){ this.line2 = text;
		}else if(line == 3){ this.line3 = text;
		}else if(line == 4){ this.line4 = text;
		}else if(line == 5){ this.line5 = text;
		}else if(line == 6){ this.line6 = text;
		}else if(line == 7){ this.line7 = text;
		}else if(line == 8){ this.line8 = text;
		}else if(line == 9){ this.line9 = text;
		}else if(line == 10){ this.line10 = text;
		}else if(line == 11){ this.line11 = text;
		}else if(line == 12){ this.line12 = text;
		}
	}

	public void enableMultipleLines(){
		this.multipleLines = true;
	}

	public void disableMultipleLines(){
		this.multipleLines = false;
	}

}
