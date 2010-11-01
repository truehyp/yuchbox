
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.mail.Address;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;



class Inte{
	int m_value;
	
	String m_test = new String();
	Inte(int _val){
		m_value = _val;
	}
	
	String GetVector(){
		return m_test;
	}
	
	void p(){
		//for(int i = 0;i < m_test.size();i++){
			HelloWorld.prt((String)m_test);
		//}
	}
}
/*!
 *  @brief note
 *  @author tzz
 *  @version 0.1
 */
public class HelloWorld {
	/*!
	 *  @brief main function
	 *  @param arg  parameters
	 */
	public static void main(String arg[]){
		
		HelloWorld test = new HelloWorld(); 
		test.berrySendTest();
	}
	
	public void berrySendTest(){
		
		try{
			
			Socket t_socket = GetSocketServer("111111","localhost",9716,false);
			sendReceive t_receive = new sendReceive(t_socket.getOutputStream(),t_socket.getInputStream());
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			os.write(msg_head.msgConfirm);
			sendReceive.WriteString(os, "111111");
			t_receive.SendBufferToSvr(os.toByteArray(), false);
			
			fetchMail t_mail = new fetchMail();
			
			String[] t_string = {"yuchting@gmail.com"};
			t_mail.SetSendToVect(t_string);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			t_mail.SetContain(df.format(new Date()));
			t_mail.SetSubject(t_mail.GetContain());
			
			final int t_math = (int)(Math.random() * 100);
			t_mail.SetMailIndex(t_math);
			
			t_mail.AddAttachment("HelloWorld.jar", readFileBuffer("HelloWorld.jar"));
			t_mail.AddAttachment("YuchBerryKey", readFileBuffer("YuchBerryKey"));
			
			
			os.close();
			os.write(msg_head.msgMail);
			t_mail.OutputMail(os);		
			
			t_receive.SendBufferToSvr(os.toByteArray(), true);
			
			ByteArrayInputStream in = new ByteArrayInputStream(t_receive.RecvBufferFromSvr());
			
			if(in.read() == msg_head.msgSendMail
				&& t_math == sendReceive.ReadInt(in)){
				prt(t_mail.GetSubject() + " mail deliver succ id<" + Integer.toString(t_math) + ">");
			}
						
		}catch(Exception _e){
			prt(_e.getMessage());
			_e.printStackTrace();
		}
	}
	
	private byte[] readFileBuffer(String _file)throws Exception{
		File t_file = new File(_file);
		byte[] t_buffer = new byte[(int)t_file.length()];
		
		FileInputStream in = new FileInputStream(_file);
		in.read(t_buffer, 0, t_buffer.length);
		
		return t_buffer;
	}
	public void berryRecvTest(){
		try{
			
			Socket t_socket = GetSocketServer("111111","localhost",9716,false);
			sendReceive t_receive = new sendReceive(t_socket.getOutputStream(),t_socket.getInputStream());
			
			while(true){

				ByteArrayInputStream in = new ByteArrayInputStream(t_receive.RecvBufferFromSvr());
				switch(in.read()){
					case msg_head.msgMail:
						fetchMail t_mail = new fetchMail();
						t_mail.InputMail(in);
						prt("receive idx: " + t_mail.GetMailIndex() + " subject: " + t_mail.GetSubject());
						
						// send msgSendMail to increase fetch index imm
						//
						ByteArrayOutputStream t_os = new ByteArrayOutputStream();
						t_os.write(msg_head.msgSendMail);
						//fetchMail.WriteInt(t_os,t_mail.GetMailIndex());
						t_receive.SendBufferToSvr(t_os.toByteArray(), true);
						
						// TODO display in berry
						//
						
						break;
					case msg_head.msgSendMail:
						
						// TODO display in berry
						// the post mail has been send
						//
						
						break;
				}
			}
			
		}catch(Exception _e){
			prt(_e.getMessage());
			_e.printStackTrace();
		}
	}
	
	public void test4(){
		Vector t_list = new Vector();
		t_list.addElement("\"yuch\"<yuchting@gmail.com>");
		t_list.addElement("yuch<yuchting@gmail.com>");
		t_list.addElement("\"yuch\"");
		try{
			String[] t_add = parseAddressList(t_list);
			for(int i = 0;i < t_add.length;i++){
				prt(t_add[i]);				
			}
		}catch(Exception _e){
			
		}
		
	}
	
	public static String[] parseAddressList(Vector _list)throws Exception{
		String[] 	t_addressList = new String[_list.size()];
		
		for(int i = 0;i < _list.size();i++){
			String add = (String)_list.elementAt(i);
			String t_name = null;
			
			final int t_start =add.indexOf('<');
			final int t_end = add.indexOf('>');
			
			final int t_start_quotation = add.indexOf('"');
			final int t_end_quotation = add.indexOf('"',t_start_quotation + 1);
			
			if(t_start_quotation != -1 && t_end_quotation != -1 ){			
				t_name = add.substring(t_start_quotation + 1, t_end_quotation);
			}else{
				t_name = "";
			}
			
			if(t_start != -1 && t_end != -1 ){			
				add = add.substring(t_start + 1, t_end);
			}else{
				t_name = "";
			}
			
			t_addressList[i] = add + t_name;
		}
		
		return t_addressList;
	}

	public void test3(){
		
		try{
			
			Properties p = new Properties(); 
			p.load(new FileInputStream("config.ini"));
			p.setProperty("userFetchIndex",Integer.toString(120));
			
			p.save(new FileOutputStream("config.ini"), "");
			p.clear();
			
		}catch(Exception _e){
			//sendReceive.prt(_e.getMessage());
			_e.printStackTrace();
		}
	}
	public void test2(){
		byte[] t_data = {0,2,3,4,5,6,7,8,9};
		ByteArrayInputStream in = new ByteArrayInputStream(t_data);
		
		byte[] t_rdata = new byte[5];
		in.read(t_rdata,0,t_rdata.length);
		
		prtA(t_rdata);
		
	}
	public void test1(){
		try{
			byte[] t_data = new byte[6540];
			
			for(int i = 0;i < t_data.length ;i++){
				t_data[i] = (byte)(Math.random() * 100);
			}
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			GZIPOutputStream zo = new GZIPOutputStream(os);
			zo.write(t_data);
			zo.close();
			
			ByteArrayOutputStream t_finalos = new ByteArrayOutputStream();
			
			final int length = (t_data.length << 16) | (os.toByteArray().length);
			
			WriteInt(t_finalos,length);
			t_finalos.write(os.toByteArray());
			
			t_finalos.close();
			
			//prtA(t_finalos.toByteArray());
				
			ByteArrayInputStream in = new ByteArrayInputStream(t_finalos.toByteArray());
			
			
			final int t_rLength = ReadInt(in);
			final int t_orgLength = t_rLength >>> 16;
			final int t_zipLength = t_rLength & 0x0000ffff;
			
			byte[] t_zipdata = new byte[t_zipLength];
			byte[] t_orgdata = new byte[t_orgLength];
			
			in.read(t_zipdata);
			
			//prtA(t_zipdata);
			
			GZIPInputStream zi	= new GZIPInputStream(
									new ByteArrayInputStream(t_zipdata));
			
			int t_readIndex = 0;
			int t_readNum = 0;
			while((t_readNum = zi.read(t_orgdata,t_readIndex,t_orgLength - t_readIndex)) > 0){
				t_readIndex += t_readNum;
			}
			
			
			prtA(t_orgdata);
			
		}catch(Exception _e){
			
			prt(_e.getMessage());
			_e.printStackTrace();			
		}
	}
	public void test(){
		try{
			int t_version = 5000;
			Vector temp = new Vector();
			for(int i = 0;i < 5;i++){				
				temp.addElement(new String("input " + Math.random()));
			}
			ByteArrayOutputStream t_stream = new ByteArrayOutputStream();
			
			WriteInt(t_stream,t_version);
			WriteStringVector(t_stream,temp);
			
			ByteArrayInputStream t_stream1 = new ByteArrayInputStream(t_stream.toByteArray());
			
			int t_version1 = 0;
			Vector temp1 = new Vector();
			
			t_version1 = ReadInt(t_stream1);
			ReadStringVector(t_stream1,temp1);
			
			
			prt("version " + t_version1);
			for(int i = 0;i < temp1.size();i++){
				prt((String)temp1.elementAt(i));
			}
		}catch(Exception _e){
			prt(_e.getMessage());
			_e.printStackTrace();
		}
	}
	
	public static Socket GetSocketServer(String _userPassword,String _host,int _port,boolean _ssl)throws Exception{
		
		if(_ssl){

			String	key				= "YuchBerryKey";  
			
			char[] keyStorePass		= _userPassword.toCharArray();
			char[] keyPassword		= _userPassword.toCharArray();
			
			KeyStore ks				= KeyStore.getInstance(KeyStore.getDefaultType());
			
			ks.load(new FileInputStream(key),keyStorePass);
			
			KeyManagerFactory kmf	= KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks,keyPassword);
			
			SSLContext sslContext = SSLContext.getInstance("SSLv3");
			sslContext.init(kmf.getKeyManagers(),null,null);
			  
			SSLSocketFactory factory=sslContext.getSocketFactory();
			
			return factory.createSocket(_host,_port);
			
		}else{
			
			return new Socket(InetAddress.getByName(_host),_port); 
		}	  
	}
	
	// static function to input and output integer
	//
	static public void WriteStringVector(OutputStream _stream,Vector _vect)throws Exception{
		
		final int t_size = _vect.size();
		_stream.write(t_size);
		
		for(int i = 0;i < t_size;i++){
			WriteString(_stream,(String)_vect.elementAt(i));
		}
	}
	
	static public void WriteString(OutputStream _stream,String _string)throws Exception{
		final byte[] t_strByte = _string.getBytes();
		WriteInt(_stream,t_strByte.length);
		if(t_strByte.length != 0){
			_stream.write(t_strByte);
		}
	}
	
		
	static public void ReadStringVector(InputStream _stream,Vector _vect)throws Exception{
		
		_vect.removeAllElements();
		
		int t_size = 0;
		t_size = _stream.read();
		
		for(int i = 0;i < t_size;i++){
			_vect.addElement(ReadString(_stream));
		}
	}
	
	static public String ReadString(InputStream _stream)throws Exception{
		
		final int len = ReadInt(_stream);
		if(len != 0){
			byte[] t_buffer = new byte[len];
			
			_stream.read(t_buffer);	
			return new String(t_buffer);
		}
		
		return new String("");
		
	}
	
	static public int ReadInt(InputStream _stream)throws Exception{
		return _stream.read() | (_stream.read() << 8) | (_stream.read() << 16) | (_stream.read() << 24);
	}

	static public void WriteInt(OutputStream _stream,int _val)throws Exception{
		_stream.write(_val);
		_stream.write(_val >>> 8 );
		_stream.write(_val >>> 16);
		_stream.write(_val >>> 24);
	}
	
	static void prt(String s) {
		System.out.println(s);
	}
	
	static void prtA(byte[] a) {
		
		for(int i = 0;i < a.length;i++){
			prt(String.valueOf(a[i]));
		}
	}
}