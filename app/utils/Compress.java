package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.Http.Header;

/**
 * 压缩Response
 * 
 * @author hanzhao
 * 
 */
public class Compress extends Controller{
	private static final String KEY = "gzip";
	
	@Finally
    public static void compress() throws IOException {
		
		Header ae = request.headers.get("Accept-Encoding");
		if(ae == null || ae.value() == null || ae.value().toLowerCase().indexOf(KEY) == -1){
			return;
		}
		
        String text = response.out.toString();
     
        final ByteArrayOutputStream gzip = gzip(text);
        response.setHeader("Content-Encoding", "gzip, deflate");
        response.setHeader("Content-Length", gzip.size() + "");
        response.out = gzip;
    }

    public static ByteArrayOutputStream gzip(final String input)
            throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        final ByteArrayOutputStream stringOutputStream = new ByteArrayOutputStream((int) (input.length() * 0.75));
        final OutputStream gzipOutputStream = new GZIPOutputStream(stringOutputStream);

        final byte[] buf = new byte[5000];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            gzipOutputStream.write(buf, 0, len);
        }

        inputStream.close();
        gzipOutputStream.close();
        return stringOutputStream;
    }
}