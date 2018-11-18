package a8;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class IAMain {
	
	public static void main(String[] args) throws IOException {
		//paste your own image url here for to adjust your own
		Picture p = A8Helper.readFromURL("http://www.goldenrural.org/wp-content/uploads/2016/04/internet.jpg");
		ImageAdjusterView simple_widget = new ImageAdjusterView(p);
		
		JFrame main_frame = new JFrame();
		main_frame.setTitle("Assignment 8 Image Adjuster");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		top_panel.add(simple_widget, BorderLayout.CENTER);
		main_frame.setContentPane(top_panel);

		main_frame.pack();
		main_frame.setVisible(true);
	}

}
