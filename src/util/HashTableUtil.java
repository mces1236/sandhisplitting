package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HashTableUtil {
	
	public static void increment(Map<String, Integer> table, String key, int amount){
		if(table.containsKey(key)){
			int prevCount = table.get(key) + amount;
			table.put(key, prevCount);
		}
		else{
			table.put(key, amount);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static List<Pair<String, Integer>> sort(Map<String, Integer> map){
		List<Pair<String, Integer>> list = new ArrayList<Pair<String,Integer>>();
		for(Map.Entry<String, Integer> entry : map.entrySet()){
			Pair<String, Integer> pair = new Pair<String, Integer>(entry.getKey(), entry.getValue());
			list.add(pair);
		}
		Collections.sort(list, new Comparator<Pair<String, Integer>>() {
			@Override
			public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
				return o2.getSecond().compareTo(o1.getSecond());
			}
		});
		return  list;
	}

	public static void writeTableToFile(String fileName, Hashtable<String,Integer> freqTable, boolean append) {
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));
			for(Map.Entry<String, Integer> entry : freqTable.entrySet()){
				writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			}
			writer.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}

	public static void readTableFromFile(String fileName, Hashtable<String,Integer> freqTable) {
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = "";
			
			while((line = reader.readLine()) != null){
				String[] splits = line.split("\t");
				freqTable.put(splits[0], Integer.parseInt(splits[1]));
			}
			reader.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}

	
}
