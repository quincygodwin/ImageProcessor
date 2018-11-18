package a8;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FramePuzzleView extends JPanel implements MouseListener, KeyListener {
	
	private PictureView picture_view;
	private Picture pic;
	private PictureView[][] puzzle;
	private PictureView block;
	private int block_x;
	private int block_y;
	private int width_factor;
	private int height_factor;
	
	
	
	public FramePuzzleView(Picture p){
		
		pic = p;
		puzzle = this.splitter(p);
		
		setLayout(new BorderLayout());
		
		JPanel jig = new JPanel();
		jig.setLayout(new GridLayout(5,5));
		add(jig, BorderLayout.CENTER);
		
		for(int i = 0; i<5; i++){
			for(int j = 0; j<5; j++){
				puzzle[i][j].addMouseListener(this);
				jig.add(puzzle[i][j]);
			}
		}

	}


	//splits picture into 5x5 grid of pictureview objects
	private PictureView[][] splitter(Picture p){
		PictureView [][] puzzle = new PictureView[5][5];
		width_factor = p.getWidth()/5;
		height_factor = p.getHeight()/5;
		
		for(int i = 0; i<5; i++){
			for(int j = 0; j<5; j++){
						Picture piece = new PictureImpl(width_factor,height_factor);
						Picture pieceholder = p.extract(j*width_factor, i*height_factor, width_factor, height_factor);
						for(int q = 0; q<width_factor;q++){
							for(int r = 0; r<height_factor;r++){
								piece.setPixel(q, r, pieceholder.getPixel(q, r));
							}
						}
						if(i*j==16){
							PictureView coolPiece = this.getCoolPiece(width_factor, height_factor);
							coolPiece.addKeyListener(this);
							block = coolPiece;
							puzzle[i][j] = coolPiece;
							block_x = 4;
							block_y = 4;
						} else{
							PictureView viewpiece = new PictureView(piece.createObservable());
							puzzle[i][j] = viewpiece;
						}
			}
		}
		return puzzle;
	}

	//main method for actions, switches objects for key events and slides them over with mouse event
	private void swap(PictureView block, PictureView swapee, boolean mouse){
		//boolean if mouse event in parameters
		
		if(!mouse){
		int blockx = block.getX();
		int blocky = block.getY();
		
		int swap_x = swapee.getX();
		int swap_y = swapee.getY();
		
		block.setLocation(swap_x, swap_y);
		swapee.setLocation(blockx, blocky);	
		} else{
			
			
			
			int blockx = block.getX();
			int blocky = block.getY();
			
			int swap_x = swapee.getX();
			int swap_y = swapee.getY();
			
			if(blockx==swap_x&&blocky>swap_y){
				while(swap_y!=blocky){
					try{
						this.swap(block, puzzle[block_x-1][block_y], false );
						puzzle[block_x][block_y] = puzzle[block_x-1][block_y];
						puzzle[block_x-1][block_y] = block;
						block_x = block_x-1;
						blocky= blocky-height_factor;	
					} catch(RuntimeException t){
					}
				}
			} else if(blockx==swap_x&&blocky<swap_y){
				while(swap_y!=blocky){
					
					try{
						this.swap(block, puzzle[block_x+1][block_y], false );
						puzzle[block_x][block_y] = puzzle[block_x+1][block_y];
						puzzle[block_x+1][block_y] = block;
						block_x = block_x+1;
						blocky=blocky+height_factor;
					} catch(ArrayIndexOutOfBoundsException t){
					}
				}
			} else if(blocky==swap_y&&blockx>swap_x){
				while(swap_x!=blockx){
					try{
						this.swap(block, puzzle[block_x][block_y-1], false );
						puzzle[block_x][block_y] = puzzle[block_x][block_y-1];
						puzzle[block_x][block_y-1] = block;
						block_y = block_y-1;
						blockx=blockx-width_factor;
					} catch(RuntimeException t){
					}
				}
			} else if(blocky==swap_y&&blockx<swap_x){
				while(swap_x!=blockx){
					try{
						this.swap(block, puzzle[block_x][block_y+1], false );
						puzzle[block_x][block_y] = puzzle[block_x][block_y+1];
						puzzle[block_x][block_y+1] = block;
						block_y = block_y+1;
						blockx = blockx+width_factor;
					} catch(RuntimeException t){
					}
				}
			}
			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}


	
//used when mouse is clicked
	@Override
	public void mouseClicked(MouseEvent e) {

		 PictureView click = (PictureView) e.getSource();
		 
		 this.swap(block, click, true);
		 
		 
		 System.out.println();
     }

	//returns a new blank space for the puzzle
	private PictureView getCoolPiece(int width, int height){
		Picture coolPiece = new PictureImpl(width, height);
		for (int i = 0; i<width-1; i++){
			for (int j = 0; j<height-1; j++){
				coolPiece.setPixel(i,  j, new ColorPixel(.99, .00, .99));
			}
		}
		return new PictureView(coolPiece.createObservable());
		
	}
	

//action listener for keys pressed
	@Override
	public void keyPressed(KeyEvent e) {
		int type = e.getKeyCode();
		int down = KeyEvent.VK_RIGHT;
		int up = KeyEvent.VK_LEFT;
		int left = KeyEvent.VK_UP;
		int right = KeyEvent.VK_DOWN;
		if(type==right){
			try{
				this.swap(block, puzzle[block_x+1][block_y], false );
				puzzle[block_x][block_y] = puzzle[block_x+1][block_y];
				puzzle[block_x+1][block_y] = block;
				block_x = block_x+1;
			} catch(ArrayIndexOutOfBoundsException t){
			}
		} else if(type==left){
			try{
				this.swap(block, puzzle[block_x-1][block_y], false );
				puzzle[block_x][block_y] = puzzle[block_x-1][block_y];
				puzzle[block_x-1][block_y] = block;
				block_x = block_x-1;
			} catch(RuntimeException t){
			}
		} else if(type==up){
			try{
				this.swap(block, puzzle[block_x][block_y-1], false );
				puzzle[block_x][block_y] = puzzle[block_x][block_y-1];
				puzzle[block_x][block_y-1] = block;
				block_y = block_y-1;
			} catch(RuntimeException t){
			}
		} else if(type==down){
			try{
				this.swap(block, puzzle[block_x][block_y+1], false );
				puzzle[block_x][block_y] = puzzle[block_x][block_y+1];
				puzzle[block_x][block_y+1] = block;
				block_y = block_y+1;
			} catch(RuntimeException t){
			}
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	

}
