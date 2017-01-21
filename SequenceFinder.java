/*
 * Erland Arctaedius och Lovisa Col�rus
 * 
 *  H�sten 2011
 *  
 *  Copyright Erland Arctaedius och Lovisa Col�rus 2011
 */

package se.lina.projekt;

/**
 * Hittar perioden hos ett oktalspels nimv�rden
 * @author Erland Arctaedius och Lovisa Col�rus
 *
 */
public class SequenceFinder {
	
	/**
	 * Kontrollerar om de tv� arrayerna a och s har identiska element i samma ordning
	 * @param a En array
	 * @param s En array
	 * @return true om de �r lika, annars false
	 */
	public static boolean arrayEquals(int[] a, int[] s){
		if (a.length != s.length){
			return false;
		}
		
		for (int i = 0; i < a.length; i++){
			if (a[i] != s[i]){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Hittar perioden hos ett oktalspels nimv�rden genom sk�ldpadda-hare metoden
	 * (heter Cycle detection p� engelska, se l�nk http://en.wikipedia.org/wiki/Cycle_detection )
	 * Skriver ut (en multipel) av perioden och antalet steg det tog f�r att hitta den
	 * 
	 * @param g Ett spel med oktalkoden som ska unders�kas, storleken spelar ingen roll
	 * 
	 */
	public static void findSequence(Game g){
		/*
		 * L�ngden p� oktalkoden utan punkt
		 */
		int 		depth 		= g.getOctalcode().length() - 1;
		
		/*
		 * Sk�ldpaddans position l�ngst fram
		 */
		int 		skalPointer	= depth - 1;
		
		/*
		 * Harens position l�ngst fram
		 */
		int			harePointer = depth;
		
		boolean 	found		= false;
		int 		turnCount	= 0;
		
		/*
		 * H�ller koll p� vilka v�rden haren respektive sk�ldpaddan st�r p� just nu
		 */
		int[] 		hare		= new int[depth];
		int[] 		skal		= new int[depth];
		
		/*
		 * G�r iordning startpositionerna f�r haren, respektive sk�ldpaddan
		 * Haren startar ett steg framf�r sk�ldpaddan
		 */
		for (int i = 0; i < depth; i++){
			hare[i] = new Game(i+1, g).getNimValue();
			skal[i] = new Game(i, g).getNimValue();
		}
		
		
		/*
		 * G�r igenom ett steg i taget tills vi hittar (en multipel av) perioden eller avslutar n�r vi g�tt alltf�r l�ngt
		 * (troligen d�r programmet av minnesbrist innan dess)
		 */
		while (!found && turnCount < Integer.MAX_VALUE){
			
			/*
			 * Om haren och sk�ldpaddan st�r p� identiska v�rden i samma ordning �r vi klara
			 */
			if (arrayEquals(hare, skal)){
				found = true;
				continue;
			}
			
			/*
			 * Sk�ldpaddan flyttar ett steg fram�t, haren tv�
			 */
			skalPointer++;
			harePointer += 2;
			
			/*
			 * L�gger in de nya v�rdena i skal-arrayen n�r sk�ldpaddan har flyttat p� sig
			 */
			for (int i = 0; i < depth; i++){
				skal[depth-1-i] = new Game(skalPointer - i, g).getNimValue();
			}
			
			/*
			 * L�gger in de nya v�rdena i hare-arrayen n�r haren har flyttat p� sig
			 */
			for (int i = 0; i < depth; i++){
				hare[depth-1-i] = new Game(harePointer - i, g).getNimValue();
			}
			
			turnCount++;
		}
		
		/*
		 * Skillnaden mellan haren och sk�ldpaddan �r (en multipel av) perioden
		 */
		int period = harePointer - skalPointer;
		
		/*
		 * Skriver ut (en multipel av) perioden
		 * Vi b�rjar med att skriva ut sk�ldpaddans f�rsta v�rde
		 */
		for (int i=0; i < period; i++){
			System.out.println(new Game(skalPointer-depth+2+i, g));
		}
		
		/*
		 * Skriver ut hur m�nga steg vi beh�vde g� f�r att hitta (en multipel av) perioden
		 */
		System.out.println(turnCount);
	}
}