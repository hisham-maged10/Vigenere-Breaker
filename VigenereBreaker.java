/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
public class VigenereBreaker
{
	private String alphabit="abcdefghijklmnopqrstuvwxyz";
	private int keyLen;
	private int[] decryptKeys;
	private int validWordsCount;
	private String decryptedMsg;
	private char mostFreqLetter;
	public VigenereBreaker(int keyLen,char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		this.keyLen=keyLen;
		breakVigenere();
	}
	public VigenereBreaker(char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		breakVigenere(readDictionary());
	}
	public VigenereBreaker()
	{
		breakVigenere(readDictionaries());
	}
	public String getDecrypted()
	{
		return this.decryptedMsg;
	}
	public String sliceString(String msg,int start, int space)
	{
		StringBuilder sb=new StringBuilder();
		for(int i=start,n=msg.length();i<n;i+=space)
			sb.append(msg.charAt(i));
		return sb.toString();
	}
	public Integer[] tryKeyLength(String encrypted,int keyLen,char mostCommon)
	{
		Integer[] key=new Integer[keyLen];
		for(int i=0;i<keyLen;i++)
		{
			key[i]=new CeaserBreaker(sliceString(encrypted,i,keyLen),CeaserBreaker.Key.REGULAR,mostCommon).getDecryptKey();	
		}
		//System.out.println(Arrays.toString(key));
		return key;
	}
	public void breakVigenere()
	{
		String content=getContent(getFile());
		Integer[] decryptKeys=tryKeyLength(content,this.keyLen,mostFreqLetter);
		for(int i=0;i<decryptKeys.length;i++)decryptKeys[i]=26-decryptKeys[i];
		this.decryptedMsg= new VigenereCipher(content,decryptKeys).getEncrypted();
		//this.decryptedMsg=new VigenereCipher(content,keys).getDecryptedMsg();
	}
	//overload
	public void breakVigenere(HashSet<String> dictionary)
	{
		String content=getContent(getFile());
		this.decryptedMsg=breakForLanguages(content,dictionary,true);
	}
	//overload
	public void breakVigenere(HashMap<String,HashSet<String>> dictionaries)
	{
		String content=getContent(getFile());
		this.decryptedMsg=breakForAllLanguages(content,dictionaries);
	}
	public String breakForAllLanguages(String encrypted,HashMap<String,HashSet<String>> languages)
	{
		String decrypted=null;
		int max=0;
		int temp=0;
		String lang=null;
		String chosenDecrypt=null;
		for(String e:languages.keySet())
		{
			System.out.println(e);
			decrypted=breakForLanguages(encrypted,languages.get(e),false);
			if(max<=(temp=countWords(decrypted,languages.get(e))))
			{
				max=temp;
				lang=e;	
				chosenDecrypt=decrypted;
			}
		}
		System.out.println("Done decrypting!");
		System.out.println("The identified Language is "+ lang);
		return chosenDecrypt;
	}
	public String breakForLanguages(String encrypted,HashSet<String> dictionary,boolean knownLetter)
	{
		Integer[] decryptKeys=null;
		String decryptedMsg=null;
		int max=0;
		int temp=0;
		int keyLen=-1;
		for(int i=1;i<=100;i++)	
		{
			decryptKeys=tryKeyLength(encrypted,i,(knownLetter)?mostFreqLetter:MostCommonChar(dictionary));
			//decryptKeys=tryKeyLength(encrypted,i,MostCommonChar(dictionary));
			for(int j=0;j<decryptKeys.length;j++)decryptKeys[j]=26-decryptKeys[j];
			decryptedMsg= new VigenereCipher(encrypted,decryptKeys).getEncrypted();
			if(max<(temp=countWords(decryptedMsg,dictionary)))
			{	
				max=temp;
				keyLen=i;
			}
		}
		//decryptKeys=tryKeyLength(encrypted,keyLen,MostCommonChar(dictionary));
		decryptKeys=tryKeyLength(encrypted,keyLen,(knownLetter)?mostFreqLetter:MostCommonChar(dictionary));
		System.out.println(MostCommonChar(dictionary));
		System.out.println("Key length: "+keyLen);
		System.out.println("Keys: "+Arrays.toString(decryptKeys));
		System.out.println("valid words: "+max);
		for(int j=0;j<decryptKeys.length;j++)decryptKeys[j]=26-decryptKeys[j];
		return new VigenereCipher(encrypted,decryptKeys).getEncrypted();
	}
	private String getContent(File file)
	{
		StringBuilder sb=null;
		try(BufferedReader input=new BufferedReader(new FileReader(file)))
		{
		sb=new StringBuilder();
		String line=null;
		while((line=input.readLine())!=null)
			sb.append(line+ "\n");
		}catch(IOException ex){ex.printStackTrace();}
		return sb.toString();
	}
	private HashSet<String> readDictionary()
	{
		String line=null;
		HashSet<String> words=new HashSet<>();
		try(BufferedReader input=new BufferedReader(new FileReader(getFile())))
		{	
			while((line=input.readLine())!=null)
				words.add(line.toLowerCase());	
		}
		catch(IOException ex){System.out.println("File error");return readDictionary();}
		return words;
	}
	private HashSet<String> readDictionary(File file)
	{
		String line=null;
		HashSet<String> words=new HashSet<>();
		try(BufferedReader input=new BufferedReader(new FileReader(file)))
		{	
			while((line=input.readLine())!=null)
				words.add(line.toLowerCase());	
		}
		catch(IOException ex){System.out.println("File error");return readDictionary();}
		return words;
	}
	private char MostCommonChar(HashSet<String> dictionary)
	{
		int[] charCount=new int[26];
		StringBuilder sb=new StringBuilder();
		for(String e:dictionary)
			sb.append(e);
		int index=indexMostCommonChar(sb);
		//System.out.println(this.alphabit.charAt(index));
		return (index!=-1)?this.alphabit.charAt(index):'\u0000';
	}
	/*private char MostCommonChar(HashSet<String> dictionary)
	{
		HashMap<Character,Integer> charFreq=new HashMap<>();
		for(String e:dictionary)
		{	
			for(int i=0;i<e.length();i++)
			{
				char ch=e.charAt(i);
				if(!Character.isLetter(ch)) break;
				if (charFreq.containsKey(ch)) {
         		           int freq = charFreq.get(ch);
                    		   charFreq.put(ch, freq + 1);
                		} else	{
                    		charFreq.put(ch, 1);
                		}
            		}
       		 }
		char mostCommonChar = ' ';
        int maxCount = 0;
        for (char ch : charFreq.keySet()) {
            int freq = charFreq.get(ch);
            // If on first loop, initialize tracker variables 
            if (Character.isSpaceChar(mostCommonChar)) {
                mostCommonChar = ch;
                maxCount = freq;
            } else {
                if (freq > maxCount) {
                    mostCommonChar = ch;
                    maxCount = freq;
                }
            }
        }
        return mostCommonChar;
    }*/
	private int indexMostCommonChar(StringBuilder concatStr)
	{
		int[] charCount=new int[26];
		for(int i=0,n=concatStr.length(),index=-1;i<n;i++)
			if((index=this.alphabit.indexOf(concatStr.charAt(i)))!=-1)
				++charCount[index];
		int[] tempArr=new int[charCount.length];
		System.arraycopy(charCount,0,tempArr,0,charCount.length);
		Arrays.sort(tempArr);
		for(int i=0;i<charCount.length;i++)
			if(charCount[i]==tempArr[tempArr.length-1])
				return i;
		return -1;
	}
	private HashMap<String,HashSet<String>> readDictionaries()
	{
		JFileChooser chooser=new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		HashMap<String,HashSet<String>> dictionaries=new HashMap<>();
		File[] files=null;
		do
		{
			System.out.println("Select one or more dictionary");
			chooser.showOpenDialog(null);
		}while((files=chooser.getSelectedFiles())==null);
		for(File e:files)
			dictionaries.put(e.getName(),readDictionary(e));
		return dictionaries;
	}
	public int countWords(String message,HashSet<String> dictionary)
	{	
		int count=0;
		String[] words=message.toLowerCase().split("\\W+");
		for(String e:words)
			if(dictionary.contains(e))count++;
		return count;
	}
	private File getFile()
	{
		JFileChooser chooser=new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		try{
		do
		{
			System.out.println("Please select a file to decrypt");
			chooser.showOpenDialog(null);	
		}while(chooser.getSelectedFile()==null);
		}catch(NullPointerException ex){System.out.println("Incorrect Respone"); return getFile();}
		return chooser.getSelectedFile();
	}

}