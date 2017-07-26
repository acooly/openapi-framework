package com.acooly.openapi.framework.common.utils.ketama;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * katama测试 Created by zhangpu on 2015/9/4.
 */
public class KetamaNodeLocatorTest {

	static Random ran = new Random();

	/**
	 * key's count
	 */
	private static final Integer EXE_TIMES = 1000000;
	private static final Integer NODE_COUNT = 20;
	private static final Integer VIRTUAL_NODE_COUNT = 160;

	public static void main(String[] args) {
		KetamaNodeLocatorTest test = new KetamaNodeLocatorTest();
		Map<KetamaNode, Integer> nodeRecord = new LinkedHashMap<KetamaNode, Integer>();
		// List<KetamaNode> allNodes = test.getNodes(NODE_COUNT);
		// KetamaNodeLocator locator = new KetamaNodeLocator(allNodes);
		KetamaNodeLocator locator = new KetamaNodeLocator("order_info", NODE_COUNT, VIRTUAL_NODE_COUNT, "_");
		List<String> allKeys = test.getAllStrings();
		for (String key : allKeys) {
			KetamaNode node = locator.getPrimary(key);
			Integer times = nodeRecord.get(node);
			if (times == null) {
				nodeRecord.put(node, 1);
			} else {
				nodeRecord.put(node, times + 1);
			}
		}
		System.out.println("Nodes count : " + NODE_COUNT + ", Keys count : " + EXE_TIMES + ", Normal percent : "
				+ (float) 100 / NODE_COUNT + "%");
		System.out.println("-------------------- boundary  ----------------------");
		for (Map.Entry<KetamaNode, Integer> entry : nodeRecord.entrySet()) {
			System.out.println("Node name :" + entry.getKey() + " - Times : " + entry.getValue() + " - Percent : "
					+ (float) entry.getValue() / EXE_TIMES * 100 + "%");
		}

	}

	public List<KetamaNode> getNodes(int nodeCount) {
		List<KetamaNode> nodes = new ArrayList<KetamaNode>();

		for (int k = 1; k <= nodeCount; k++) {
			KetamaNode node = new KetamaNode("order_info" + k, "order_info" + k);
			nodes.add(node);
		}

		return nodes;
	}

	private List<String> getAllStrings() {
		List<String> allStrings = new ArrayList<String>(EXE_TIMES);

		for (int i = 0; i < EXE_TIMES; i++) {
			allStrings.add(generateRandomString(ran.nextInt(50)));
		}

		return allStrings;
	}

	private String generateRandomString(int length) {
		StringBuffer sb = new StringBuffer(length);

		for (int i = 0; i < length; i++) {
			sb.append((char) (ran.nextInt(95) + 32));
		}

		return sb.toString();
	}
}
