package job;

import java.io.File;

import play.jobs.Every;
import play.jobs.Job;


/**
 * 
 * 
 * @author hanzhao
 * 
 */
@Every("1h")
public class DeleteTmpJob extends Job {
	private static final String path = "tmp";
	private static final String x = "/";
	@Override
	public void doJob() throws Exception {
		File file=new File(play.Play.applicationPath+x+path);
		File files[] = file.listFiles();
		for(File f : files){
			if(f.isFile()){
				f.delete();
			}
		}
	}

	@Override
	public void onException(Throwable e) {
		play.Logger.error("DeleteTmpJob onException");
		super.onException(e);
	}
}