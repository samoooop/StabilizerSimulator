package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JSlider;

import simulator.StabilizerLogic;

public class StabilizerControl {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(StabilizerLogic logic) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StabilizerControl window = new StabilizerControl();
					window.frame.setVisible(true);
					logic.window = window;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public StabilizerControl() {
		initialize();
	}
	JSlider slider[] = new JSlider[12];
	String word[] = {"pitch","roll","yaw","x","y","z"};
	JLabel debug;
	int max[] = {9000,9000,9000,200,200,200};
	int def[] = {4500,4500,4500,100,130,100};
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 479, 526);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		for(int i=0;i<6;i++){
			JLabel lblNewLabel = new JLabel("Motor" + i);
			frame.getContentPane().add(lblNewLabel);
			
			slider[i] = new JSlider();
			slider[i].setMaximum(9000);
			slider[i].setValue(4500);
			frame.getContentPane().add(slider[i]);
			
		}

		for(int i=0;i<6;i++){
			JLabel lblNewLabel = new JLabel(word[i]);
			frame.getContentPane().add(lblNewLabel);
			
			slider[i+6] = new JSlider();
			slider[i+6].setMaximum(max[i]);
			slider[i+6].setValue(max[i]/2);
			frame.getContentPane().add(slider[i+6]);
			
		}
		debug = new JLabel("DEBUG MESSAGE HERE");
		frame.getContentPane().add(debug);

	}
	
	public synchronized int[] getSliderData() throws InterruptedException{
		int sliderData[] = new int[12];
		for(int i=0;i<12;i++){
			sliderData[i] = slider[i].getValue();
		}
		return sliderData;
	}
	
	public synchronized void setSliderData(int[] sliderData) throws InterruptedException{
		for(int i=0;i<12;i++){
			slider[i].setValue(sliderData[i]); 
		}
	}
	public synchronized void display(String message) throws InterruptedException{
		debug.setText(message);
	}
}
