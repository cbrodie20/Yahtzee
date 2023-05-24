//Caitlyn Brodie and Lucus Burg

import java.util.*;

public class Die
{
	private int sides, value;
	private Random rn;
	private boolean tf;

	public Die(int num)
	{
		sides = num;
		rn = new Random();
		value = roll();
		tf = false;
	}

	public int roll()
	{
		value = rn.nextInt(sides) + 1;
		return value;
	}

	public int value()
	{
		return value;
	}

	public boolean tf()
	{
		return tf;
	}

	public void toggletf()
	{
		tf = !tf;
	}
}

