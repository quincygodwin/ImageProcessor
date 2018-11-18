package a8;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PixelInspectorView extends JPanel implements MouseListener {
	
private PictureView picture_view;
private Picture pic;
private JLabel x;
private JLabel y;
private JLabel red;
private JLabel green;
private JLabel blue;
private JLabel brightness;
	
	public PixelInspectorView(Picture picture, String title) {
		this.pic = picture;
		setLayout(new BorderLayout());
		
		x = new JLabel("X: ");
		y = new JLabel("Y: ");
		red = new JLabel("Red: ");
		green = new JLabel("Green: ");
		blue = new JLabel("Blue: ");
		brightness = new JLabel("Brightness: ");
		
		picture_view = new PictureView(picture.createObservable());
		picture_view.addMouseListener(this);
		add(picture_view, BorderLayout.CENTER);
		
		JLabel title_label = new JLabel(title);
		add(title_label, BorderLayout.SOUTH);
		
		JPanel info = new JPanel();
		info.setLayout(new GridLayout(0,1));
		
		info.add(x);
		info.add(y);
		
		info.add(red);
		info.add(green);
		info.add(blue);
		info.add(brightness);
		
		add(info, BorderLayout.WEST);
		
		
		
	}

	//action listener for mouse event
	@Override
	public void mouseClicked(MouseEvent e) {
		
		x.setText("X: " + e.getX());
		y.setText("Y: " + e.getY());
		
		red.setText("Red: " + pic.getPixel(e.getX(), e.getY()).getRed());
		green.setText("Green: " + pic.getPixel(e.getX(), e.getY()).getGreen());
		blue.setText("Blue: "  + pic.getPixel(e.getX(), e.getY()).getBlue());
		brightness.setText("Brightness: " + pic.getPixel(e.getX(), e.getY()).getIntensity());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
