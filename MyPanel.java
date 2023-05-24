//Caitlyn Brodie and Lucus Burg
//Sets width and heights of the panels and buttons
import javax.swing.*;
import java.awt.*;

class MyPanel extends JPanel
{
	private int width, height;

	//Constructor sets width and heigth
	public MyPanel(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	/*second constructor that also sets layout*/
	public MyPanel(int w, int h, int a, int b)
	{
		width = w;
		height = h;
		this.setLayout(new GridLayout(a, b));
	}

	//@return Dimension of width and heigth
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}
}

