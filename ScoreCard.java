//Caitlyn Brodie and Lucus Burg
import java.util.*;
import javax.swing.*;

public class ScoreCard
{
	private int [] scores;

//Constructor
	public ScoreCard()
	{
		scores = new int[15];

		//Set all scores = to -1
		for (int i = 0; i < 15; i++)
		{
			scores[i] = -1;
		}
	}

	public void setScore(int n, int score)
	{
		scores[n] = score;
	}

//@return score
	public int getScore(int n)
	{
		return scores[n];
	}

//@return integer of upper score
	public int upperScore()
	{
		int upperScore = 0;
		//Loop 0-6 for only upper scores
		for (int i = 0; i < 6; i++)
			if (scores[i] != -1)
				upperScore += scores[i];
		return upperScore;
	}

//@return integer of total upper score
	public int totalUpperScore()
	{
		//Calculate if bonus
		int bonus = 0;
		if (upperScore() >= 63)
			bonus =  35;

		//Return total score with the bonus
		return upperScore() + bonus;
	}

//@return integer if lower score
	public int lowerScore()
	{
		int lowerScore = 0;
		//Loop starting at 6 for only for only lower scores
		for (int i = 6; i < scores.length; i++)
			if (scores[i] != -1)
				lowerScore += scores[i];
		return lowerScore;
 	}

//@return integer of the total lower and upper score
	public int grandTotal()
	{
		return upperScore() + lowerScore();
	}

//@return boolean to check if all the scores are filled
	public boolean filled()
	{
		boolean ans = true;
		for (int i = 0; i < 13; i++)
			if (scores[i] == -1)
				ans = false;
		return ans;
	}

	//this method used to test for ones-sixes
	//@return boolean
	public boolean num(int n, Die [] dice)
	{
		boolean ans = false;
		for (int i = 0; i < 5; i++)
			if (dice[i].value() == n)
				ans = true;

		return ans;
	}

	//this method used to test for 3 of a kind, 4 of a kind and yahtzee.
	public boolean numOfAKind(int n, Die [] dice)
	{
		boolean ans = false;
		int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;

		for (int i = 0; i < 5; i++)
		{
			if (dice[i].value() == 1)
				ones++;
			if (dice[i].value() == 2)
				twos++;
			if (dice[i].value() == 3)
				threes++;
			if (dice[i].value() == 4)
				fours++;
			if (dice[i].value() == 5)
				fives++;
			if (dice[i].value() == 6)
				sixes++;
		}
		ans = (ones >= n) || (twos >= n) || (threes >= n) || (fours >= n)
						 || (fives >= n) || (sixes >= n);

		return ans;
	}

	//calculate the full house
	//@returns boolean
	public boolean fullHouse(Die [] dice)
	{
		boolean ans = false;
		int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;

		for (int i = 0; i < 5; i++)
		{
			if (dice[i].value() == 1)
				ones++;
			if (dice[i].value() == 2)
				twos++;
			if (dice[i].value() == 3)
				threes++;
			if (dice[i].value() == 4)
				fours++;
			if (dice[i].value() == 5)
				fives++;
			if (dice[i].value() == 6)
				sixes++;
		}
		ans = (ones == 3 && (twos == 2 || threes == 2 || fours == 2 ||
				fives == 2 || sixes == 2)) || (twos == 3 && (ones == 2 ||
				 threes == 2 || fours == 2 || fives == 2 || sixes == 2)) ||
				 (threes == 3 && (ones == 2 || twos == 2 || fours == 2 ||
				 fives == 2 || sixes == 2)) || (fours == 3 && (ones == 2 ||
				 threes == 2 || twos == 2 || fives == 2 || sixes == 2))
				 || (fives == 3 && (ones == 2 || threes == 2 || fours == 2 ||
				 twos == 2 || sixes == 2)) || (sixes == 3 && (ones == 2 ||
				 threes == 2 || fours == 2 || fives == 2 || twos == 2));

		return ans;
	}

	//small and large straight
	//@return boolean
	public boolean numStraight(int n, Die [] dice)
	{
		boolean ans = false;
		int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;

		for (int i = 0; i < 5; i++)
		{
			if (dice[i].value() == 1)
				ones++;
			if (dice[i].value() == 2)
				twos++;
			if (dice[i].value() == 3)
				threes++;
			if (dice[i].value() == 4)
				fours++;
			if (dice[i].value() == 5)
				fives++;
			if (dice[i].value() == 6)
				sixes++;
		}
		if (n == 4)
			ans = (ones >= 1 && twos >= 1 && threes >= 1 && fours >= 1) ||
			 		(twos >= 1 && threes >= 1 && fours >= 1 &&	fives >= 1) ||
					  (threes >= 1 && fours >= 1 && fives >= 1 && sixes >= 1);

		if (n == 5)
			ans = (ones >= 1 && twos >= 1 && threes >= 1 && fours >= 1 &&
					fives >= 1) || (twos >= 1 && threes >= 1 && fours >= 1 &&
						fives >= 1 && sixes >= 1);

		return ans;
	}

//Chance
//@return boolean
	public boolean chance()
	{
		return scores[12] == -1;
	}

}
