/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import java.util.Arrays;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
public class CeaserBreaker
{
	private String alphabit="abcdefghijklmnopqrstuvwxyz";
	private File file;
	private String decryptedStr;
	private Key key=Key.REGULAR;
	private char mostFreqLetter;
	private int decryptKey;
	public CeaserBreaker(Key key,char mostFreqLetter)
	{	
		this.mostFreqLetter=mostFreqLetter;
		setKeyType(key);
		this.decryptedStr=decrypt(getContent());
	}
	public CeaserBreaker(String encryptedMsg,Key key,char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		setKeyType(key);
		this.decryptedStr=decrypt(encryptedMsg);
	}		
	public CeaserBreaker(File file,Key key,char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		setKeyType(key);
		this.decryptedStr=decrypt(getContent(file));
	}
	public CeaserBreaker(int firstKey,int secondKey,char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		this.key=Key.MULTIPLE;
		this.decryptedStr=decrypt(getContent(),firstKey,secondKey);
	}
	public CeaserBreaker(String encryptedMsg,int firstKey,int secondKey,char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		this.key=Key.MULTIPLE;
		this.decryptedStr=decrypt(encryptedMsg,firstKey,secondKey);
	}
	public CeaserBreaker(File file,int firstKey,int secondKey,char mostFreqLetter)
	{
		this.mostFreqLetter=mostFreqLetter;
		this.key=Key.MULTIPLE;
		this.decryptedStr=decrypt(getContent(file),firstKey,secondKey);
	}
	public void setKeyType(Key key)
	{
		this.key=key;
	}
	public String getDecrypted()
	{
		return this.decryptedStr;
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
	private String getContent()
	{
		StringBuilder sb=null;
		try(BufferedReader input=new BufferedReader(new FileReader(getFile())))
		{
		sb=new StringBuilder();
		String line=null;
		while((line=input.readLine())!=null)
			sb.append(line+"\n");
		}catch(IOException ex){ex.printStackTrace();}	
		return sb.toString();
	}
	private String getContent(File file)
	{
		StringBuilder sb=null;
		try(BufferedReader input=new BufferedReader(new FileReader(file)))
		{
		sb=new StringBuilder();
		String line=null;
		while((line=input.readLine())!=null)
			sb.append(line+"\n");
		}catch(IOException ex){ex.printStackTrace();}
		return sb.toString();
	}

	private int[] countLetters(String message)
	{
		int index=-1;
		int[] count=new int[alphabit.length()];
		for(int i=0,n=message.length();i<n;i++)
		{
			index=alphabit.indexOf(Character.toLowerCase(message.charAt(i)));
			if(index!=-1)
			count[index]++;
			else continue;				
		}	
		return count;	
	}
	private static int getMaxIndex(int[] countArr)
	{
		int[] countCopy=new int[countArr.length];
		System.arraycopy(countArr,0,countCopy,0,countArr.length);
		Arrays.sort(countCopy);
		for(int i=0;i<countArr.length;i++)
		if(countArr[i]==countCopy[countCopy.length-1])
		return i;
		else continue;
		return -1;
	}
	public String decrypt(String encryptedMsg)
	{
		//return (key.toString().equalsIgnoreCase("multiple"))?decryptTwoKeys(encryptedMsg):new CeaserCipher().encrypt(encryptedMsg,(26-getKey(encryptedMsg)));
		return (key.toString().equalsIgnoreCase("multiple"))?decryptTwoKeys(encryptedMsg):new CeaserCipher((26-getKey(encryptedMsg))).encrypt(encryptedMsg);
	}
	public String decrypt(String encryptedMsg,int firstKey,int secondKey)
	{
		return decryptTwoKeys(encryptedMsg,firstKey,secondKey);
	}
	public String decryptTwoKeys(String encryptedMsg)
	{
		String firstHalf=halfOfString(encryptedMsg,0);
		String secondHalf=halfOfString(encryptedMsg,1);
		System.out.println("first Key: "+getKey(firstHalf));
		System.out.println("second Key: "+getKey(secondHalf));
		//firstHalf=new CeaserCipher().encrypt(firstHalf,(26-getKey(firstHalf)));
		//secondHalf=new CeaserCipher().encrypt(secondHalf,(26-getKey(secondHalf)));
		StringBuilder sb=new StringBuilder();
		for(int i=0,n=encryptedMsg.length(),f=0,s=0;i<n;i++)
		{
			if(i%2==0)
			sb.append(firstHalf.charAt(f++));	
			else if(i%2==1)
			sb.append(secondHalf.charAt(s++));
		}
		return sb.toString();
	}
	public String decryptTwoKeys(String encryptedMsg,int firstKey,int secondKey)
	{
		String firstHalf=halfOfString(encryptedMsg,0);
		String secondHalf=halfOfString(encryptedMsg,1);
		System.out.println("first Key: "+firstKey);
		System.out.println("second Key: "+secondKey);
		//firstHalf=new CeaserCipher().encrypt(firstHalf,(26-firstKey));
		//secondHalf=new CeaserCipher().encrypt(secondHalf,(26-secondKey));
		StringBuilder sb=new StringBuilder();
		for(int i=0,n=encryptedMsg.length(),f=0,s=0;i<n;i++)
		{
			if(i%2==0)
			sb.append(firstHalf.charAt(f++));	
			else if(i%2==1)
			sb.append(secondHalf.charAt(s++));
		}
		return sb.toString();
	}
	private String halfOfString(String message, int start)
	{
		StringBuilder sb=new StringBuilder();
		for(int i=start,n=message.length();i<n;i+=2)
		{
			sb.append(message.charAt(i));		
		}
		return sb.toString();
	}
	private int getKey(String encryptedMsg)
	{
		int idxFreq=getMaxIndex(countLetters(encryptedMsg));
		int shift=0;
		//assuming that the converted char that is most frequent was e and encrypted to be another
		if(idxFreq!=-1)
		{
			if(idxFreq<alphabit.indexOf(mostFreqLetter))
				shift=26-(alphabit.indexOf(mostFreqLetter)-idxFreq);
			else
				shift=idxFreq-alphabit.indexOf(mostFreqLetter);
		}
		return (this.decryptKey=shift);	
	}
	public int getDecryptKey()
	{
		return this.decryptKey;
	}
	public static enum Key{  MULTIPLE,REGULAR; } 
}