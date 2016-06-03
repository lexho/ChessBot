package test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import org.junit.Test;

import board.Board;
import board.Position;
import board.position.Fen;
import engine.UCIEngine;

public class TestUCI {

	@Test
	public void testCommands() throws IOException {
		Process process = new ProcessBuilder(
				"java", "-classpath", "/home/alex/Code/java/ChessBot/bin", "engine.UCIEngine").start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line;
				
				OutputStream os = process.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				BufferedWriter wr = new BufferedWriter(osw);

				wr.write("isready");
				wr.newLine();
				wr.flush();
				
				line = br.readLine(); // skip header line
				System.out.println(line);
				line = br.readLine(); // answer to the uci command 'isready'...
				System.out.println(line);
				assertEquals("readyok", line); // ...should be 'readyok'
				
				/*System.out.print("Output of running bot is:");
				while ((line = br.readLine()) != null) {
				  System.out.println(line);
				}*/
	}
}
