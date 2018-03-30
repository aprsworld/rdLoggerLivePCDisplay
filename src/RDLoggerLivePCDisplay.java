import javax.swing.JOptionPane;

/* master server */
class RDLoggerLivePCDisplay extends Thread {
	protected String inifile;
	protected String stationID;
	protected boolean hideGUI;
	protected IniFile ini;
	WindSmallDisplay disp;

	RDLoggerLivePCDisplay (String inifilename) {
		inifile = inifilename;
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		// Open configuration file
		ini = new IniFile(inifile);
		hideGUI=ini.isTrue("GENERAL","hideGUI");
		
		
		/* GUI screens */
		disp=null;

		System.err.println("# Establishing data link");
		String serialPort=ini.getValueSafe("SERIAL", "port","COM1");
		int serialSpeed = Integer.parseInt(ini.getValueSafe("SERIAL", "speed", "57600"));
		LinkSerial link = new LinkSerial(serialPort,serialSpeed);
		
		if ( ! hideGUI ) {
			/* Start the GUI */
			disp = new WindSmallDisplay(ini);
		}

		// Establish the Link
		if (!link.Connect()) {
			String message = "Invalid link type.";

			if ( "serial".equals(ini.getValue("LINK","type")) ) {
				message = "Failed to establish " + ini.getValue("LINK","type").toUpperCase() + " link to " + ini.getValue("SERIAL","port");
			}


			System.err.println("ABORT: Error Establishing Link\n" + message);
			if ( ! hideGUI ) {
				JOptionPane.showMessageDialog(disp.f, message, "Link Failure", JOptionPane.ERROR_MESSAGE);

				disp.f.dispose();
				new ConfigSelect().show();
			}

			return;
		}

		if ( disp != null ) {
			disp.setVisible(true);
		}


		int errors=0;
		int buff[] = new int[9];
		while ( null != ( buff=link.getRawPacket('#', buff.length))) {
			RDLoggerLiveRecord rec = new RDLoggerLiveRecord();
			rec.parseRecord(buff);

//			for ( int i=0 ; i<buff.length ; i++ ) {
//				System.out.print(buff[i] + " ");
//			}
			System.out.println();
			
			if (rec.isValid()) {
				if ( ! hideGUI ) {
					disp.updateDisplay(rec);
				}

//				if ( null != log ) {
//					log.log(line,rec.rxDate);
//				}
//				System.err.println("Errors=" + errors);
			} else {
				errors++;
				System.err.println("Invalid packet received from rdLoggerLive. Errors=" + errors);
			}
			
			
		}
	

		// Clean Up
		link.Disconnect();
	}

	public static void main (String args[]) {
		String ini = null;

		// Parse arguments
//		if (args.length == 0) {
//			new ConfigSelect().setVisible(true);
//			return;
//		} else 
		if (args.length == 1) {
			ini = args[0];
		} else {
//			System.err.println("Usage: java RDLoggerLivePCDisplay inifile");
//			System.err.println("Invoke with -Dswing.aatext=true for anti-aliased fonts");
//			System.exit(-1);
			ini = "config_default.ini";
		}

		(new RDLoggerLivePCDisplay(ini)).start();
	}
}
