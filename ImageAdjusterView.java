package a8;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ImageAdjusterView extends JPanel implements ChangeListener {

	private PictureView picture_view;
	private Picture pic;
	private JSlider blur_slider;
	private JSlider bright_slider;
	private JSlider sat_slider;
	
	public ImageAdjusterView(Picture pic){
		this.pic = pic;
		
		setLayout(new BorderLayout());
		
		picture_view = new PictureView(pic.createObservable());
		add(picture_view, BorderLayout.CENTER);
		
		
		JPanel slider_panel = new JPanel();
		slider_panel.setLayout(new GridLayout(3,2));
		
		blur_slider = new JSlider(0, 5, 0);
		blur_slider.setPaintTicks(true);
		blur_slider.setMajorTickSpacing(1);
		blur_slider.setPaintLabels(true);
		bright_slider = new JSlider(-100, 100, 0);
		bright_slider.setPaintTicks(true);
		bright_slider.setMajorTickSpacing(50);
		bright_slider.setPaintLabels(true);
		sat_slider = new JSlider(-100, 100, 0);
		sat_slider.setPaintTicks(true);
		sat_slider.setMajorTickSpacing(50);
		sat_slider.setPaintLabels(true);
		
		slider_panel.setPreferredSize(new Dimension(pic.getWidth(), 200));

		
		slider_panel.add(new JLabel("Blur: "));
		slider_panel.add(blur_slider);
		slider_panel.add(new JLabel("Brightness: "));
		slider_panel.add(bright_slider);
		slider_panel.add(new JLabel("Saturation: "));
		slider_panel.add(sat_slider);
		
		add(slider_panel, BorderLayout.SOUTH);
		
		blur_slider.addChangeListener(this);
		bright_slider.addChangeListener(this);
		sat_slider.addChangeListener(this);
	}
	
//listener for a change in the sliders values
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();
		if(!slider.getValueIsAdjusting()){
			int blur = blur_slider.getValue();
			Picture newpic = blurChange(blur, pic);
			int bright = bright_slider.getValue();
			newpic = brightChange(bright, newpic);
			int sat = sat_slider.getValue();
			newpic = satChange(sat, newpic);
			
			picture_view.setPicture(newpic.createObservable());
			
		}
		
		
		
	}
	//where the blur math happens
	private Pixel blurHelp(int x, int y, Pixel p, int blur){
		SubPicture sub;
		double redsum = 0;
		double greensum = 0;
		double bluesum = 0;
		try{
		sub = pic.extract(new Coordinate(x-blur,y-blur), new Coordinate(x+blur,y+blur));
		}catch(IllegalArgumentException e){
			sub = pic.extract(new Coordinate(x,y), new Coordinate(x,y));
		}
		for(int i = 0; i<sub.getWidth(); i++){
			for(int j = 0; j<sub.getHeight(); j++){
				redsum = redsum+sub.getPixel(i, j).getRed();
				greensum = greensum+sub.getPixel(i, j).getGreen();
				bluesum = bluesum+sub.getPixel(i, j).getBlue();
			}
		}
		int blurfactor = sub.getWidth()*sub.getHeight();
		return new ColorPixel(redsum/blurfactor, greensum/blurfactor, bluesum/blurfactor);
	}
	//provides new picture for blur changed value
	private Picture blurChange(int blur, Picture p){
		Picture newpic = new PictureImpl(pic.getWidth(), pic.getHeight());
		if(blur==0){
			return p;
		}
		
		for(int i = 0; i<p.getWidth(); i++){
			for(int j = 0; j<p.getHeight(); j++){
				Pixel pix = p.getPixel(i,j);
				newpic.setPixel(new Coordinate(i,j), this.blurHelp(i, j, pix, blur));
				
			}
		}
		return newpic;
		
		
	}
	//math for brightness happens
	private Pixel brightHelp(Pixel p, int bright){
		double brite = (bright*.01);
		double redbrite = p.getRed()+brite;
		double greenbrite = p.getGreen()+brite;
		double bluebrite = p.getBlue()+brite;
		if(redbrite>1.0){
			redbrite = 1.0;
		}
		if(redbrite<0.0){
			redbrite = 0.0;
		}
		if(greenbrite>1.0){
			greenbrite = 1.0;
		}
		if(greenbrite<0.0){
			greenbrite = 0.0;
		}
		if(bluebrite>1.0){
			bluebrite = 1.0;
		}
		if(bluebrite<0.0){
			bluebrite = 0.0;
		}
		
		return new ColorPixel(redbrite, greenbrite, bluebrite);
		
	}
	//provides new picture for bright changed value
	private Picture brightChange(int bright, Picture p){
		
		Picture newpic = new PictureImpl(pic.getWidth(), pic.getHeight());
		
		for(int i = 0; i<p.getWidth(); i++){
			for(int j = 0; j<p.getHeight(); j++){
				Pixel pix = p.getPixel(i,j);
				newpic.setPixel(new Coordinate(i,j), this.brightHelp(pix, bright));
			}
		}	
		return newpic;
	}
	//determines if saturation value is positive or negative and applies formula appropriately
	private Pixel satHelp(Pixel p, int sat){
		if(p.equals(new ColorPixel(0,0,0))){
			return p;
		}
		if(p.equals(new ColorPixel(1,1,1))){
			return p;
		}
		double newred;
		double newgreen;
		double newblue;
		if(sat<0){
			 newred = p.getRed()*(1.0+(sat*0.01)-(p.getIntensity()*sat*0.01));
			 newgreen = p.getGreen()*(1.0+(sat*0.01)-(p.getIntensity()*sat*0.01));
			 newblue = p.getBlue()*(1.0+(sat*.01)-(p.getIntensity()*sat*.01));
			return new ColorPixel(newred, newgreen, newblue);
			
		} else {
			 newred = p.getRed()*((p.getBiggest()+((1.0-p.getBiggest())*(sat*.01)))/p.getBiggest());
			 newgreen = p.getGreen()*((p.getBiggest()+((1.0-p.getBiggest())*(sat*.01)))/p.getBiggest());
			 newblue = p.getBlue()*((p.getBiggest()+((1.0-p.getBiggest())*(sat*.01)))/p.getBiggest());
			if(newred>.99){
				newred = .99;
			}
			if(newgreen>.99){
				newgreen = .99;
			}
			if(newblue>.99){
				newblue = .99;
			}
			 return new ColorPixel(newred, newgreen, newblue);
			
		}
		
	}
	//sets new picture to the changed saturation value
	private Picture satChange(int sat, Picture p){
		Picture newpic = new PictureImpl(pic.getWidth(), pic.getHeight());
		
		for(int i = 0; i<p.getWidth(); i++){
			for(int j = 0; j<p.getHeight(); j++){
				Pixel pix = p.getPixel(i,j);
				newpic.setPixel(new Coordinate(i,j), this.satHelp(pix, sat));
			}
		}	
		return newpic;
	}

}
