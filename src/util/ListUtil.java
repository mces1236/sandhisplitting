package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListUtil {

	public static void sortList(List<Pair<String, Double>> list){
		Collections.sort(list, new Comparator<Pair<String, Double>>() {
			@Override
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				return o2.getSecond().compareTo(o1.getSecond());
			}
		});
	}

	public static List<String> readListFromFile(String fileName){
		List<String> list = new ArrayList<String>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = "";
			while((line = reader.readLine()) != null){
				line = line.trim();
				list.add(line);
			}
			reader.close();
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return list;
	}
	
	public static void print(List<Pair<String, Integer>> list){
		for(Pair<String, Integer> pair : list){
			System.out.println(pair.getFirst() + "\t" + pair.getSecond());
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
