/*
 * Erland Arctaedius och Lovisa Col�rus
 * 
 *  H�sten 2011
 *  
 *  Copyright Erland Arctaedius och Lovisa Col�rus 2011
 */

package se.lina.projekt;

import java.util.HashMap;


/**
 * @author Erland Arctaedius och Lovisa Col�rus
 *
 * Representerar ett spel (dvs. en h�g med stenar och reglerna f�r hur man f�r ta bort stenar)
 * Har metoder f�r att ber�kna spelets nimv�rde, vilka spel man kan g� till fr�n denna position mm.
 */
public class Game {
	/*
	 * gamesMap �r en buffer av nimv�rden f�r att undvika att ber�kna dem fler g�nger d� det tar mycket l�ng tid att ber�kna
	 * nimv�rden f�r stora spel om man inte sparar v�rdena
	 * Den yttre HashMap:en har som nyckel en oktalkoden som str�ng (med inledande punkt) och som v�rde buffern f�r den oktalkoden
	 * Den inre HashMap:en har som nyckel storleken p� h�gen och som v�rde nimv�rdet f�r den h�gen
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
	 * Antalet stenar i h�gen
	 */
	private int size;
	
	/**
	 * Skapar ett spel
	 * @param i Antalet stenar i h�gen
	 * @param s Spelets oktalkod som str�ng, med inledande punkt (t.ex. ".133")
	 */
	public Game(int i, String s){
		this.size = i;
		this.octalcode = s;
		this.allowedMoves = codeToArray(s);
	}
	
	/**
	 * Skapar ett spel
	 * @param i Antalet stenar i h�gen
	 * @param g Det nya spelet f�r samma oktalkod som g
	 */
	public Game(int i, Game g){
		this.size = i;
		this.octalcode = g.octalcode;
		this.allowedMoves = g.allowedMoves;
	}
	
	/**
	 * Ber�knar nimv�rdet f�r spelet
	 * @return Spelets nimv�rde
	 */
	public int getNimValue(){
		int nim;
		if(!isInBuffer(size, octalcode)){
			/*
			 * Om nimv�rdet inte finns buffrat, ber�kna det
			 */
			if(size == 0){
				return 0;
			}
			
			nim = mex(turnGamesToNims(getAllowedGames()));
			
			/*
			 * Om det inte finns en buffer f�r denna oktalkod s� skapa den
			 */
			if(!bufferExists(octalcode)){
				gamesMap.put(octalcode, new HashMap<Integer, Integer>());
			}
			
			/*
			 * L�gg nimv�rdet i buffern
			 */
			gamesMap.get(octalcode).put(new Integer(size), new Integer(nim));
			return nim;
		}else{
			/*
			 * Om v�rdet finns buffrat, h�mta och returnera det
			 */
			return gamesMap.get(octalcode).get(new Integer(size)).intValue();
		}
	}
	
	/**
	 * Ber�ttar om det finns en buffer f�r denna oktalkod
	 * @param code Oktalkoden som str�ng med inledande punkt (t.ex. ".133")
	 * @return true om buffern finns, false annars
	 */
	private boolean bufferExists(String code){
		return gamesMap.containsKey(code);
	}
	
	/**
	 * Ber�ttar om nimv�rdet f�r det speciella spelet finns i buffern
	 * @param index Storleken p� h�gen
	 * @param code Oktalkoden som str�ng med inledande punkt (t.ex. ".133")
	 * @return true om nimv�rdet finns buffrat, false annars
	 */
	private boolean isInBuffer(int index, String code){
		if(!bufferExists(code)){
			return false;
		}
		return gamesMap.get(octalcode).containsKey(new Integer(index));
	}
	
	/**
	 * Ger alla spel som man kan n� med ett drag fr�n detta spel
	 * @return De spel som kan n�s genom att g�ra ett drag i detta spel
	 */
	public Game[] getAllowedGames(){
		/*
		 * Vi vill bara ha med varje m�jligt spel en g�ng �ven om man kan komma till det via fler olika drag
		 * s� vi sparar dem i ett Set (se class:en Set)
		 */
		Set<Game> ret = new Set<Game>();
		
		/*
		 * G� igenom alla siffror i oktalkoden
		 * i blir siffrans index, dvs. antalet stenar som siffrar styr �ver
		 */
		for(int i = 1; i < allowedMoves.length; i++){
			switch (allowedMoves[i]){      
				case 0: 
					/*
					 * Om siffran �r noll s� kan vi inte g�ra n�got drag
					 */
					break;
				case 1:
					/*
					 * Om siffran �r ett s� f�r vi g�ra draget om vi d� tar hela h�gen
					 */
					if(size == i){
						ret.add(new Game(0, this));
					}
					break;
				case 2:
					/*
					 * Om siffran �r tv� s� f�r vi g�ra draget om vi l�mnar minst en sten
					 */
					if(size > i){
						ret.add(new Game(size-i, this));
					}
					break;
				case 3:
					/*
					 * Om siffran �r tre s� f�r vi g�ra draget varesej det blir stenar kvar eller ej
					 */
					if(size >= i){
						ret.add(new Game(size-i, this));
					}
					break;
				default:
					/*
					 * Om oktalkoden inneh�ller 4, 5, 6 eller 7 (vilket koden inte st�djer)
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
	 * @return En int-array d�r index i motsvarar siffran p� plats i+1 efter punkten i oktalkoden
	 */
	public int[] getAllowedMoves(){
		return allowedMoves.clone();
	}
	
	/**
	 * Ger oktalkoden som en str�ng med inledande punkt (t.ex. ".133")
	 * @return Oktalkoden som en str�ng med inledande punkt (t.ex. ".133")
	 */
	public String getOctalcode(){
		return octalcode;
	}
	
	/**
	 * Ger antalet stenar i h�gen
	 * @return Antalet stenar i h�gen
	 */
	public int getSize(){
		return this.size;
	}
	
	/**
	 * Applicerar mex-regeln p� den givna arrayen, dvs. ger det minsta icke-negativa heltal som inte finns i arrayen
	 * @param a Arrayen med heltal
	 * @return Det minsta icke-negativa heltalet som inte finns i a
	 */
	public static int mex(int[] a){
		int lowest = 0;
		
		/*
		 * Det st�rsta v�rde som mex(a) kan anta �r a.length (om alla v�rden fr�n 0 till a.length-1 finns i arrayen)
		 * s� vi stegar igenom och kollar alla tal upp till detta
		 */
		for(int i = 0;i < a.length + 1; i++){
			if(isInArray(i, a)){
				/*
				 * Om i finns i arrayen, �ka lowest med ett
				 */
				lowest++;
			}else{
				/*
				 * Om i inte finns s� m�ste det v�rde lowest har nu vara mex(a) s� returnera lowest
				 */
				return lowest;
			}
		}
		/*
		 * Om vi av n�gon anledning har g�tt igenom alla och �nd� inte hittat nimv�rdet, kasta ett fel
		 */
		throw(new RuntimeException("No mex value found."));
	}
	
	/**
	 * Omvandlar en array med spel till en array med deras nimv�rden
	 * @param a Arrayen med spel
	 * @return En array med nimv�rdena fr�n spelen i a
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
	 * @param i Talet vi s�ker
	 * @param a Arrayen vi s�ker i
	 * @return true om i finns som v�rde i a, false annars
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
	 * Equals-metod fr�n Object
	 */
	public boolean equals(Object o){
		if(!(o instanceof Game)){
			return false;
		}
		Game g = (Game)o;
		
		/*
		 * Tv� spel �r lika om de har samma oktalkod och samma antal stenar
		 */
		if(this.getSize()==g.getSize() && this.getOctalcode().equals(g.getOctalcode())){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * toString-metod fr�n Object
	 * Skriver ut spelet p� formen "S:stenar, N:nimv�rde"
	 */
	public String toString(){
		return "S:"+size + ", N:" + getNimValue();
	}
	
	/**
	 * Omvandlar en str�ng-oktalkod med inledande punkt (t.ex. ".133") till en int-array
	 * @param s Oktalkoden som str�ng med inledande punkt (t.ex. ".133")
	 * @return En array d�r index i motsvarar siffran p� plats i+1 efter punkten
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