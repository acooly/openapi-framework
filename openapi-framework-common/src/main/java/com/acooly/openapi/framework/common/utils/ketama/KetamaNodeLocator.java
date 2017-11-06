/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * zhangpu 2015年8月6日 上午3:32:51 创建
 */
package com.acooly.openapi.framework.common.utils.ketama;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * ketama一致性hash算法定位器
 *
 * @author zhangpu 2015-09-04
 */
public final class KetamaNodeLocator {

  private static TreeMap<Long, KetamaNode> ketamaNodes = new TreeMap<>();
  private String nodeNamePrefix = "";
  private int nodes = 100;
  private int nodeCopies = 160;
  private String nameSplits = "";

  public KetamaNodeLocator(String nodeNamePrefix, int nodes, int nodeCopies, String nameSplits) {
    this.nodeNamePrefix = nodeNamePrefix;
    this.nodes = nodes;
    this.nodeCopies = nodeCopies;
    this.nameSplits = nameSplits;
    List<KetamaNode> kns = new ArrayList<>(nodes);
    for (int i = 1; i <= nodes; i++) {
      kns.add(new KetamaNode(nodeNamePrefix + nameSplits + i));
    }
    init(kns);
  }

  public KetamaNodeLocator(List<KetamaNode> nodes) {
    init(nodes);
  }

  protected void init(List<KetamaNode> nodes) {
    ketamaNodes.clear();
    // 一个节点分散为nodeCopies个子节点，key不同，值都为相同的节点
    for (KetamaNode node : nodes) {
      for (int i = 0; i < nodeCopies / 4; i++) {
        byte[] digest = digestMD5(node.getName() + i);
        for (int h = 0; h < 4; h++) {
          long m = diversifyHash(digest, h);
          ketamaNodes.put(m, node);
        }
      }
    }
  }

  public KetamaNode getPrimary(final String key) {
    byte[] digest = digestMD5(key);
    return getNodeForKey(diversifyHash(digest, 0));
  }

  protected KetamaNode getNodeForKey(long hash) {
    final KetamaNode ketamaNode;
    Long key = hash;
    if (!ketamaNodes.containsKey(key)) {
      key = ketamaNodes.ceilingKey(key);
      if (key == null) {
        key = ketamaNodes.firstKey();
      }
    }
    ketamaNode = ketamaNodes.get(key);
    return ketamaNode;
  }

  /**
   * 计算MD5摘要
   *
   * @param key 字符串源文，这里一般是节点名称为做Key
   * @return MD5摘要byte数组
   */
  private byte[] digestMD5(String key) {
    try {
      byte[] keyBytes = key.getBytes("UTF-8");
      return DigestUtils.md5(keyBytes);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Invalid utf-8 encoding string :" + key);
    }
  }

  /**
   * 分散hash算法
   *
   * @param digest
   * @param nTime
   * @return
   */
  private long diversifyHash(byte[] digest, int nTime) {
    long rv =
        ((long) (digest[3 + nTime * 4] & 0xFF) << 24)
            | ((long) (digest[2 + nTime * 4] & 0xFF) << 16)
            | ((long) (digest[1 + nTime * 4] & 0xFF) << 8)
            | (digest[0 + nTime * 4] & 0xFF);

    return rv & 0xffffffffL;
  }

  public int getNodes() {
    return nodes;
  }

  public String getNameSplits() {
    return nameSplits;
  }

  public int getNodeCopies() {
    return nodeCopies;
  }

  public String getNodeNamePrefix() {
    return nodeNamePrefix;
  }
}
