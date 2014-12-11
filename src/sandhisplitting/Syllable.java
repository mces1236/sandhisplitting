package sandhisplitting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import util.ListUtil;

public class Syllable {

	static HashSet<String> vowlesSet = new HashSet<String>();
	
	static{
		loadVowels();
	}
	
	public static List<String> splitSyllables(String word){
		List<String> syllables = new ArrayList<String>();
		int syllablebeginIndex = 0;
		
		for(int i = 0; i < word.length(); i++){
			String temp = word.charAt(i) + "";
			if(vowlesSet.contains(temp)){
				String syllable = word.substring(syllablebeginIndex, (i+1));
				syllablebeginIndex = i+1;
				syllables.add(syllable);
			}
		}
		
		if(syllablebeginIndex < word.length()){
			String syllable = word.substring(syllablebeginIndex);
			syllables.add(syllable);
		}
		
		return syllables;
	}
	
	public static List<String> splitSyllablesWithNumbers(String word){
		List<String> syllables = new ArrayList<String>();
		int syllablebeginIndex = 0;
		
		int sylNum = 1;
		for(int i = 0; i < word.length(); i++){
			String temp = word.charAt(i) + "";
			if(vowlesSet.contains(temp)){
				String syllable = word.substring(syllablebeginIndex, (i+1));
				syllablebeginIndex = i+1;
				syllable = SyllableTree.appendSyllableWithNumber(sylNum, syllable);
				syllables.add(syllable);
				sylNum++;
			}
		}
		
		if(syllablebeginIndex < word.length()){
			String syllable = word.substring(syllablebeginIndex);
			syllables.add(syllable);
		}
		
		return syllables;
	}
	
	private static void loadVowels() {
		List<String> list = ListUtil.readListFromFile("Train/vowels");
		vowlesSet.addAll(list);
	}

	public static void main(String[] args) {
		System.out.println(splitSyllablesWithNumbers("ramudu"));
	}
}
