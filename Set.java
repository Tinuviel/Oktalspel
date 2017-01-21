/*
 * Erland Arctaedius och Lovisa Colérus
 * 
 *  Hösten 2011
 *  
 *  Copyright Erland Arctaedius och Lovisa Colérus 2011
 */

package se.lina.projekt;

import java.util.HashMap;


/**
 * @author Erland Arctaedius och Lovisa Colérus
 *
 * Representerar ett spel (dvs. en hög med stenar och reglerna för hur man får ta bort stenar)
 * Har metoder för att beräkna spelets nimvärde, vilka spel man kan gå till från denna position mm.
 */
public class Game {
	/*
	 * gamesMap är en buffer av nimvärden för att undvika att beräkna dem fler gånger då det tar mycket lång tid att beräkna
	 * nimvärden för stora spel om man inte sparar värdena
	 * Den yttre HashMap:en har som nyckel en oktalkoden som sträng (med inledande punkt) och som värde buffern för den oktalkoden
	 * Den inre HashMap:en har som nyckel storleken på högen och som värde nimvärdet för den högen
	 */
	private static HashMap<String, HashMap<Integer, Integer>> gamesMap = new HashMap<String, HashMap<Integer, Integer>>();
	
	/*
	 * Spelets oktalkod, med inledande punkt (t.ex. ".133")
	 */
	private String octalcode;
	
	/*
	 * Spelets oktalkod som en int-array (t.ex. {1,3,3})
	 */
	private int[] allowedMoves;
	
	/*
	 * Antalet stenar i högen
	 */
	private int size;
	
	/**
	 * Skapar ett spel
	 * @param i Antalet stenar i högen
	 * @param s Spelets oktalkod som sträng, med inledande punkt (t.ex. ".133")
	 */
	public Game(int i, String s){
		this.size = i;
		this.octalcode = s;
		this.allowedMoves = codeToArray(s);
	}
	
	/**
	 * Skapar ett spel
	 * @param i Antalet stenar i högen
	 * @param g Det nya spelet får samma oktalkod som g
	 */
	public Game(int i, Game g){
		this.size = i;
		this.octalcode = g.octalcode;
		this.allowedMoves = g.allowedMoves;
	}
	
	/**
	 * Beräknar nimvärdet för spelet
	 * @return Spelets nimvärde
	 */
	public int getNimValue(){
		int nim;
		if(!isInBuffer(size, octalcode)){
			/*
			 * Om nimvärdet inte finns buffrat, beräkna det
			 */
			if(size == 0){
				return 0;
			}
			
			nim = mex(turnGamesToNims(getAllowedGames()));
			
			/*
			 * Om det inte finns en buffer för denna oktalkod så skapa den
			 */
			if(!bufferExists(octalcode)){
				gamesMap.put(octalcode, new HashMap<Integer, Integer>());
			}
			
			/*
			 * Lägg nimvärdet i buffern
			 */
			gamesMap.get(octalcode).put(new Integer(size), new Integer(nim));
			return nim;
		}else{
			/*
			 * Om värdet finns buffrat, hämta och returnera det
			 */
			return gamesMap.get(octalcode).get(new Integer(size)).intValue();
		}
	}
	
	/**
	 * Berättar om det finns en buffer för denna oktalkod
	 * @param code Oktalkoden som sträng med inledande punkt (t.ex. ".133")
	 * @return true om buffern finns, false annars
	 */
	private boolean bufferExists(String code){
		return gamesMap.containsKey(code);
	}
	
	/**
	 * Berättar om nimvärdet för det speciella spelet finns i buffern
	 * @param index Storleken på högen
	 * @param code Oktalkoden som sträng med inledande punkt (t.ex. ".133")
	 * @return true om nimvärdet finns buffrat, false annars
	 */
	private boolean isInBuffer(int index, String code){
		if(!bufferExists(code)){
			return false;
		}
		return gamesMap.get(octalcode).containsKey(new Integer(index));
	}
	
	/**
	 * Ger alla spel som man kan nå med ett drag från detta spel
	 * @return De spel som kan nås genom att göra ett drag i detta spel
	 */
	public Game[] getAllowedGames(){
		/*
		 * Vi vill bara ha med varje möjligt spel en gång även om man kan komma till det via fler olika drag
		 * så vi sparar dem i ett Set (se class:en Set)
		 */
		Set<Game> ret = new Set<Game>();
		
		/*
		 * Gå igenom alla siffror i oktalkoden
		 * i blir siffrans index, dvs. antalet stenar som siffrar styr över
		 */
		for(int i = 1; i < allowedMoves.length; i++){
			switch (allowedMoves[i]){      
				case 0: 
					/*
					 * Om siffran är noll så kan vi inte göra något drag
					 */
					break;
				case 1:
					/*
					 * Om siffran är ett så får vi göra draget om vi då tar hela högen
					 */
					if(size == i){
						ret.add(new Game(0, this));
					}
					break;
				case 2:
					/*
					 * Om siffran är två så får vi göra draget om vi lämnar minst en sten
					 */
					if(size > i){
						ret.add(new Game(size-i, this));
					}
					break;
				case 3:
					/*
					 * Om siffran är tre så får vi göra draget varesej det blir stenar kvar eller ej
					 */
					if(size >= i){
						ret.add(new Game(size-i, this));
					}
					break;
				default:
					/*
					 * Om oktalkoden innehåller 4, 5, 6 eller 7 (vilket koden inte stödjer)
					 */
					throw(new RuntimeException("Default in getAllowedGames."));
			}
					
		}
		
		/*
		 * Omvandla Set:et till en array och skicka tillbaks
		 */
		return ret.toArray(new Game[]{});
	}
	
	/**
	 * Ger oktalkoden som en int-array
	 * @return En int-array där index i motsvarar siffran på plats i+1 efter punkten i oktalkoden
	 */
	public int[] getAllowedMoves(){
		return allowedMoves.clone();
	}
	
	/**
	 * Ger oktalkoden som en sträng med inledande punkt (t.ex. ".133")
	 * @return Oktalkoden som en sträng med inledande punkt (t.ex. ".133")
	 */
	public String getOctalcode(){
		return octalcode;
	}
	
	/**
	 * Ger antalet stenar i högen
	 * @return Antalet stenar i högen
	 */
	public int getSize(){
		return this.size;
	}
	
	/**
	 * Applicerar mex-regeln på den givna arrayen, dvs. ger det minsta icke-negativa heltal som inte finns i arrayen
	 * @param a Arrayen med heltal
	 * @return Det minsta icke-negativa heltalet som inte finns i a
	 */
	public static int mex(int[] a){
		int lowest = 0;
		
		/*
		 * Det största värde som mex(a) kan anta är a.length (om alla värden från 0 till a.length-1 finns i arrayen)
		 * så vi stegar igenom och kollar alla tal upp till detta
		 */
		for(int i = 0;i < a.length + 1; i++){
			if(isInArray(i, a)){
				/*
				 * Om i finns i arrayen, öka lowest med ett
				 */
				lowest++;
			}else{
				/*
				 * Om i inte finns så måste det värde lowest har nu vara mex(a) så returnera lowest
				 */
				return lowest;
			}
		}
		/*
		 * Om vi av någon anledning har gått igenom alla och ändå inte hittat nimvärdet, kasta ett fel
		 */
		throw(new RuntimeException("No mex value found."));
	}
	
	/**
	 * Omvandlar en array med spel till en array med deras nimvärden
	 * @param a Arrayen med spel
	 * @return En array med nimvärdena från spelen i a
	 */
	public static int[] turnGamesToNims(Game[] a){
		int[] gameSize = new int[a.length];
		for(int foo=0; foo<a.length; foo++){
			gameSize[foo] = a[foo].getNimValue(); 
		}
		return gameSize;
	}
	
	/**
	 * Kollar om ett tal finns i en array
	 * @param i Talet vi söker
	 * @param a Arrayen vi söker i
	 * @return true om i finns som värde i a, false annars
	 */
	private static boolean isInArray(int i, int[] a){
		boolean found = false;
		for(int ii = 0; ii < a.length; ii++){
			if(i == a[ii]){
				found = true;
			}
		}
		return found;
	}
	
	/**
	 * Equals-metod från Object
	 */
	public boolean equals(Object o){
		if(!(o instanceof Game)){
			return false;
		}
		Game g = (Game)o;
		
		/*
		 * Två spel är lika om de har samma oktalkod och samma antal stenar
		 */
		if(this.getSize()==g.getSize() && this.getOctalcode().equals(g.getOctalcode())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * toString-metod från Object
	 * Skriver ut spelet på formen "S:stenar, N:nimvärde"
	 */
	public String toString(){
		return "S:"+size + ", N:" + getNimValue();
	}
	
	/**
	 * Omvandlar en sträng-oktalkod med inledande punkt (t.ex. ".133") till en int-array
	 * @param s Oktalkoden som sträng med inledande punkt (t.ex. ".133")
	 * @return En array där index i motsvarar siffran på plats i+1 efter punkten
	 */
	private static int[] codeToArray(String s){
		char[] chars = s.toCharArray();
		int[] ret = new int[chars.length];
		
		for(int i = 1; i < chars.length; i++){
			ret[i] = Integer.parseInt("" + chars[i]);
		}
		ret[0] = -1;
		return ret;
	}
}