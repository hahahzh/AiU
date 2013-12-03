package utils;


import java.io.*;
import java.net.*;
import java.util.*;

public class HttpUtils {

	public static final String Response_Code = "Response-Code";
	
	public static final String Response_Message = "Response-Message";
	
	public static final String ENCODING = "Encoding";
	
	/**
     * ���ָ��URL�����
     * @param url
     * @param headers ��HTTP�����ͷ������Map�б�����<String,String>
     * @param connectTimeout �������ӳ�ʱʱ�䣬��λ��ms
     * @param readTimeout ���ö���ʱʱ�䣬��λ��ms
     * @return ���ݣ�ͬʱ��headers�У���HTTP��Ӧ��ͷ��
     * 			���У�
     * 				<code>Response-Code</code>��Ӧ Response Code
     * 				<code>Response-Message</code>��Ӧ Response Message
     * 				<code>Encoding</code>��Ӧ��Ӧ�������Encoding
     * @throws IOException
     */
    public static byte[] webGet(URL url, java.util.Map headers,int connectTimeout,int readTimeout) throws
        IOException {
            if(headers == null)
            	headers = new java.util.HashMap();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();

            String jdkV = System.getProperty("java.vm.version");
            if(jdkV!=null && jdkV.indexOf("1.5")!=-1){
              Class clazz = conn.getClass();
              try{
                java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout",new Class[]{int.class});
                method.invoke(conn,new Object[]{new Integer(connectTimeout)});
                method = clazz.getMethod("setReadTimeout",new Class[]{int.class});
                method.invoke(conn,new Object[]{new Integer(readTimeout)});
              }catch(Exception ec){
                ec.printStackTrace();
              }
            }else{
              System.setProperty("sun.net.client.defaultConnectTimeout","" + connectTimeout);
              System.setProperty("sun.net.client.defaultReadTimeout", "" + readTimeout);
            }
            
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                String key = (String) me.getKey();
                String value = (String) me.getValue();
                if (key != null && value != null) {
                    conn.setRequestProperty(key, value);
                }
            }

            String contentType = conn.getContentType();
            String encoding = null;
            if (contentType != null &&
                contentType.toLowerCase().indexOf("charset") > 0) {
                int k = contentType.toLowerCase().indexOf("charset");
                if (contentType.length() > k + 7) {
                    String sss = contentType.substring(k + 7).trim();
                    k = sss.indexOf("=");
                    if (k >= 0 && sss.length() > k + 1) {
                        encoding = sss.substring(k + 1).trim();
                        if (encoding.indexOf(";") > 0) {
                            encoding = encoding.substring(0,
                                encoding.indexOf(";")).trim();
                        }
                    }

                }
            }
            headers.clear();

            int k = 0;
            String feildValue = null;
            while ( (feildValue = conn.getHeaderField(k)) != null) {
                String key = conn.getHeaderFieldKey(k);
                k++;
                if (key != null) {
                    headers.put(key, feildValue);
                }
            }
            headers.put("Response-Code", new Integer(conn.getResponseCode()));
            headers.put("Response-Message", conn.getResponseMessage());

            java.io.InputStream bis = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte bytes[] = new byte[1024];
            int n = 0;
            while ( (n = bis.read(bytes)) > 0) {
                out.write(bytes, 0, n);
            }
            bis.close();
            
            bytes = out.toByteArray();
            if (encoding == null) {
                try {
                    for (int i = 0; i < 64 && i < bytes.length - 2; i++) {
                        if (bytes[i] == '?' && bytes[i + 1] == '>') {
                            String s = new String(bytes, 0, i);
                            if (s.indexOf("encoding") > 0) {
                                s = s.substring(s.indexOf("encoding") + 8);
                                if (s.indexOf("=") >= 0) {
                                    s = s.substring(s.indexOf("=") + 1).trim();
                                    if (s.charAt(0) == '"') {
                                        s = s.substring(1);
                                    }
                                    if (s.indexOf("\"") > 0) {
                                        encoding = s.substring(0,
                                            s.indexOf("\""));

                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (encoding == null) {
                encoding = "UTF-8";
            }
            headers.put("Encoding", encoding);

            return bytes;

        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (conn != null) {
                try {
                    conn.disconnect();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


	/**
     * ��ָ����URL�ķ�����ݣ�����÷���ֵ
     * @param url
     * @param headers ��HTTP�����ͷ������Map�б�����<String,String>
     * @param content ������
     * @param connectTimeout �������ӳ�ʱʱ�䣬��λ��ms
     * @param readTimeout ���ö���ʱʱ�䣬��λ��ms

     * @return ���ݣ�ͬʱ��headers�У���HTTP��Ӧ��ͷ��
     * 			���У�
     * 				<code>Response-Code</code>��Ӧ Response Code
     * 				<code>Response-Message</code>��Ӧ Response Message
     * 				<code>Encoding</code>��Ӧ��Ӧ�������Encoding
     * @throws IOException
     */
    public static byte[] webPost(URL url, byte[] content,java.util.Map headers,int connectTimeout,int readTimeout) throws
        IOException {
        HttpURLConnection urlconnection = null;
        try {
            urlconnection = (HttpURLConnection)url.openConnection();

            //set connectTimeout and readTimeout
            String jdkV = System.getProperty("java.vm.version");
            if(jdkV!=null && jdkV.indexOf("1.5")!=-1){
              Class clazz = urlconnection.getClass();
              try{
                java.lang.reflect.Method method = clazz.getMethod("setConnectTimeout",new Class[]{int.class});
                method.invoke(urlconnection,new Object[]{new Integer(connectTimeout)});
                method = clazz.getMethod("setReadTimeout",new Class[]{int.class});
                method.invoke(urlconnection,new Object[]{new Integer(readTimeout)});
              }catch(Exception ec){
                ec.printStackTrace();
              }
            }else{
              System.setProperty("sun.net.client.defaultConnectTimeout","" + connectTimeout);
              System.setProperty("sun.net.client.defaultReadTimeout", "" + readTimeout);
            }
            
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry me = (Map.Entry) it.next();
                String key = (String) me.getKey();
                String value = (String) me.getValue();
                if (key != null && value != null) {
                	urlconnection.setRequestProperty(key, value);
                }
            }
            urlconnection.setRequestProperty("Request-method", "post");
            urlconnection.setRequestProperty("Content-length",
                                             Integer.toString(content.length));

            urlconnection.setDoInput(true);
            urlconnection.setDoOutput(true);
	
            
            OutputStream outputstream = urlconnection.getOutputStream();
            outputstream.write(content);
            outputstream.flush();


            String contentType = urlconnection.getContentType();
            String encoding = null;
            if (contentType != null &&
                contentType.toLowerCase().indexOf("charset") > 0) {
                int k = contentType.toLowerCase().indexOf("charset");
                if (contentType.length() > k + 7) {
                    String sss = contentType.substring(k + 7).trim();
                    k = sss.indexOf("=");
                    if (k >= 0 && sss.length() > k + 1) {
                        encoding = sss.substring(k + 1).trim();
                        if (encoding.indexOf(";") > 0) {
                            encoding = encoding.substring(0,
                                encoding.indexOf(";")).trim();
                        }
                    }

                }
            }
            headers.clear();

            int k = 0;
            String feildValue = null;
            while ( (feildValue = urlconnection.getHeaderField(k)) != null) {
                String key = urlconnection.getHeaderFieldKey(k);
                k++;
                if (key != null) {
                    headers.put(key, feildValue);
                }
            }
            headers.put("Response-Code", new Integer(urlconnection.getResponseCode()));
            headers.put("Response-Message", urlconnection.getResponseMessage());

            java.io.InputStream bis = urlconnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte bytes[] = new byte[1024];
            int n = 0;
            
            int count = 0;
            while( (n = bis.read(bytes)) > 0){
                out.write(bytes, 0, n);
            }
            bis.close();
            bytes = out.toByteArray();
            out.close();
            outputstream.close();

            if (encoding == null) {
                encoding = "UTF-8";
            }
            headers.put("Encoding", encoding);
            
            return bytes;
        }
        catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            if (urlconnection != null){
            	urlconnection.disconnect();
            }
        }
    }
}
