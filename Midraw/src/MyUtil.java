import java.util.StringTokenizer;

public class MyUtil
{
	public static String[] parseMsg(String msg)
	{
		StringTokenizer st=new StringTokenizer(msg,";");
		String[] arr=new String[st.countTokens()];
		for(int i=0;st.hasMoreTokens();i++)
		{
			arr[i]=st.nextToken();
		}
		return arr;
	}
	
	public static String[] parseMsg(String msg,String de)
	{
		StringTokenizer st=new StringTokenizer(msg,de);
		String[] arr=new String[st.countTokens()];
		for(int i=0;st.hasMoreTokens();i++)
		{
			arr[i]=st.nextToken();
		}
		return arr;
	}
}
