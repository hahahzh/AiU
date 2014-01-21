package job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import play.db.DB;
import play.jobs.Every;
import play.jobs.Job;


/**
 * 
 * 
 * @author hanzhao
 * 
 */
@Every("5s")
public class RecordJob extends Job {
	
	public static volatile Queue<String> logRecord = new ConcurrentLinkedQueue<String>();
	@Override
	public void doJob() throws Exception {
		if (logRecord.isEmpty()) {
			return;
		}
		
		Connection conn = DB.getConnection();
		PreparedStatement pst = null;
		
		try {
				conn.setAutoCommit(false);
				conn.setTransactionIsolation(conn.TRANSACTION_READ_UNCOMMITTED);
				pst = conn.prepareStatement("INSERT INTO `logs` (customer_name,`data`,imei,ip,mac,`type`) VALUES (?, ?, ?, ?, ?, ?)");

				String data = null;
				int i = 0;
				while (i < 5000) {
					data = logRecord.poll();
					if(data == null)break;
					String[] d = data.split(",");
					pst.setString(1, d[0]);
					pst.setLong(2, new Date().getTime());
					pst.setString(3, d[1]);
					pst.setString(4, d[2]);
					pst.setString(5, d[3]);
					pst.setString(6, d[4]);
					pst.addBatch();
					i++;
				}
				pst.executeBatch();
				conn.commit();
		} catch (Exception e) {
			play.Logger.error("RecordJob onException:"+e.getMessage());
		}finally{
			DB.close();
		}
		
		
	}

	@Override
	public void onException(Throwable e) {
		play.Logger.error("RecordJob onException");
		super.onException(e);
	}
}