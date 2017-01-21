/*
 * Erland Arctaedius och Lovisa Colérus
 * 
 *  Hösten 2011
 *  
 *  Copyright Erland Arctaedius och Lovisa Colérus 2011
 */

package se.lina.projekt;

/**
 * Hittar perioden hos ett oktalspels nimvärden
 * @author Erland Arctaedius och Lovisa Colérus
 *
 */
public class SequenceFinder {
	
	/**
	 * Kontrollerar om de två arrayerna a och s har identiska element i samma ordning
	 * @param a En array
	 * @param s En array
	 * @return true om de är lika, annars false
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
	 * Hittar perioden hos ett oktalspels nimvärden genom sköldpadda-hare metoden
	 * (heter Cycle detection på engelska, se länk http://en.wikipedia.org/wiki/Cycle_detection )
	 * Skriver ut (en multipel) av perioden och antalet steg det tog för att hitta den
	 * 
	 * @param g Ett spel med oktalkoden som ska undersökas, storleken spelar ingen roll
	 * 
	 */
	public static void findSequence(Game g){
		/*
		 * Längden på oktalkoden utan punkt
		 */
		int 		depth 		= g.getOctalcode().length() - 1;
		
		/*
		 * Sköldpaddans position längst fram
		 */
		int 		skalPointer	= depth - 1;
		
		/*
		 * Harens position längst fram
		 */
		int			harePointer = depth;
		
		boolean 	found		= false;
		int 		turnCount	= 0;
		
		/*
		 * Håller koll på vilka värden haren respektive sköldpaddan står på just nu
		 */
		int[] 		hare		= new int[depth];
		int[] 		skal		= new int[depth];
		
		/*
		 * Gör iordning startpositionerna för haren, respektive sköldpaddan
		 * Haren startar ett steg framför sköldpaddan
		 */
		for (int i = 0; i < depth; i++){
			hare[i] = new Game(i+1, g).getNimValue();
			skal[i] = new Game(i, g).getNimValue();
		}
		
		
		/*
		 * Går igenom ett steg i taget tills vi hittar (en multipel av) perioden eller avslutar när vi gått alltför långt
		 * (troligen dör programmet av minnesbrist innan dess)
		 */
		while (!found && turnCount < Integer.MAX_VALUE){
			
			/*
			 * Om haren och sköldpaddan står på identiska värden i samma ordning är vi klara
			 */
			if (arrayEquals(hare, skal)){
				found = true;
				continue;
			}
			
			/*
			 * Sköldpaddan flyttar ett steg framåt, haren två
			 */
			skalPointer++;
			harePointer += 2;
			
			/*
			 * Lägger in de nya värdena i skal-arrayen när sköldpaddan har flyttat på sig
			 */
			for (int i = 0; i < depth; i++){
				skal[depth-1-i] = new Game(skalPointer - i, g).getNimValue();
			}
			
			/*
			 * Lägger in de nya värdena i hare-arrayen när haren har flyttat på sig
			 */
			for (int i = 0; i < depth; i++){
				hare[depth-1-i] = new Game(harePointer - i, g).getNimValue();
			}
			
			turnCount++;
		}
		
		/*
		 * Skillnaden mellan haren och sköldpaddan är (en multipel av) perioden
		 */
		int period = harePointer - skalPointer;
		
		/*
		 * Skriver ut (en multipel av) perioden
		 * Vi börjar med att skriva ut sköldpaddans första värde
		 */
		for (int i=0; i < period; i++){
			System.out.println(new Game(skalPointer-depth+2+i, g));
		}
		
		/*
		 * Skriver ut hur många steg vi behövde gå för att hitta (en multipel av) perioden
		 */
		System.out.println(turnCount);
	}
}