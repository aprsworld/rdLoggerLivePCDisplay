import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class WindSmallDisplay {
	protected AnemometerPanel anemometer;
	protected JFrame f;
	protected String stationID, imageFileName;
	protected String anemometerType[];
	protected boolean hideAnemometer[],hideUser,hideWindDirection;
	protected JLabel statusLabel;
	protected javax.swing.Timer timer;
	protected Date recordDate;
	protected String recDate;
	protected int maxAge; // seconds of history to plot
	protected int serialNo;
	protected JLabel titleLabel;
	protected String sUnits;
	
	public void updateStatus() {
		/* calculate the difference between rec.rxDate and the current date and update status bar */
		Date d=new Date();
		long delta;

		delta=(d.getTime()-recordDate.getTime())/1000;
		
		statusLabel.setText("Last record received at " + recordDate + " (" + delta + " seconds ago)");

	}
	
	public void addHistoricalRecord(RDLoggerLiveRecord rec) {
		anemometer.setWindHistory(rec.rxDate,rec.windSpeed,rec.windGust);
	}

	public void updateDisplay(RDLoggerLiveRecord rec) {
		/* timer for updating the status bar */
		if ( ! timer.isRunning() ) {
			timer.start();
		}

		if ( rec.serialNumber != serialNo ) {
			serialNo=rec.serialNumber;
			titleLabel.setText("Current Conditions at " + serialNo);
		}
		
		recordDate=rec.rxDate;
		updateStatus();
			
		
		anemometer.setWind(rec.windSpeed,rec.windGust);
		
		f.repaint();
	}

	protected void readIni(IniFile ini) {
		maxAge=Integer.parseInt(ini.getValueSafe("GUI","historySeconds","86400"));
		sUnits=ini.getValueSafe("ANEMOMETER","anemo_u","m/s");
	}
	
	public void setVisible(boolean state) {
		f.setVisible(state);
	}

	public WindSmallDisplay(IniFile ini) {
		WindowUtilities.setNativeLookAndFeel();

		readIni(ini);

		f = new JFrame("Current Wind");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		/* fixed size window */
		int width=450;
		int height=500;
		f.setSize(width, height);

		/* Overall BorderLayout */
		Container cont = f.getContentPane();
		cont.setBackground(Color.white);
		cont.setLayout(new BorderLayout());
		
		/* Our body section */
		Container content=new Container();
		
		content.setBackground(Color.white);

		content.setLayout(new GridLayout(0,1)); /* two columns wide ... as long as we need */
		
		
		anemometer=new AnemometerPanel("Anemometer",sUnits,maxAge,width/2-20,height/2-65);
		content.add(anemometer);
		
		/* title text and photo */
		Container titleContainer=new Container();
		titleContainer.setLayout(new FlowLayout());
		
		titleLabel = new JLabel("Current Conditions at " + serialNo);
		titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
		titleLabel.setForeground(Color.blue);
		titleContainer.add(titleLabel);
		
		/* add the title */
		cont.add(titleContainer, BorderLayout.PAGE_START);
		
		/* Add the body */
		cont.add(content, BorderLayout.CENTER);

		/* Add our status bar */
		statusLabel = new JLabel("No data received.",JLabel.CENTER);
		statusLabel.setOpaque(true);
		statusLabel.setBackground(Color.lightGray);
		cont.add(statusLabel, BorderLayout.PAGE_END);

		f.setLocationRelativeTo(null);
		f.addWindowListener(new ExitListener());

		/* add a timer to keep our status bar updated */
		timer = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStatus();
			}
		});

	}
}
