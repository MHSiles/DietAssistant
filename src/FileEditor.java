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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileEditor extends Thread{

	BufferedWriter bufferW = null;
	FileWriter fileW = null;
	String fact, file;
	Boolean keepInformation = true;

	public FileEditor(String file, String fact){
		this.fact = fact;
		this.file = file;
	}

	public FileEditor(String file, String fact, Boolean keepInformation){
		this.fact = fact;
		this.file = file;
		this.keepInformation = keepInformation;
	}

	@Override
	public void run() {
		try{
			fileW = new FileWriter(file, keepInformation);
			bufferW = new BufferedWriter(fileW);
			bufferW.write("\n" + fact);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}

		finally{

			try{
				if(bufferW != null)
					bufferW.close();
				if(fileW != null)
					fileW.close();
			}catch(IOException e){
				System.err.println(e.getMessage());
			}

		}

	}


}
