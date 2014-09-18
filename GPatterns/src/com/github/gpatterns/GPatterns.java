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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class GPatterns {
	public static void main( String[] args ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				GPatterns gp = new GPatterns();
				gp.createAndShowUI();
			}
		});
	}
	
	public void createAndShowUI(){
		String lang = System.getProperty( "user.language" );
		Locale currentLocale = new Locale( lang );
		
		ResourceBundle messages = ResourceBundle.getBundle( "com.github.gpatterns.res.GPatterns", currentLocale);
		
		_overWriteMsg = messages.getString( "overWriteMsg" );
		_overWriteTitle = messages.getString( "overWriteTitle" );
        
		String[] lngs = { "en", "ru" };
        _lngCmb = new JComboBox<>( lngs );
        _lngCmb.setSelectedItem( lang );
        _lngCmb.addActionListener( new ChangeLng() );
		
		_frame = new JFrame( messages.getString( "title" ) );
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().setLayout(new GridBagLayout());

		_saveBtn = new JButton( messages.getString( "saveBtn" ) );
		Dimension dim = _saveBtn.getPreferredSize();
		dim.width = 100;
		_saveBtn.setPreferredSize( dim );
		_saveBtn.addActionListener( new ImageSaver() );
		
		_firstFretLbl = new JLabel( messages.getString( "firstFret" ) );
		_guitarTuningLbl = new JLabel( messages.getString( "guitarTuning" ) );
		_numOfStringsLbl = new JLabel( messages.getString( "numberOfStrings" ) );
		_numOfFretsLbl = new JLabel( messages.getString( "numberOfFrets" ) );
		
		_rootChk = new JCheckBox( messages.getString( "rootNote" ) );
		_rootChk.setSelected( false );
		_rootChk.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_rootAlt1.setEnabled( _rootChk.isSelected() );
				_rootAlt2.setEnabled( _rootChk.isSelected() );
				_rootCmb.setEnabled( _rootChk.isSelected() );
				if( _rootChk.isSelected() ){
					setRoot();
				}
			}
		});

		Integer[] numOfFretsItems = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
		_firstFretCmb = new JComboBox<>( numOfFretsItems );
		_firstFretCmb.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if( _rootChk.isSelected() ){
					setRoot();
				}
			}
		});
		
		Integer[] numOfStringsItems = { 4, 5, 6, 7, 8 };
		_numOfStringsCmb = new JComboBox<>( numOfStringsItems );
		_numOfStringsCmb.setSelectedItem( _nNumOfStrings );
		_numOfStringsCmb.addActionListener( new NumOfStringsAL() );
		
		Integer[] fretItems = { 4, 5, 6, 7, 8, 9, 10 };
		_numOfFretsCmb = new JComboBox<>( fretItems );
		_numOfFretsCmb.setSelectedItem( _nNumOfFrets );
		_numOfFretsCmb.addActionListener( new NumOfFretsAL() );
		
        _rootAlt1 = new JToggleButton( "b" );
        _rootAlt1.setEnabled( false );
        _rootAlt1.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if( _rootAlt2.isSelected() ){
					_rootAlt2.setSelected( false );
				}
				setRoot();
			}
		});
        _rootAlt2 = new JToggleButton( "#" );
        _rootAlt2.setEnabled( false );
        _rootAlt2.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if( _rootAlt1.isSelected() ){
					_rootAlt1.setSelected( false );
				}
				setRoot();
			}
		});
        _rootCmb = new JComboBox< String > ( _stringTuneItems );
        _rootCmb.setEnabled( false );
        _rootCmb.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setRoot();
			}
		});
       		
		_pane = _frame.getContentPane();
		
		GridBagConstraints saveBtnConstraints = new GridBagConstraints();
		saveBtnConstraints.gridy = UIRaw.SAVEBTNRAW.ordinal();
		saveBtnConstraints.gridwidth = 3;
		saveBtnConstraints.insets =  new Insets( 4, 4, 4, 4 );
		saveBtnConstraints.anchor = GridBagConstraints.WEST;

		_pane.add( _saveBtn, saveBtnConstraints );
		
		GridBagConstraints labelConstraints = new GridBagConstraints();
		labelConstraints.insets = new Insets( 4, 4, 4, 4 );
		labelConstraints.gridwidth = 6;
		labelConstraints.anchor = GridBagConstraints.WEST;
		labelConstraints.gridy = UIRaw.FIRSTFRETRAW.ordinal();
		
		_pane.add( _firstFretLbl, labelConstraints );
		
		labelConstraints.gridy = UIRaw.NUMOFFRETSRAW.ordinal();
		
		_pane.add( _numOfFretsLbl, labelConstraints );
		
		labelConstraints.gridy = UIRaw.TUNINGRAW.ordinal();
		
		_pane.add( _guitarTuningLbl, labelConstraints );
		
		labelConstraints.gridy = UIRaw.NUMOFSTRINGSRAW.ordinal();
		
		_pane.add( _numOfStringsLbl, labelConstraints );
		
		labelConstraints.gridy = UIRaw.ROOTRAW.ordinal();
		
		_pane.add( _rootChk, labelConstraints );
		
		GridBagConstraints comboBoxConstraints = new GridBagConstraints();
		comboBoxConstraints.insets = new Insets( 4, 4, 4, 4 );
		comboBoxConstraints.gridwidth = 2;
		comboBoxConstraints.anchor = GridBagConstraints.WEST;
		comboBoxConstraints.gridy = UIRaw.FIRSTFRETRAW.ordinal();
		comboBoxConstraints.gridx = 6;
		
		_pane.add( _firstFretCmb, comboBoxConstraints );
		
		comboBoxConstraints.gridy = UIRaw.NUMOFFRETSRAW.ordinal();
		
		_pane.add( _numOfFretsCmb, comboBoxConstraints );
		
		comboBoxConstraints.gridy = UIRaw.NUMOFSTRINGSRAW.ordinal();
		
		_pane.add( _numOfStringsCmb, comboBoxConstraints );
        
		GridBagConstraints alterNoteConstraints = new GridBagConstraints();
		alterNoteConstraints.gridx = 6;
		alterNoteConstraints.gridy = UIRaw.ROOTRAW.ordinal();
		alterNoteConstraints.insets = new Insets( 0, 4, 0, 0 );

		_pane.add( _rootAlt1, alterNoteConstraints );
		
		alterNoteConstraints.gridx++;
		alterNoteConstraints.insets = new Insets( 0, 0, 0, 4 );
		
        _pane.add( _rootAlt2, alterNoteConstraints );
        
        alterNoteConstraints.gridx++;
        alterNoteConstraints.insets = new Insets( 0, 4, 0, 4 );
        
        _pane.add( _rootCmb, alterNoteConstraints );
        
		_pane.add( _lngCmb, new GridBagConstraints( 6 //gridx
				, UIRaw.SAVEBTNRAW.ordinal() //gridy
				, 2 //gridwidth
				, 1 //gridheight
				, 0 //weightx
				, 0 //weighty
				, GridBagConstraints.WEST //anchor
				, GridBagConstraints.CENTER //fill
				, new Insets( 4, 4, 4, 4 ) //insets
				, 0 //ipadx
				, 0 //ipady 
				) );
		
		createFretboard();

		_frame.pack();
        _frame.setResizable( false );
		_frame.setLocationRelativeTo(null);
		_frame.setVisible(true);
	}
	
	int _nNumOfStrings = 6;
	int _nNumOfFrets = 5;
	
	JButton _saveBtn;
	
	JLabel _numOfFretsLbl;
	JLabel _firstFretLbl;
	JLabel _numOfStringsLbl;
	JLabel _guitarTuningLbl;
	
	JCheckBox _rootChk;
	
	JComboBox< Integer > _numOfStringsCmb;
	JComboBox< Integer > _firstFretCmb;
	JComboBox< Integer > _numOfFretsCmb;	
	JComboBox< String > _lngCmb;
	JComboBox< String > _rootCmb;
	
	JToggleButton _rootAlt1;
	JToggleButton _rootAlt2;
	
	List< JComboBox< String > > _strings = new ArrayList<>();
	List< JSeparator > _separators = new ArrayList<>();
	List< JToggleButton > _rootNotes = new ArrayList<>();
	
	Map< Integer, List< JToggleButton > > _alters = new HashMap<>();
	Map< Integer, List< JToggleButton > > _frets = new HashMap<>();
	
	private final int _nOctaveFrets = 12;
	
	private String _overWriteMsg;
	private String _overWriteTitle;
	
	private Container _pane;
	private JFrame _frame;
	
	private final String[] _stringTuneItems = { "A", "B", "C", "D", "E", "F", "G" };
	private final List< String > _notesLst = Arrays.asList( "A", "#", "B", "C", "#", "D", "#", "E", "F", "#", "G" );
	
	private void createFretboard(){
        int[] tuning = { 5, 1, 4, 0, 3, 6, 1, 4 };
        int nAdjustment = 8 - _nNumOfStrings;
        if( nAdjustment > 2 )
        	nAdjustment = 2;
        
        class BuddyController implements ActionListener{
			BuddyController( JToggleButton buddy ){
				_buddy = buddy;
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				if( _buddy.isSelected() ){
					_buddy.setSelected( false );
				}
			}
			private JToggleButton _buddy;
        }
        
        GridBagConstraints alterNoteConstraints = new GridBagConstraints();
        Insets firstAlterInsets = new Insets( 0, 4, 0, 0 );
        Insets secondAlterInsets = new Insets( 0, 0, 0, 4 );
        
        GridBagConstraints stringTuneConstraints = new GridBagConstraints();
        stringTuneConstraints.gridy = UIRaw.NOTERAW.ordinal();
        stringTuneConstraints.insets = new Insets( 0, 0, 8, 0 );
        stringTuneConstraints.anchor = GridBagConstraints.CENTER;
        stringTuneConstraints.gridwidth = 2;
        
        for( int n = 0; n < _nNumOfStrings; n++ ){
	        JToggleButton alt1 = new JToggleButton( "b" );
	        JToggleButton alt2 = new JToggleButton( "#" );
	        List< JToggleButton > l = new ArrayList<>();
	        l.add( alt1 );
	        l.add( alt2 );
	        alt1.addActionListener( new BuddyController( alt2 ) );
	        alt2.addActionListener( new BuddyController( alt1 ) );

	        _alters.put( n, l );
	        alterNoteConstraints.insets = firstAlterInsets;
	        alterNoteConstraints.gridx = n * 2;
	        alterNoteConstraints.gridy = UIRaw.ALTNOTERAW.ordinal();
	        _pane.add( alt1, alterNoteConstraints );
	        alterNoteConstraints.gridx++;
	        alterNoteConstraints.insets = secondAlterInsets;
	        _pane.add( alt2, alterNoteConstraints );
	        JComboBox< String > cmb = new JComboBox< String > ( _stringTuneItems );
	        cmb.setSelectedIndex( tuning[ n + nAdjustment ] );
	        _strings.add( cmb );
	        
	        stringTuneConstraints.gridx = n * 2;
	        
	        _pane.add( cmb, stringTuneConstraints );
        }
        
        GridBagConstraints sc = new GridBagConstraints();
        sc.insets = new Insets( 4, 10, 4, 10);
        sc.weightx = 1.0;
        sc.gridx = 0;
		sc.fill = GridBagConstraints.HORIZONTAL;
        sc.gridwidth = GridBagConstraints.REMAINDER;
        
        GridBagConstraints fc = new GridBagConstraints();
        fc.gridwidth = 2;
        fc.anchor = GridBagConstraints.CENTER;
        fc.insets = new Insets( 0, 0, 0, 0 );
        
        for( int nString = 0; nString < _nNumOfStrings; nString++ ){
        	List< JToggleButton > fretBtns = new ArrayList<>();
        	for( int nFret = 0; nFret < _nNumOfFrets; nFret++ ){
		        JToggleButton btn = new JToggleButton();
		        btn.setMargin(new Insets( 0, 0, 0, 0 ));
		        btn.setPreferredSize( new Dimension( 25,25 ) );
		        
		        fretBtns.add(btn);
		        
		        fc.gridx = nString * 2;
		        fc.gridy = UIRaw.FRETRAW.ordinal() + nFret * 2;
		        fc.insets.bottom = 10 * ( ( nFret + 1 )/_nNumOfFrets );
		        
		        _pane.add( btn, fc );
		        
		        if( nString == 0 &&  nFret < _nNumOfFrets - 1 ){
			        JSeparator s = new JSeparator( JSeparator.HORIZONTAL );
			        
			        _separators.add( s );
			        
			        sc.gridy = UIRaw.FRETRAW.ordinal() + nFret * 2 + 1;
			        
			        _pane.add( s, sc );   		        	
		        }
        	}
        	_frets.put( nString, fretBtns );
        }
	}
	private void reCreateFretboard(){
		for( JComboBox< String > cmb: _strings ){
			_pane.remove( cmb );
		}
		for( List< JToggleButton > l : _alters.values() ){
			for( JToggleButton btn : l ){
				_pane.remove( btn );
			}
		}

		for( List< JToggleButton > l : _frets.values() ){
			for( JToggleButton btn : l ){
				_pane.remove( btn );
			}
		}
		for( JSeparator s: _separators ){
			_pane.remove( s );
		}
		_separators.clear();
		_strings.clear();
		_alters.clear();
		createFretboard();
		_frame.pack();
		_frame.validate();
	}
	
	private void clearRoot(){
		for( JToggleButton btn : _rootNotes ){
			btn.setText( "" );
		}
		_rootNotes.clear();		
	}
	private void setRoot(){
		String noteName = _rootCmb.getSelectedItem().toString();
		int nNoteIndex = _notesLst.indexOf( noteName );
		if( _rootAlt1.isSelected() ){	
			if( nNoteIndex == 0 ){
				nNoteIndex = _nOctaveFrets - 1;
			} else {
				nNoteIndex -=1;
			}
		} else if( _rootAlt2.isSelected() ){
			nNoteIndex += 1;
			if( nNoteIndex > _nOctaveFrets ){
				nNoteIndex = 0;
			}
		}
		
		clearRoot();
		
		int nFirstFret = (int) _firstFretCmb.getSelectedItem();
		int nStringIndex = 0;
		for( JComboBox< String > cmb : _strings ){
			int nAdjustment = 0;
			String stringNote = cmb.getSelectedItem().toString();
			int nStringNoteIndex = _notesLst.indexOf( stringNote );
			if( nNoteIndex < nStringNoteIndex ){
				nAdjustment = _nOctaveFrets + nNoteIndex - nStringNoteIndex;
			} else if ( nNoteIndex > nStringNoteIndex ) {
				nAdjustment = nNoteIndex - nStringNoteIndex;
			}
			
			if( nAdjustment >= nFirstFret && (nAdjustment - nFirstFret) < _nNumOfFrets ){
				List<JToggleButton> frets = _frets.get( nStringIndex );
				JToggleButton btn = frets.get( nAdjustment - nFirstFret );
				btn.setText( "R" );
				_rootNotes.add( btn );
			}
			
			nStringIndex ++;
		}
	}
	private class NumOfStringsAL implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			_nNumOfStrings = (int) _numOfStringsCmb.getSelectedItem();
			reCreateFretboard();
			if( _rootChk.isSelected() ){
				setRoot();
			}
		}
		
	}
	private class ChangeLng implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String lng = ( String) _lngCmb.getSelectedItem();
			Locale currentLocale = new Locale( lng );
			ResourceBundle messages = ResourceBundle.getBundle( "com.github.gpatterns.res.GPatterns", currentLocale);
			_overWriteMsg = messages.getString( "overWriteMsg" );
			_overWriteTitle = messages.getString( "overWriteTitle" );
			_frame.setTitle( messages.getString( "title" ) );
			_saveBtn.setText( messages.getString( "saveBtn" ) );
			_firstFretLbl.setText( messages.getString( "firstFret" ) );
			_guitarTuningLbl.setText( messages.getString( "guitarTuning" ) );
			_numOfStringsLbl.setText( messages.getString( "numberOfStrings" ) );
			_numOfFretsLbl.setText( messages.getString( "numberOfFrets" ) );
			_rootChk.setText( messages.getString( "rootNote" ) );
		}
		
	}
	
	private class NumOfFretsAL implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			_nNumOfFrets = (int) _numOfFretsCmb.getSelectedItem();
			reCreateFretboard();
			if( _rootChk.isSelected() ){
				setRoot();
			}
		}
		
	}
	private class ImageSaver implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fDlg = new JFileChooser(){
				private static final long serialVersionUID = 2730286832280583972L;
			    @Override
			    public void approveSelection(){
			        File f = getSelectedFile();
			        if(f.exists() && getDialogType() == SAVE_DIALOG){
			            int result = JOptionPane.showConfirmDialog(this, _overWriteMsg, _overWriteTitle, JOptionPane.YES_NO_CANCEL_OPTION);
			            switch(result){
			                case JOptionPane.YES_OPTION:
			                    super.approveSelection();
			                    return;
			                case JOptionPane.NO_OPTION:
			                    return;
			                case JOptionPane.CLOSED_OPTION:
			                    return;
			                case JOptionPane.CANCEL_OPTION:
			                    cancelSelection();
			                    return;
			            }
			        }
			        super.approveSelection();
			    }        
			};
			
			fDlg.setAcceptAllFileFilterUsed( false );
			fDlg.setSelectedFile( new File("diagram.png") );
				
			fDlg.setFileFilter( new FileFilter() {

				@Override
				public boolean accept(File file) {
					if( file.isDirectory() )
						return true;
					if( file.getName().endsWith( ".png" ) || file.getName().endsWith( ".PNG" ) )
						return true;
					return false;
				}

				@Override
				public String getDescription() {
					return "PNG files";
				}
				
			} );
				
				
			if( fDlg.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ){
			    File file = fDlg.getSelectedFile();
			    String fileName = file.getName();
			    if( !(fileName.endsWith( ".png" ) || fileName.endsWith( ".PNG" ) ) )
			    	file = new File( fileName + ".png" );
			    
			    FretboardImage image = new FretboardImage();
				try {				
					image.createFretboardImage( _nNumOfStrings, _nNumOfFrets );
				
					image.addFretNum( _firstFretCmb.getSelectedItem().toString() );
				
					int nCount = 0;
					List< String > stringNames = new ArrayList<>();
				    for( JComboBox<String> s : _strings){
				    	String alt = "";
				    	for( JToggleButton altBtn: _alters.get( nCount ) ){
				    		if( altBtn.isSelected() ){
				    			alt = altBtn.getText();
				    			break;
				    		}
				    	}
				    	stringNames.add(s.getSelectedItem().toString()+alt);
				    	nCount ++;
				    }
				    
				    image.addStringNames( stringNames );
				    
				    int nString = 0;
					for( List< JToggleButton > l : _frets.values() ){
						int nFret = 0;
						for( JToggleButton btn : l ){
							if( btn.isSelected() ){
								boolean bIsRoot = _rootChk.isSelected() && btn.getText().equals( "R" );
								image.addNote( nString, nFret, bIsRoot );
							}
							nFret++;
						}
						nString++;
					}
			    
					image.saveImage( file );
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}		
		}

	}
}
