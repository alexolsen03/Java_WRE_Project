import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class home {
	
	/*
	 * Global variables - 'dic' to hold a dictionary list
	 * 					  'fin_list' to hold MyWords objects 
	 */
	static List<String> dic = new ArrayList<String>();
	static List<MyWords> fin_list = new ArrayList<MyWords>();
	
	public static void main(String[] args){
		
		/*
		 * Scan the dictionary and add all of the words into the list
		 */
		try{
			Scanner dicScan = new Scanner(new File("DictionaryE.txt"));
			while(dicScan.hasNext()){
				String a = dicScan.next();
				dic.add(a);
			}
		}
		catch(Exception e){
			System.out.println("Error");
		}
		
		/*
		 * The next part will run until a user enters 'QUIT'
		 * after a word is entered, 'processWord()' is called to find matches
		 * 'rateWords()' is then called to rank what is returned from 'processWord()'
		 */
		Scanner scan = new Scanner(System.in);
		boolean status = true;
		while(status){
			System.out.println("Begin entering words.  Type 'QUIT' to finish.");
			String word = scan.next();
			
			if(word.equals("QUIT")){
				status = false;
			}else{
				List<String> list = processWord(word.toLowerCase());
				list = rateWords(word, list);
				
				MyWords mw = new MyWords();
				mw.mainWord = word;
				mw.list = list;
				fin_list.add(mw);
			}
		}
		/*
		 * Print the MyWords objects
		 */
		for(MyWords items : fin_list){
			System.out.print(items.mainWord + ": ");
			for(String w : items.list){
				System.out.print(w + " ");
			}
			System.out.println("");
		}
		
	}
	
	/*
	 * The following process finds words that are similar in size and begin with the same 2 letters.
	 * It also removes any words that may be a different from of the entered word.
	 * 	(example: if 'brother' is the word, it removes 'brothers')
	 * Finally, it returns the list of words that passed the test
	 */
	public static List<String> processWord(String word){
		
		List<String> simWords = new ArrayList<String>();
		
		char firstchar = word.charAt(0);
		char secondchar = word.charAt(1);
		
		for(int y = 0; y< dic.size(); y++){
			String dicWord = dic.get(y);
			if(dicWord.charAt(0) == firstchar && dicWord.charAt(1) == secondchar){
				if(dicWord.length() >= (word.length() - 1) && dicWord.length() <= word.length() + 1){				
					String cat_s = word.concat("s");
					String cat_ed = word.concat("ed");
					String cat_d = word.concat("d");
					String cat_es = word.concat("es");
					String cat_ly = word.concat("ly");
					
					if (dicWord.equals(word) || 
						dicWord.equals(cat_s) || 
						dicWord.equals(cat_ed) || 
						dicWord.equals(cat_d) || 
						dicWord.equals(cat_ly) || 
						dicWord.equals(cat_es)){
							dic.remove(dicWord);
					}else{
						simWords.add(dicWord);
					}
				}
			}
		}
		return simWords;
	}
	
	/*
	 * The following method rates the words within a list based on its similarity to the inputted word
	 */
	public static List<String> rateWords(String word, List<String> list){
		
		int[] scores = new int[list.size()];
		
		for(String item : list){
			int index = list.indexOf(item);
			
			int templen = word.length();
			int f = 0;
			int s = templen;
			do{
				s = templen;
				f = 0;
				
				while(s <= word.length()){
					String substr = word.substring(f, s);
					
					if(word.contains(substr)){
						scores[index] += substr.length();
						
						if(item.indexOf(substr) == word.indexOf(substr)){
							scores[index] += 2;
						}else if(item.indexOf(substr) == (word.indexOf(substr) - 1) || item.indexOf(substr) == (word.indexOf(substr) + 1)){
							scores[index] += 1;
						}
					}			
					f++;
					s++;
				}
				templen -= 1;
				if (templen == 1)
					break;				
			}while(f != s);	
		}
		
		//bubblesort
		boolean changed;
		do{
		  changed = false;
		  for(int i = 0; i < scores.length - 1; i++){
		    if(scores[i] < scores[i + 1]){
		    	
		    	//swap list[i] with list[i + 1];
		    	int temp = scores[i];
		    	String tempWord = list.get(i);
		    	
		    	scores[i] = scores[i+1];
		    	list.set(i, list.get(i + 1));
		    	
		    	scores[i+1] = temp;
		    	list.set(i + 1, tempWord);
		    	
		    	changed = true;
		    }
		  }
		}while(changed);
		
		List<String>finallist = new ArrayList<String>();
		
		while(list.size() < 6){
			list.add("XXXX");
		}
		
		for(int n=0; n<6; n++)
			finallist.add(list.get(n));

		return finallist;
	}

}
/*
 * MyWords objects that have a mainWord attribute and a list attribute
 */
class MyWords{
	String mainWord;
	List<String> list = new ArrayList<String>();
}

