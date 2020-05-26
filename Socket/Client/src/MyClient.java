import java.net.*;
import java.io.*;
public class MyClient{
    public static void main(String args[])throws Exception{
        Socket s=new Socket("145.24.222.211",8000);
        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        String str="",str2="";
        while(!str.equals("stop")){
            // Send
            str=br.readLine();
            dout.writeUTF(str);
            dout.flush();

            // Receive
            str2=din.readUTF();
            System.out.println("Server says: "+str2);
        }

        dout.close();
        s.close();
    }}