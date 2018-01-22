package java_threads;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Main /* i know it is amazing */ implements Runnable {
	
	/* Alexey Gayduk (BS4)
	 * 
	 * Вариант 2
	 * 
	 * Необходимо разработать программу, которая получает на вход список ресурсов, содержащих текст,
	 * и проверяет уникальность каждого слова. Каждый ресурс должен быть обработан в отдельном потоке,
	 * текст не должен содержать инностранных символов, только кириллица, знаки препинания и цифры.
	 * В случае нахождения дубликата, программа должна прекращать выполнение с соответсвующим сообщением.
	 * Все ошибки должны быть корректно обработаны. 
	 */
	
	public static void main(String[] args) {
		
		// get input
		ArrayList<String> input = new ArrayList<String>();
		// TODO: read from file
		
			// TEMP: for testing reasons only
			input.add("(-+:;?!\"') 4 8 15 16 23 42 42");
			input.add("Строка строчка, какая ты короткая, но в целом ничего.");
			input.add("Как же хорошо жить на этой Земле, нескем поделиться такой радостьсю.");
			input.add("Короткая стрижка тебе не лицу, но к лицу улыбка.");
			input.add("Ya tebya ne ochen ponimayu.");
			
		// process input
		threads = prepareThreads(input);
		allThreads(true);
		
		// TODO: output result
		
	}
	
	public static ArrayList<Thread> threads; 
	
	// total counter
	public static HashMap<String, Integer> counter = new HashMap<String, Integer>();
	
	// check pattern
	public static String symbols = "\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60\\x7B-\\x7E";
	public static String cyrillic = "\\u0400-\\u04FF";
	public static String pattern = "^[" + symbols + "\\d\\s" + cyrillic + "]*$";
	
	// processing function
	public static void process(String string) throws Exception {
		
		// check wrong chars (or throw)
		if(!string.matches(pattern)) {
			allThreads(false);
			throw new Exception("Wrong characters: " + string);
		}
		
		// extract words + lowercase
		String[] words = string.replaceAll("["+symbols+"\\d]*", "").toLowerCase().split(" ");
		
		// search and add to counter (or throw)
		List<String> list = Arrays.asList(words);
		Iterator<String> i = list.iterator();
		while(i.hasNext()) {
			synchronized (counter) {
				String word = i.next();
				Integer check = counter.get(word);
				if(check == null) {
					counter.put(word, new Integer(1));
				} else {
					allThreads(false);
					throw new Exception("It's a match: " + word);
				}
			}
		}
		
		// i am done!
		
	}
	
	// tread creator
	public static ArrayList<Thread> prepareThreads(ArrayList<String> strings){
		ArrayList<Thread> threads = new ArrayList<Thread>();
		Iterator<String> i = strings.iterator();
		while(i.hasNext()) {
			threads.add(new Thread(new Main(i.next())));
		}
		return threads;
	}
	
	// thread launcher/stopper
	public static void allThreads(boolean run) {
		Iterator<Thread> i = threads.iterator();
		synchronized(threads) {
			while(i.hasNext()) {
				if(run) i.next().start();
				else i.next().interrupt();
			}
		}
		// TODO: smart launch
	}
	
	// for threads
	public String string = null;
	public void run() { try { process(this.string); } catch (Exception e) { log(e.getMessage()); } }
	public Main(String string) { this.string = string; }

	public static void log(String string) {
		System.out.println(string);
	}
	
}

/* started - 0:01; ended - 2:16 */
