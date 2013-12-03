package controllers;

import java.util.HashMap;

import org.w3c.dom.Element;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;
import utils.APNSUtil;


/**
 * 
 * 
 * @author hanzhao
 * 
 */
@Every("10s")
public class PushJob extends Job {
	@Override
	public void doJob() throws Exception {
//		if (TcpServer.moveAlarmTask.isEmpty()) {
//			return;
//		}
//		APNSUtil.initializeConnection();
//		while (!TcpServer.moveAlarmTask.isEmpty()) {
//			// 从队列中去掉当前要处理的数据
//			Location location = TcpServer.moveAlarmTask.poll();
//			try {
//				if (location.locator != null
//						&& location.locator.guardian1 != null
//						&& location.locator.moveAlarm != null
//						&& location.locator.moveAlarm.switch_on
//						&& location.locator.guardian1.type == 1) {
//
//					if ((location.valid >> 7) == 1) {
//						play.Logger
//								.debug("[GPSMoveAlarmJob] MoveAlarm send serialNumber = "
//										+ location.locator.serialNumber
//										+ ",location.locator.id = "
//										+ location.locator.id
//										+ ",dateTime = "
//										+ location.dateTime);
//						APNSUtil.send(location.locator.guardian1.serialNumber,
//								"您好,i-lbs通知您定位器[" + location.locator.name
//										+ "]有移位报警!", 1, "default",
//								new HashMap());
//					}
//				}
//			} catch (Exception e) {
//				play.Logger
//						.debug("[GPSMoveAlarmJob] iphone push send failed serialNumber = "
//								+ location.locator.serialNumber
//								+ ",location.locator.id = "
//								+ location.locator.id
//								+ ",dateTime = "
//								+ location.dateTime);
//			}
//		}
//		APNSUtil.stopConnection();
	}

	@Override
	public void onException(Throwable e) {
		play.Logger.error("PushJob onException");
		super.onException(e);
	}
}
