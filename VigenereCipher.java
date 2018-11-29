/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import java.util.ArrayList;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
public class VigenereCipher
{
	private String alphabit="abcdefghijklmnopqrstuvwxyz";
	private Integer[] cipherSeq;
	private String encryptedMsg;
	public VigenereCipher(String msg,String key)
	{
		this.cipherSeq=findCipherSeq(key);
		this.encryptedMsg=encrypt(msg);	
	}
	public VigenereCipher(String key)
	{
		this.cipherSeq=findCipherSeq(key);
		this.encryptedMsg=encrypt(getContent(getFile()));	
	}
	public VigenereCipher(Integer[] key)
	{
		this.cipherSeq=key;
		this.encryptedMsg=encrypt(getContent(getFile()));	
	}
	public VigenereCipher(String msg,Integer[] key)
	{
		this.cipherSeq=key;
		this.encryptedMsg=encrypt(msg);	
	}
	public String getEncrypted()
	{
		return this.encryptedMsg;
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

	public String encrypt(String msg)
	{
		StringBuilder sb=new StringBuilder();
		char[] charArr=msg.toCharArray();
		for(int i=0;i<charArr.length;i++)
			sb.append(new CeaserCipher(cipherSeq[i%cipherSeq.length]).encrypt(Character.toString(charArr[i])));
		return sb.toString();
	}
	/*private String decrypt(String msg)
	{
		Integer[] decryptKeys=new Integer[cipherSeq.length];
		System.arraycopy(cipherSeq,0,decryptKeys,0,cipherSeq.length);
		for(int i=0;i<decryptKeys.length;i++)decryptKeys[i]=26-decryptKeys[i];
		return new VigenereCipher(msg,decryptKeys).getEncrypted();
	}*/
	/*public String getDecryptedMsg()
	{
		return decrypt(encryptedMsg);	
	}*/
	private Integer[] findCipherSeq(String key)
	{
		ArrayList<Integer> cipherSeq=new ArrayList<>();
		char[] charArr=key.toLowerCase().toCharArray();
		for(Character e:charArr)
			cipherSeq.add(alphabit.indexOf(e));
		return cipherSeq.toArray(new Integer[cipherSeq.size()]);	
	}	
}