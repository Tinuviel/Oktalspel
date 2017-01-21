/*
 * Erland Arctaedius och Lovisa Colérus
 * 
 *  Hösten 2011
 *  
 *  Copyright Erland Arctaedius och Lovisa Colérus 2011
 */

package se.lina.projekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Erland Arctaedius och Lovisa Colérus
 * 
 * Applikation som läser in en oktalkod frln användaren och skriver ut perioden
 */
public class Nimvarden {
	/*
	 * Läsare för att läsa indata frln användaren
	 */
	private static BufferedReader reader;
	
	/*
	 * Ifall koden utvecklar intelligens
	 */
	private int AspirationToWorldDomination = 0;
	
	/*
	 * Sätter upp reader för läsning
	 */
	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * main-metod
	 * Läser in oktalkod frln användaren, finner perioden för nimvärdena hos spelen med den koden och skriver ut perioden
	 * Skriver dessutom som sista siffra ut hur mlnga steg det tog för att finna perioden (sl att man kan uppskatta om koden fungerar)
	 * @param toffelErland
	 */
	public static void main(String[] toffelErland){
		/*
		 * Läser in oktalkoden frln användaren
		 */
		String gamecode = query("Octal code?:");

		/*
		 * Använd SequenceFinder för att hitta perioden
		 */
		SequenceFinder.findSequence(new Game(0, gamecode));
		
		/*
		 * Skriv ut sl att man vet om programmer avslutades korrekt
		 */
		System.out.println("End.");
	}
	
	/**
	 * Ställer en frlga till användaren
	 * @param s En frlga som skrivs ut till användaren
	 * @return Användarens svar
	 */
	private static String query(String s){
		System.out.println(s);
		try{
			return reader.readLine();
		}catch(IOException e){
			throw(new RuntimeException("IOException", e));
		}
	}
}