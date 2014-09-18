/*Copyright (c) 2014, Ivan Pozharsky, pozharsky@inbox.ru
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation and/or 
other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors may be 
used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/

package com.github.gpatterns;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class FretboardImage {
	void createFretboardImage( int nNumOfStrings, int nNumOfFrets ) throws IOException{
		_image = new BufferedImage( START_WIDTH + ( nNumOfStrings - 4 ) * TILE_WIDTH
								  , START_HEIGHT + ( nNumOfFrets - 4 ) * TILE_HEIGHT
								  , BufferedImage.TYPE_BYTE_BINARY );
		_g2d = _image.createGraphics();
		_g2d.setBackground( new Color( 255, 255, 255 ) );
		_g2d.clearRect(0, 0, START_WIDTH + nNumOfStrings * TILE_WIDTH, START_HEIGHT + nNumOfFrets * TILE_HEIGHT);
		_g2d.setColor( new Color( 0, 0, 0 ) );

		BufferedImage fboard = ImageIO.read( getClass().getResource( BASE_FRET_DIAGRAM_PATH ) );
		BufferedImage rtTile = ImageIO.read( getClass().getResource( RT_CORNER_FRET_DIAGRAM_PATH ) );
		BufferedImage lbTile = ImageIO.read( getClass().getResource( LB_CORNER_FRET_DIAGRAM_PATH ) );
		
		_g2d.drawImage( fboard, 50, 50, null );
		for( int nStrings = 0; nStrings < ( nNumOfStrings - 4 ); nStrings++ ){
			for( int nFret = 0; nFret < nNumOfFrets; nFret++ ){
				_g2d.drawImage( rtTile, START_WIDTH - 50 + nStrings * HOR_OFFSET_ADD
									  , 50 + nFret * VERT_OFFSET_ADD, null );
			}
		}
		
		for( int nStrings = 0; nStrings < nNumOfStrings - 1; nStrings++ ){
			for( int nFret = 0; nFret < ( nNumOfFrets - 4 ); nFret++ ){
				_g2d.drawImage( lbTile, 50 + nStrings * HOR_OFFSET_ADD
									  , START_HEIGHT - 50 + nFret * VERT_OFFSET_ADD, null );
			}
		}
		
	}
	void addStringNames( List<String> names ){
	    _g2d.setFont( new Font( "Arial", Font.BOLD, 30 ) );
	    int nCount = 0;
		for( String s : names ){
			_g2d.drawString( s, HOR_STRING_OFFSET + nCount * HOR_OFFSET_ADD, VERT_STRING_OFFSET );
			nCount ++;
		}
	}
	void addFretNum( String fretNum ){
		_g2d.setFont( new Font( "Arial", Font.BOLD, 25 ) );
		_g2d.drawString( fretNum, 3, VERT_OFFSET + VERT_OFFSET_ADD );	
	}
	void addNote( int nString, int nFret, boolean bIsRoot ) throws IOException{
		BufferedImage fbnote;
		if( bIsRoot ){
			fbnote = ImageIO.read( getClass().getResource( ROOT2_FRET_DIAGRAM_PATH ) );			
		} else {
			fbnote = ImageIO.read( getClass().getResource( NOTE_FRET_DIAGRAM_PATH ) );
		}
		_g2d.drawImage( fbnote, HOR_OFFSET + nString * HOR_OFFSET_ADD
							  , VERT_OFFSET + nFret * VERT_OFFSET_ADD, null );
	}
	void saveImage( File file ) throws IOException{
		ImageIO.write( _image, "PNG", file );
	}
	
	private BufferedImage _image;
	private Graphics2D _g2d;

	private final String BASE_PATH = "res/";
	private final String BASE_FRET_DIAGRAM_PATH = BASE_PATH + "BaseFretDgrm.png";
	private final String NOTE_FRET_DIAGRAM_PATH = BASE_PATH + "Note.png";
	//private final String ROOT1_FRET_DIAGRAM_PATH = BASE_PATH + "RootSqr.png";
	private final String ROOT2_FRET_DIAGRAM_PATH = BASE_PATH + "RootRnd.png";
	private final String RT_CORNER_FRET_DIAGRAM_PATH = BASE_PATH + "RTCorner.png";
	private final String LB_CORNER_FRET_DIAGRAM_PATH = BASE_PATH + "LBCorner.png";
	private final int START_WIDTH = 164 + (2*50); 
	private final int START_HEIGHT = 290 + (2*50);
	private final int TILE_HEIGHT = 72;
	private final int TILE_WIDTH = 54;
	private final int HOR_STRING_OFFSET = 40;
	private final int VERT_STRING_OFFSET = 40;
	private final int HOR_OFFSET = 25;
	private final int HOR_OFFSET_ADD = 54;
	private final int VERT_OFFSET = 61;
	private final int VERT_OFFSET_ADD = 72;
}
