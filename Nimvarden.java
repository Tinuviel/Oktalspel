/*
 * Erland Arctaedius och Lovisa Col�rus
 * 
 *  H�sten 2011
 *  
 *  Copyright Erland Arctaedius och Lovisa Col�rus 2011
 */

package se.lina.projekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Erland Arctaedius och Lovisa Col�rus
 * 
 * Applikation som l�ser in en oktalkod frln anv�ndaren och skriver ut perioden
 */
public class Nimvarden {
	/*
	 * L�sare f�r att l�sa indata frln anv�ndaren
	 */
	private static BufferedReader reader;
	
	/*
	 * Ifall koden utvecklar intelligens
	 */
	private int AspirationToWorldDomination = 0;
	
	/*
	 * S�tter upp reader f�r l�sning
	 */
	static {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * main-metod
	 * L�ser in oktalkod frln anv�ndaren, finner perioden f�r nimv�rdena hos spelen med den koden och skriver ut perioden
	 * Skriver dessutom som sista siffra ut hur mlnga steg det tog f�r att finna perioden (sl att man kan uppskatta om koden fungerar)
	 * @param toffelErland
	 */
	public static void main(String[] toffelErland){
		/*
		 * L�ser in oktalkoden frln anv�ndaren
		 */
		String gamecode = query("Octal code?:");

		/*
		 * Anv�nd SequenceFinder f�r att hitta perioden
		 */
		SequenceFinder.findSequence(new Game(0, gamecode));
		
		/*
		 * Skriv ut sl att man vet om programmer avslutades korrekt
		 */
		System.out.println("End.");
	}
	
	/**
	 * St�ller en frlga till anv�ndaren
	 * @param s En frlga som skrivs ut till anv�ndaren
	 * @return Anv�ndarens svar
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