/*
 * Copyright 2007 Phidgets Inc.  All rights reserved.
 */

import com.phidgets.*;
import com.phidgets.event.*;

public class PhidgetSound
{
	public static final void main(String args[]) throws Exception {
		InterfaceKitPhidget ik;

		//Example of enabling logging.
		//Phidget.enableLogging(Phidget.PHIDGET_LOG_VERBOSE, null);

		System.out.println(Phidget.getLibraryVersion());

		ik = new InterfaceKitPhidget();
		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("attachment of " + ae);
			}
		});
		ik.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("detachment of " + ae);
			}
		});
		ik.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println(ee);
			}
		});
		ik.addInputChangeListener(new InputChangeListener() {
			public void inputChanged(InputChangeEvent oe) {
				System.out.println(oe);
			}
		});
		ik.addOutputChangeListener(new OutputChangeListener() {
			public void outputChanged(OutputChangeEvent oe) {
				System.out.println(oe);
			}
		});
		ik.addSensorChangeListener(new SensorChangeListener() {
			public void sensorChanged(SensorChangeEvent se) {
				System.out.println(se);
			}
		});

		ik.openAny();
		System.out.println("waiting for InterfaceKit attachment...");
		ik.waitForAttachment();

		System.out.println(ik.getDeviceName());

		Thread.sleep(500);

		if (false) {
			System.out.print("closing...");
			System.out.flush();
			ik.close();
			System.out.println(" ok");
			System.out.print("opening...");
			ik.openAny();
			System.out.println(" ok");
			ik.waitForAttachment();
		}
		if (ik.getInputCount() > 8)
			System.out.println("input(7,8) = (" +
			  ik.getInputState(7) + "," +
			  ik.getInputState(8) + ")");
		if (false) {
			System.out.print("turn on outputs (slowly)...");
			for (int i = 0; i < ik.getOutputCount() ; i++) {
				ik.setOutputState(i, true);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
			System.out.println(" ok");
		}

		if (false)
			for (;;) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		for (int j = 0; j < 1000 ; j++) {
			for (int i = 0; i < ik.getOutputCount(); i++) {
				ik.setOutputState(i, true);
			}
			for (int i = 0; i < ik.getOutputCount(); i++)
				ik.setOutputState(i, false);
		}
		if (false) {
			System.out.println("toggling outputs like crazy");
			boolean o[] = new boolean[ik.getOutputCount()];
			for (int i = 0; i < ik.getOutputCount(); i++)
				o[i] = ik.getOutputState(i);
			for (int i = 0; i < 100000; i++) {
				int n = (int)(Math.random() * ik.getOutputCount());
				ik.setOutputState(n, !o[n]);
				System.out.println("setOutputState " + n +
				  ": " + !o[n]);
				o[n] = !o[n];
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}
		}

		System.out.println("Outputting events for 30 seconds.");
		Thread.sleep(30000);
		System.out.print("closing...");
		ik.close();
		ik = null;
		System.out.println(" ok");
		if (false) {
			System.out.println("wait for finalization...");
			System.gc();
		}
	}
}
