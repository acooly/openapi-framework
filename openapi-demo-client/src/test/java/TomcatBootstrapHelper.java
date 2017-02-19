import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletException;

/**
 * Created by zhangpu on 2015/10/20.
 */
public class TomcatBootstrapHelper {
	private static final char ENTER_CHAR = '\n';
	private static final int DEFAULT_PORT = 8090;
	private static final String DEFULT_ENV = "dev";
	private int port;
	private boolean isServlet3Enable;

	public static void main(String[] args) {
		new TomcatBootstrapHelper(DEFAULT_PORT, false, DEFULT_ENV).start();
	}

	public TomcatBootstrapHelper(int port, boolean isServlet3Enable, String env) {
		this.port = 8080;
		this.isServlet3Enable = false;
		System.setProperty("spring.profiles.active", env);
		this.port = port;
		this.isServlet3Enable = isServlet3Enable;
	}

	public TomcatBootstrapHelper(int port, boolean isServlet3Enable) {
		this(port, isServlet3Enable, "dev");
	}

	public TomcatBootstrapHelper(int port) {
		this(port, false);
	}

	public TomcatBootstrapHelper() {
		this(8080);
	}

	public void start() {
		try {
			long e = System.currentTimeMillis();
			Tomcat tomcat = new Tomcat();
			this.configTomcat(tomcat);
			tomcat.start();
			long end = System.currentTimeMillis();
			this.log(end - e);

			while (true) {
				char c;
				do {
					c = (char) System.in.read();
				} while (c != 10);

				e = System.currentTimeMillis();
				System.out.println("重启tomcat...");
				tomcat.stop();
				tomcat.start();
				end = System.currentTimeMillis();
				this.log(end - e);
			}
		} catch (Exception var7) {
			System.err.println("非常抱歉，貌似启动挂了...,请联系bohr");
			var7.printStackTrace();
		}
	}

	private void configTomcat(Tomcat tomcat) throws ServletException {
		tomcat.setBaseDir("target");
		tomcat.setPort(this.port);
		Connector connector = new Connector("HTTP/1.1");
		connector.setPort(this.port);
		connector.setURIEncoding("utf-8");
		tomcat.setConnector(connector);
		tomcat.getService().addConnector(connector);
		String webappPath = this.getWebappsPath();
		System.out.println("webapp目录：" + webappPath);
		Context ctx = tomcat.addWebapp("", webappPath);
		StandardJarScanner scanner = (StandardJarScanner) ctx.getJarScanner();
		if (!this.isServlet3Enable) {
			scanner.setScanAllDirectories(false);
			scanner.setScanClassPath(true);
		}

		tomcat.setSilent(true);
		System.setProperty("org.apache.catalina.SESSION_COOKIE_NAME", "JSESSIONID" + this.port);
	}

	private void log(long time) {
		System.out.println("********************************************************");
		System.out.println("启动成功: http://127.0.0.1:" + this.port + "   in:" + time + "ms");
		System.out.println("您可以直接在console里敲回车，重启tomcat,just have a try");
		System.out.println("********************************************************");
	}

	public String getWebappsPath() {
		String file = this.getClass().getClassLoader().getResource(".").getFile();
		return file.substring(0, file.indexOf("target")) + "src/main/webapp";
	}
}
