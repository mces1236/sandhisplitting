package sandhisplitting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.ListUtil;
import util.Pair;

public class SyllableTree {
	static Hashtable<String, List<String>> topDownTable = new Hashtable<String, List<String>>();
	static Hashtable<String, List<String>> bottomUpTable = new Hashtable<String, List<String>>();
	
	static Hashtable<String, Double> topDownSyllableScoresTable = new Hashtable<String, Double>();
	static Hashtable<String, Double> bottomUpSyllableScoresTable = new Hashtable<String, Double>();
	
	static Hashtable<String, List<String>> syllableSandhiTransformation = new Hashtable<String, List<String>>();
	
	public static String sandhiWordsFileName;
	public static boolean verify = false;
	
	static{
//		load(sandhiWordsFileName);
	}
	
	public static void main(String[] args) {
		
		if(args.length != 2){
			System.out.println("specify train and test data [1] train file\t[2] test file");
			System.exit(0);
		}
		
		sandhiWordsFileName = args[0];
		String compoundWordsFile = args[1];
		
		load(sandhiWordsFileName);
		
		SyllableTree syllableTree = new SyllableTree();
		List<String> list = ListUtil.readListFromFile(compoundWordsFile);
		syllableTree.searchTree(list);
	}

	public void constructTree(List<String> words){
		for(String word : words){
			constructTree(word);
		}
	}
	
	public void constructTree(String word){
		List<String> syllables = Syllable.splitSyllables(word);
		constructTopDownTree(syllables, word);
		constructBottomUpTree(syllables, word);
	}

	public void constructTopDownTree(List<String> syllables, String word) {
		appendNumbers(syllables, topDownTable, word);
	}
	
	public void constructBottomUpTree(List<String> syllables, String word) {
		appendNumbersinReverse(syllables, bottomUpTable, word);
	}

	private void appendNumbers(List<String> syllables, Hashtable<String, List<String>> table, String word) {
		int num = 1;
		for(String syllable : syllables){
			String tempSyl = appendSyllableWithNumber(num, syllable);
			addKeyValueToTable(tempSyl, word, table);
			num++;
		}
	}
	
	private void appendNumbersinReverse(List<String> syllables, Hashtable<String, List<String>> table, String word) {
		int num = syllables.size();
		for(String syllable : syllables){
			String tempSyl = appendSyllableWithNumber(num, syllable);
			addKeyValueToTable(tempSyl, word, table);
			num--;
		}
	}
	
	private void addKeyValueToTable(String key, String value,
			Hashtable<String, List<String>> table) {
		if(table.containsKey(key)){
			List<String> list = table.get(key);
			HashSet<String> uniqueSet = new HashSet<>(list);
			if(!uniqueSet.contains(key)){
				list.add(value);
			}
		}
		else{
			List<String> list = new ArrayList<String>();
			list.add(value);
			table.put(key, list);
		}
	}

	private List<String> appendNumbers(List<String> syllables) {
		List<String> tempList = new ArrayList<String>();
		
		int num = 1;
		for(String syllable : syllables){
			String tempSyl = appendSyllableWithNumber(num, syllable);
			tempList.add(tempSyl);
			num++;
		}
		
		return tempList;
	}
	
	private List<String> appendNumbersinReverse(List<String> syllables) {
		List<String> tempList = new ArrayList<String>();
		
		int num = syllables.size();
		for(String syllable : syllables){
			String tempSyl = appendSyllableWithNumber(num, syllable);
			tempList.add(tempSyl);
			num--;
		}
		
		return tempList;
	}

	public static String appendSyllableWithNumber(int num, String syllable){
		return (num + "-" + syllable);
	}
	
	public void printTable(Hashtable<String, List<String>> table){
		for(Map.Entry<String, List<String>> entry : table.entrySet()){
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}
	
	private List<String> searchBottomUpTree(String compoundWord) {
		List<String> syllables = Syllable.splitSyllables(compoundWord);
		syllables = appendNumbersinReverse(syllables);
		syllables = reverseList(syllables);
		List<String> bottomUpWords = getWordsFromSyllabels(syllables, bottomUpTable);
		return bottomUpWords;
	}

	private List<String> reverseList(List<String> syllables) {
		List<String> reverseList = new ArrayList<String>();
		int length = syllables.size();
		String[] reverseArrays = new String[length];
		length--;
		for(String str : syllables){
			reverseArrays[length] = str;
			length--;
		}
		
		for(String str : reverseArrays){
			reverseList.add(str);
		}
		
		return reverseList;
	}

	private List<String> searchTopDownTree(String compoundWord) {
		List<String> syllables = Syllable.splitSyllables(compoundWord);
		syllables = appendNumbers(syllables);
		List<String> topDownWords = getWordsFromSyllabels(syllables, topDownTable);
		return topDownWords;
	}

	private List<String> getWordsFromSyllabels(List<String> syllables,
			Hashtable<String, List<String>> syllableTable) {
		List<String> words = new ArrayList<String>();
		
//		int syllableCount = 0;
		for(String syllable : syllables){
			if(syllableTable.containsKey(syllable)){
				List<String> toBeAdded = syllableTable.get(syllable);
				if(words.isEmpty()){
					words.addAll(toBeAdded);
				}
				else if(checkifContainsCommonWords(words, toBeAdded)){
					words = getCommonWords(words, toBeAdded);
				}
				else
					break;
			}
			else
				break;
		}
		
//		if(syllableCount == 1){
//			words = getUniqueWords(words);
//			return words;
//		}
		
//		words = getCommonWords(words);
		return words;
	}

	private boolean checkifContainsCommonWords(List<String> words,
			List<String> toBeAdded) {
		HashSet<String> set = new HashSet<String>(words);
		
		for(String str : toBeAdded){
			if(set.contains(str)){
				return true;
			}
		}
		
		return false;
	}

	private List<String> getUniqueWords(List<String> words) {
		List<String> uniqueWords = new ArrayList<String>();
		HashSet<String> uniqueSet = new HashSet<String>();
		
		for(String word : words){
			if(!uniqueSet.contains(word)){
				uniqueWords.add(word);
				uniqueSet.add(word);
			}
		}
		
		return uniqueWords;
	}

	private  List<String> getCommonWords(List<String> words,
			List<String> toBeAdded) {
		List<String> list = new ArrayList<String>();
		HashSet<String> commonWordsSet = new HashSet<String>(words);
		for(String word : toBeAdded){
			if(commonWordsSet.contains(word)){
				list.add(word);
			}
		}
		
		return list;
	}
	
	public void searchTree(List<String> compoundWords){
		try{
		BufferedWriter writer = new BufferedWriter(new FileWriter("Output/sandhioutput"));
		for(String compoundWord : compoundWords){
			List<String> constituentWords = searchTree(compoundWord);
			
			if(!constituentWords.isEmpty()){
//				System.out.println(compoundWord + "\t" + constituentWords);
				writer.write(compoundWord + "=" + constituentWords.get(0)+"+"+constituentWords.get(1) + "\n");
			}
		}
		writer.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	public List<String> searchTree(String compoundWord){
		List<String> topDownWords = searchTopDownTree(compoundWord);
		List<String> bottomUpWords = searchBottomUpTree(compoundWord);
		List<String> constituentWords = getConstituentWords(compoundWord, topDownWords, bottomUpWords);
		return constituentWords;
	}

	private List<String> getConstituentWords(String compoundWord, List<String> topDownWords,
			List<String> bottomUpWords) {
		List<String> constituentWords = new ArrayList<String>();
		if(topDownWords.size() == 1 && bottomUpWords.size() == 1){
			constituentWords.addAll(topDownWords);
			constituentWords.addAll(bottomUpWords);
//			System.out.println("direct words " + compoundWord + "\t" + constituentWords);
		}
		else if(topDownWords.size() >= 1 && bottomUpWords.size() >= 1){
//			System.out.println("couldn't resolve " + compoundWord + " top down " + topDownWords + " bottom up " + bottomUpWords);
			double maxScore =  0.0;
			String topDownMax = "", bottomUpMax = "";
			
			for(String topDown : topDownWords){
				for(String bottomUp : bottomUpWords){
					double score = getProbScore(compoundWord, topDown, bottomUp);
					
					if(score > maxScore){
						maxScore = score;
						topDownMax = topDown;
						bottomUpMax = bottomUp;
					}
				}
			}
			
			constituentWords.add(topDownMax);
			constituentWords.add(bottomUpMax);
			
//			System.out.println("multiple words " + compoundWord + "\t" + constituentWords);
		}else if(topDownWords.isEmpty() && bottomUpWords.isEmpty()){
			
		}
		else if(bottomUpWords.isEmpty() && topDownWords.size() == 1){
			String topDownWord = topDownWords.get(0);
			List<String> compoundSyllables = Syllable.splitSyllables(compoundWord);
			List<String> topDownSyllable = Syllable.splitSyllables(topDownWord);
			Pair<String, String> pair = getUnmatchedSyllable(compoundSyllables, topDownSyllable);
			
			if(pair == null) return constituentWords;
			
//			System.out.println(compoundSyllables + "\t" + topDownSyllable + "\t" + pair);
			if(syllableSandhiTransformation.containsKey(pair.getFirst())){
				List<String> transitions = syllableSandhiTransformation.get(pair.getFirst());
				for(String transition : transitions){
					String[] splits = transition.split("\\|");
					if(splits[1].equals(pair.getSecond())){
						String tempCompoundWord = compoundWord.replace(pair.getFirst(), transition);
						String[] constituents = tempCompoundWord.split("\\|");
						constituentWords.add(constituents[0]);
						constituentWords.add(constituents[1]);
//						System.out.println("unseen word TD " + compoundWord + " " + constituentWords);
						return constituentWords;
					}
				}
			}
		}
		else if(topDownWords.isEmpty() && bottomUpWords.size()  == 1){
			String bottomUpWord = bottomUpWords.get(0);
			List<String> compoundSyllables = Syllable.splitSyllables(compoundWord);
			compoundSyllables = reverseList(compoundSyllables);
			List<String> bottomUpSyllable = Syllable.splitSyllables(bottomUpWord);
			bottomUpSyllable = reverseList(bottomUpSyllable);
			Pair<String, String> pair = getUnmatchedSyllable(compoundSyllables, bottomUpSyllable);
			
			if(pair == null) return constituentWords;
			
			if(syllableSandhiTransformation.containsKey(pair.getFirst())){
				List<String> transitions = syllableSandhiTransformation.get(pair.getFirst());
				for(String transition : transitions){
					String[] splits = transition.split("\\|");
					if(splits[1].equals(pair.getSecond())){
						String tempCompoundWord = compoundWord.replace(pair.getFirst(), transition);
						String[] constituents = tempCompoundWord.split("\\|");
						constituentWords.add(constituents[0]);
						constituentWords.add(constituents[1]);
//						System.out.println("unseen word BU " + compoundWord + " " + constituentWords);
						return constituentWords;
					}
				}
			}
		}
		
		return constituentWords;
	}

	private double getProbScore(String compoundWord, String topDown,
			String bottomUp) {
		double topDownScore = getUnMatchedTopDownSubNodesScore(compoundWord, topDown);
		double bottomUpScore = getUnMatchedBottomUpSubNodesScore(compoundWord, bottomUp);
		return (topDownScore * bottomUpScore);
	}

	private double getUnMatchedBottomUpSubNodesScore(String compoundWord,
			String bottomUp) {
		List<String> compoundWordSyllables = Syllable.splitSyllables(compoundWord);
		List<String> bottomUpSyllables = Syllable.splitSyllables(bottomUp);
		compoundWordSyllables = reverseList(compoundWordSyllables);
		bottomUpSyllables = reverseList(bottomUpSyllables);
		double score = getUnMatchedNodesScore(compoundWordSyllables, bottomUpSyllables, bottomUpSyllableScoresTable);
		return score;
	}

	private double getUnMatchedTopDownSubNodesScore(String compoundWord, String topDown) {
		List<String> compoundWordSyllables = Syllable.splitSyllables(compoundWord);
		List<String> topDownSyllables = Syllable.splitSyllables(topDown);
		double score = getUnMatchedNodesScore(compoundWordSyllables, topDownSyllables, topDownSyllableScoresTable);
		return score;
	}

	private Double getUnMatchedNodesScore(List<String> compoundWordSyllables,
			List<String> constituentSyllables, Hashtable<String,Double> scoresTable) {
		
		if(compoundWordSyllables.size() < constituentSyllables.size()) return 0.0;
		
		String[] compoundSyllablesArray = compoundWordSyllables.toArray(new String[0]);
		String[] constituentSyllablesArray = constituentSyllables.toArray(new String[0]);
//		System.out.println(compoundWordSyllables + "\t" + constituentSyllables);
		double unMatchedNodesScore = 1.0;
		boolean unMatched = false;
		for(int i = 0; i < constituentSyllablesArray.length; i++){
			String compoundSyllable = compoundSyllablesArray[i];
			String constituentSyllable = constituentSyllablesArray[i];
			
			if(!unMatched && !compoundSyllable.equals(constituentSyllable)){
				unMatched = true;
			}
			
			if(unMatched){
				constituentSyllable = appendSyllableWithNumber((i+1), constituentSyllable);
				if(scoresTable.containsKey(constituentSyllable)){
					unMatchedNodesScore *= scoresTable.get(constituentSyllable);
				}
			}
		}
		
		if(unMatchedNodesScore == 1.0){
			return 0.0;
		}
		
		return unMatchedNodesScore;
	}

	public static void load(String fileName){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = "";
			List<Sandhi> sandhis =new ArrayList<Sandhi>();
			
			while((line = reader.readLine()) != null){
				String[] splits = line.split("\\|");
				String sandhiPosition = splits[1].trim();
				String[] splits1 = splits[0].split("=");
				String compoundWord = splits1[0].trim();
				String[] splits2 = splits1[1].split("\\+");
				String word1 = splits2[0].trim();
				String word2 = splits2[1].trim();
				
				Sandhi sandhi = new Sandhi();
				sandhi.compoundWord = compoundWord;
				sandhi.word1 = word1;
				sandhi.word2 = word2;
				sandhi.position = sandhiPosition;
				sandhis.add(sandhi);
			}
			reader.close();
			
			SyllableTree st = new SyllableTree();
			List<String> words = st.getWordsFromSandhis(sandhis);
			st.constructTree(words);
			st.computeProbScores(words);
			st.captureSyllableSandhiTransformation(sandhis);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void captureSyllableSandhiTransformation(List<Sandhi> sandhis) {
		for(Sandhi sandhi : sandhis){
			Pair<String, String> pair = getTransformedSandhi(sandhi.compoundWord, sandhi.word1, sandhi.word2);
			
			if(pair == null) continue;
			
			if(syllableSandhiTransformation.containsKey(pair.getFirst())){
				syllableSandhiTransformation.get(pair.getFirst()).add(pair.getSecond());
			}
			else{
				List<String> transformationList = new ArrayList<String>();
				transformationList.add(pair.getSecond());
				syllableSandhiTransformation.put(pair.getFirst(), transformationList);
			}
		}
		
		removeDuplicateValues(syllableSandhiTransformation);
	}

	private void removeDuplicateValues(Hashtable<String, List<String>> table) {
		for(Entry<String, List<String>> entry : table.entrySet()){
			HashSet<String> set = new HashSet<String>(entry.getValue());
			entry.getValue().clear();
			entry.getValue().addAll(set);
//			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	private Pair<String, String> getUnmatchedSyllableThroughStringMatching(String compoundWord, String word) {
		return new Pair<String, String>(compoundWord, word);
	}

	private Pair<String, String> getTransformedSandhi(String compoundWord,
			String word1, String word2) {
//		System.out.println(compoundWord + " " + word1 + " " + word2);
		List<String> compoundSyllables = Syllable.splitSyllables(compoundWord);
		List<String> word1Syllables = Syllable.splitSyllables(word1);
		List<String> word2Syllables = Syllable.splitSyllables(word2);
		
		Pair<String, String> topDownUnmatchedPair = getUnmatchedSyllable(compoundSyllables, word1Syllables);
		
		if(topDownUnmatchedPair == null) return null;
		
		compoundSyllables = reverseList(compoundSyllables);
		word2Syllables = reverseList(word2Syllables);
		
		Pair<String, String> bottomUpUnmatchedPair = getUnmatchedSyllable(compoundSyllables, word2Syllables);
		
		if(bottomUpUnmatchedPair == null) return null;
		
//		System.out.println(topDownUnmatchedPair + "\t" + bottomUpUnmatchedPair);
		if(topDownUnmatchedPair.getFirst().equals(bottomUpUnmatchedPair.getFirst()))
			return new Pair<String, String>(topDownUnmatchedPair.getFirst(), (topDownUnmatchedPair.getSecond() + "|" + bottomUpUnmatchedPair.getSecond()));
		
		return null;
	}

	private Pair<String, String> getUnmatchedSyllable(List<String> compoundSyllables, List<String> wordSyllables) {
		
		if(compoundSyllables.size() < wordSyllables.size()) return null;
		
		String[] compoundArray = compoundSyllables.toArray(new String[0]);
		String[] wordArray = wordSyllables.toArray(new String[0]);
		
		int i = 0;
		for(i = 0; i < wordArray.length; i++){
			if(!compoundArray[i].equals(wordArray[i]))
				break;
		}
	
		if(i == wordArray.length) i -= 1;
//		System.out.println(compoundSyllables + " " + wordSyllables + " " + i);
		
		String compoundSyllable = compoundArray[i];
		String wordSyllable = wordArray[i];
		Pair<String, String> pair = getUnmatchedSyllableThroughStringMatching(compoundSyllable, wordSyllable);
		return pair;
	}

	private void computeProbScores(List<String> words) {
		double total = 0.0;
		for(String word : words){
			List<String> topDownSyllables = Syllable.splitSyllablesWithNumbers(word);
			total += topDownSyllables.size();
			
			countSyllableScores(topDownSyllables, topDownSyllableScoresTable);
			
			List<String> bottomUpSyllables = Syllable.splitSyllables(word);
			bottomUpSyllables = appendNumbersinReverse(bottomUpSyllables);
			
			countSyllableScores(bottomUpSyllables, bottomUpSyllableScoresTable);
		}
		
		computeProb(total, topDownSyllableScoresTable);
		computeProb(total, bottomUpSyllableScoresTable);
	}

	private void computeProb(double total, Hashtable<String, Double> scoresTable) {
		for(Entry<String, Double> entry : scoresTable.entrySet()){
			entry.setValue((entry.getValue() / total));
		}
	}

	private void countSyllableScores(List<String> topDownSyllables,
			Hashtable<String, Double> scoresTable) {
		for(String syllable : topDownSyllables){
			if(scoresTable.containsKey(syllable)){
				double count = scoresTable.get(syllable);
				scoresTable.put(syllable, (count+1.0));
			}
			else{
				scoresTable.put(syllable, 1.0);
			}
		}
	}

	private List<String> getWordsFromSandhis(List<Sandhi> sandhis) {
		List<String> words = new ArrayList<String>();
		for(Sandhi sandhi : sandhis){
			words.add(sandhi.word1);
			words.add(sandhi.word2);
		}
		words = getUniqueWords(words);
		return words;
	}

}
