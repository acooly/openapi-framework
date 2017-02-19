package com.yiji.framework.openapi.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;


/**
 * orderNo生成器
 * 
 * 简单实现：支持分布式环境，单机秒级并发1000
 * 
 * @author zhangpu
 * 
 */
public class Ids {

	public static final char PADCHAR = '0';

	// ******* 特定场景ID ***********//
	/**
	 * 生产GID
	 * 
	 * 长度：32 格式：系统编码(4字节)+保留(8字节)+did(20字节)
	 * 
	 * @return
	 */
	public static String gid(String systemCode, String reserved) {
		StringBuilder sb = new StringBuilder();
		sb.append(padding(systemCode, 4));
		sb.append(padding(reserved, 8));
		sb.append(Did.getInstance().getId(20));
		return sb.toString();
	}

	public static String gid(String systemCode) {
		return gid(systemCode, null);
	}

	public static String gid() {
		return gid("G001", null);
	}

	/**
	 * 统一不重复内部订单号
	 * 
	 * @param systemCode
	 *            4字节系统编码
	 * @return 24字节不重复编码
	 */
	public static String oid(String systemCode) {
		StringBuilder sb = new StringBuilder();
		sb.append(padding(systemCode, 4));
		sb.append(Did.getInstance().getId(20));
		return sb.toString();
	}

	public static String oid() {
		return oid("O001");
	}

	/**
	 * 获取分布式Id
	 * 
	 * @return 默认20字符长度的数字不重复编码
	 */
	public static String getDid() {
		return Did.getInstance().getId();
	}

	public static String getDid(int size) {
		return Did.getInstance().getId(size);
	}

	public static String getDid(String prefix) {
		return prefix + Did.getInstance().getId();
	}

	private static String padding(String text, int size) {
		String txt = Strings.trimToEmpty(text);
		if (Strings.length(txt) > size) {
			txt = Strings.substring(txt, Strings.length(txt) - size);
		}
		return Strings.leftPad(txt, size, PADCHAR);
	}

	/**
	 * ID生成器
	 * 
	 * 20字符长度 yyMMddHHmmss+3位本机IP末三位+5位随机数字
	 * 
	 * @author zhangpu
	 *
	 */
	public static class Did {
		private static final Logger logger = LoggerFactory.getLogger(Ids.class);
		private static final int MIN_LENGTH = 20;
		private static final int SEQU_LENGTH = 5;
		private static final int SEQU_MAX = 99999;

		private AtomicLong sequence = new AtomicLong(0);
		private String nodeFlag;
		private Object nodeFlagLock = new Object();
		private static Did did = new Did();

		public static Did getInstance() {
			return did;
		}

		/**
		 * 生产新Id
		 * 
		 * @return
		 */
		public String getId() {
			return getId(MIN_LENGTH);
		}

		public String getId(int size) {
			if (size < MIN_LENGTH) {
				throw Exceptions.runtimeException("did最小长度为" + MIN_LENGTH);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(Dates.format(new Date(), "yyMMddHHmmss"));
			sb.append(getNodeFlag());
			sb.append(getSequ());
			return sb.toString();
		}

		public String getSequ() {
			long timeCount = System.currentTimeMillis() / 1000 * SEQU_MAX;
			while (true) {
				long now = sequence.get();
				if (timeCount > now) {
					// 已经过了本秒，则设置新的值
					if (sequence.compareAndSet(now, timeCount)) {
						break;
					}
				} else {
					if (sequence.compareAndSet(now, now + 1)) {
						timeCount = now + 1;
						break;
					}
				}
			}
			int sn = (int) (timeCount % SEQU_MAX);
			return Strings.leftPad(String.valueOf(sn), SEQU_LENGTH, '0');
		}

		private String getNodeFlag() {
			if (this.nodeFlag == null) {
				synchronized (nodeFlagLock) {
					if (this.nodeFlag == null) {
						this.nodeFlag = generateNodeFlag();
					}
				}
			}
			return this.nodeFlag;
		}

		/**
		 * 简单节点编码
		 * 
		 * 逻辑：Ip地址后三位，便于快速知道是哪个节点
		 * 
		 * @return
		 */
		private String generateNodeFlag() {
			String ipPostfix = null;
			try {
				String ip = IPUtil.getFirstNoLoopbackIPV4Address();
				ipPostfix = Strings.substringAfterLast(ip, ".");
			} catch (Exception e) {
				logger.warn("生产DID要素本机IP获取失败:" + e.getMessage());
			}
			return StringUtils.leftPad(ipPostfix, 3, "0");
		}

		private Did() {
			super();
		}
	}

	public static void main(String[] args) throws Exception {
		// int threads = 10;
		// final int threadsRunCount = 10000;
		// System.out.println("request:" + threads * threadsRunCount);
		// final Set<String> sequs = Sets.newCopyOnWriteArraySet();
		// final CountDownLatch latch = new CountDownLatch(threads);
		// ExecutorService fixedThreadPool =
		// Executors.newFixedThreadPool(threads);
		// for (int i = 0; i < threads; i++) {
		// fixedThreadPool.execute(new Runnable() {
		// @Override
		// public void run() {
		// String id = null;
		// for (int j = 0; j < threadsRunCount; j++) {
		// id = getDid();
		// System.out.println(id);
		// sequs.add(id);
		// }
		// latch.countDown();
		// System.out.println(Thread.currentThread().getName() + ": countdown");
		// }
		// });
		// }
		// latch.await();
		// System.out.println("result:" + sequs.size());
		// fixedThreadPool.shutdown();
		System.out.println(Strings.leftPad("H001", 3, '0'));
	}

}
