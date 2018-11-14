import java.io.BufferedReader;
import java.util.Formatter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*	Name: Sabrina Cruz
 * 	Instructions: 
 * 	(1) Copy src folder into Eclipse
 * 	(2) Put TestStrings.txt outside of src file
 *  (3) Run! :D
 */
public class Driver 
{
	public static void loadTestStrings() 
	{
		List<HashMap<Character, ArrayList<Integer>>> nfsa = new ArrayList<HashMap<Character, ArrayList<Integer>>>();
		List<ArrayList<Integer>> dfsaStates = new ArrayList<ArrayList<Integer>>();
		List<HashMap<Character, ArrayList<Integer>>> dfsa = new ArrayList<HashMap<Character, ArrayList<Integer>>>();
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		try
		{
			int counter = 1;
			BufferedReader br = new BufferedReader(new FileReader("TestStrings.txt"));
			boolean done = false;
			while(counter < 4)
			{
				int currentState = 0;
				System.out.println("\nPhase " + counter + " - NFSA:");
				String string = br.readLine();
				int numberOfStates = 1;
				ArrayList<Integer> finalStates = new ArrayList<Integer>();
				nfsa.add(currentState, new HashMap<Character, ArrayList<Integer>>());
				while(!string.contains("~"))
				{	
					for(int i = 0; i < string.length(); i++)
					{
						currentState++;
						nfsa.add(currentState, new HashMap<Character, ArrayList<Integer>>());
						char transition = string.charAt(i);
						
						if(i == 0)
						{
							if(!nfsa.get(i).containsKey(transition))
							{
								nfsa.get(i).put(transition, new ArrayList<Integer>());
							}
							
							nfsa.get(i).get(transition).add(currentState);
						}
						
						else
						{
							if(i == string.length() - 1)
							{
								finalStates.add(currentState);
							}
							
							nfsa.get(currentState-1).put(transition, new ArrayList<Integer>());
							nfsa.get(currentState-1).get(transition).add(currentState);
						}	
						
						numberOfStates++;
						//System.out.println("i: " + i + "| transition: " + transition + "| string: " + string + "| currentState: " + currentState + "| string length: " + string.length());
					}
					string = br.readLine();	
				}
				
				
				System.out.println("(1) number of states: " + numberOfStates);
				System.out.print("(2) final states: ");
				
				for(int i = 0; i < finalStates.size(); i++)
				{
					System.out.print(finalStates.get(i) +  " ");
				}
				
				System.out.println("\n(3) alphabet: " + alphabet);
				System.out.println("(4) transitions: ");
				
				
				for(int i = 0; i < nfsa.size(); i++)
				{
					if(!nfsa.get(i).isEmpty())
					{
						Iterator<Character> itr = nfsa.get(i).keySet().iterator();
						while(itr.hasNext())
						{
							char key = itr.next();
							ArrayList<Integer> endStatesList = nfsa.get(i).get(key);
							String endStates = Arrays.toString(endStatesList.toArray()).replace("[", " ").replace("]", " ").replace(",", "");
							System.out.println(i + " " + key + endStates);
						}
					}
				}
				
				int numOfDFSAStates = 0;
				System.out.println(" Phase " + counter + " - The equivalent DFSA");
				dfsaStates.add(new ArrayList<Integer>());
				dfsa.add(new HashMap<Character, ArrayList<Integer>>());
				for(int i = 0; i < dfsaStates.size(); i++)
				{
					if(dfsaStates.size() == 1)
					{
						dfsaStates.get(0).add(0);
						
					} // if you are are the very beginning, add {0} to dfsa[0]
					HashMap<Character, ArrayList<Integer>> tempHash = new HashMap<Character, ArrayList<Integer>>();
					Iterator<Integer> dfsaTransITR = dfsaStates.get(i).iterator(); // iterate through the arrayList of the current index
					while(dfsaTransITR.hasNext())
					{
						int point = dfsaTransITR.next(); //assigns that specific number to pointer. This will grab nfsa info.
						Iterator<Character> transITR = nfsa.get(point).keySet().iterator(); // grabs the keys of the pointer value
						
						while(transITR.hasNext())
						{
							char key = transITR.next();
							ArrayList<Integer> newTransList = nfsa.get(point).get(key);
							Iterator<Integer> newElementITR = newTransList.iterator();
							ArrayList<Integer> temp = new ArrayList<Integer>();
							
							while(newElementITR.hasNext())
							{
								int element = newElementITR.next();
								temp.add(element);
							} // while there are still elements to look at for each key in the nfsa
							
							if(!tempHash.containsKey(key))
							{
								tempHash.put(key, new ArrayList<Integer>());
							}
							tempHash.get(key).addAll(temp);		
						} // while there are still keys to look at for the given pointed value of NFSA
					} //while there is still elements in the arrayList at index i of dfsaStates
					dfsa.add(tempHash);
					Iterator<Character> tempCharITR = tempHash.keySet().iterator();
					while(tempCharITR.hasNext())
					{
						 char tempChar = tempCharITR.next();
						 dfsaStates.add(tempHash.get(tempChar));
					}
					
				}
					
					System.out.println("(1) number of states: " + (dfsaStates.size()));
					ArrayList<Integer> finalStatesDFSA = new ArrayList<Integer>();
					for(int i = 0; i < dfsaStates.size(); i++)
					{
						System.out.println("state " + i + ": " + dfsaStates.get(i).toString().replace("[", "{").replace("]", "}"));
						Iterator<Integer> checkFinalStates = dfsaStates.get(i).iterator();
						while(checkFinalStates.hasNext())
						{
							int checkThis = checkFinalStates.next();
							
							if(finalStates.contains(checkThis))
							{
								finalStatesDFSA.add(i);
							}
						}
					}
					
					System.out.println("(2) final states: " + finalStatesDFSA);
					System.out.print("(3) transitons:");
					for(int i = 0; i < dfsaStates.size(); i++)
					{
						System.out.print("\nstate " + i + ": ");
						Iterator<Character> keyITR = dfsa.get(i+1).keySet().iterator();
						if(!keyITR.hasNext())
						{
							System.out.print("none");
						}
						while(keyITR.hasNext())
						{
							char key = keyITR.next();
							ArrayList<Integer> indexThis = dfsa.get(i+1).get(key);
							int printThis = dfsaStates.indexOf(indexThis);
							System.out.print(key + " " + printThis + " ");
						}
					}
					
					System.out.print("\n(4) test strings: ");
					string = br.readLine();
					
					while(!string.contains("/"))
					{
						System.out.print("\n" + string);
						boolean exit = false;
						int nextState = 0;
						int symbolReader = 0;
						while(!exit)
						{
							
							
							if(symbolReader == string.length())
							{
								exit = true;
								
								if(finalStatesDFSA.contains(nextState))
								{
									System.out.printf(" --------> ACCEPT");
								}
								
								else
								{
									System.out.printf(" --------> REJECT");
								}
							}
							
							else
							{
								char currentSymbol = string.charAt(symbolReader);
								
								if(dfsa.get(nextState+1).containsKey(currentSymbol)) 
								{
									nextState = dfsaStates.indexOf(dfsa.get(nextState+1).get(currentSymbol)); 
									symbolReader++;
								} // if its in the alphabet
								else
								{
									exit = true;
									System.out.printf(" --------> REJECT");
								} // not in alphabet
							}
							
						} //end while(!exit)
						string = br.readLine();
					}
					counter++;
					nfsa.clear();
					dfsa.clear();
					dfsaStates.clear();
			}
					
					
					
	
				} // end LOOP
			
		
		
		catch(IOException e)
		{
			System.out.println("This file could not be found.");
		}
	}

	
	public static void main(String[] args)
	{
		loadTestStrings();
	}
}

