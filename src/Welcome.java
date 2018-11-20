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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Welcome extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	JButton start;
	public ImageFrame character;

	public Welcome(){

		character = new ImageFrame("recommendation.png");
		character.start();

		Panel image = new Panel(new GridLayout(1,1));
		image.add(character);
		image.setBackground(new Color(10,10,30));

		Panel south = new Panel (new GridLayout(5,1));
		south.setBackground(new Color(255,255,255));
		start = new JButton("Start");
		start.addActionListener(this);
		start.setBackground(new Color(10,10,10));
		start.setForeground(Color.WHITE);
		start.setFont(new Font("Comic Sans", Font.PLAIN, 20));

		Panel title = new Panel(new GridLayout(1,1));
		JLabel titleMsg = new JLabel("Dr. Mario. Food Sensei");
		titleMsg.setFont(new Font("Comic Sans", Font.PLAIN, 20));
		titleMsg.setHorizontalAlignment(SwingConstants.CENTER);
		title.add(titleMsg);

		Panel empty2 = new Panel(new GridLayout(1,1));
		Panel empty3 = new Panel(new GridLayout(1,1));
		Panel empty4 = new Panel(new GridLayout(1,1));

		south.add(empty2);
		south.add(title);
		south.add(empty3);
		south.add(empty4);
		south.add(start);

		this.setLayout(new GridLayout(1,2));
		this.add(image);
		this.add(south);
		this.setVisible(true);
		this.setSize(500, 350);

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public void paint (Graphics gra){
		character.paint(gra);
	}


	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(start)){
			this.setVisible(false);
			IMC imc = new IMC();
		}
	}

	public static void main(String[] args) {

		Welcome welcome = new Welcome();

	}

}
