/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
public class TestVigenere
{
	public static void main(String[] args)
	{
		testing();
	}	
	public static void testing()
	{
		
		//VigenereBreaker vb=new VigenereBreaker(5,'e');
		//VigenereBreaker vb=new VigenereBreaker('e');
		VigenereBreaker vb=new VigenereBreaker();
		System.out.println(vb.getDecrypted());
	}
}