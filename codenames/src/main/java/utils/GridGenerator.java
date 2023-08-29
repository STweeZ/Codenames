package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridGenerator {

	public static String[] generate_grid() throws IOException {
		List<String> lines = new ArrayList<String>();
		try (InputStreamReader streamReader = new InputStreamReader(GridGenerator.class.getResourceAsStream("/mots.txt"), StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(streamReader)) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	        	lines.add(line);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		Collections.shuffle(lines);
		String[] mots = lines.toArray(new String[lines.size()]);
		return mots;
	}
}
