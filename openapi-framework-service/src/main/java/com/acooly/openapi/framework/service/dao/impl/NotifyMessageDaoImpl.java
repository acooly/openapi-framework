/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月20日
 *
 */
package com.acooly.openapi.framework.service.dao.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.Strings;
import com.acooly.module.ds.AbstractJdbcTemplateDao;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.enums.MessageType;
import com.acooly.openapi.framework.common.enums.TaskExecuteStatus;
import com.acooly.openapi.framework.common.enums.TaskStatus;
import com.acooly.openapi.framework.service.dao.NotifyMessageDao;
import com.acooly.openapi.framework.service.domain.NotifyMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangpu
 */
@Repository
public class NotifyMessageDaoImpl extends AbstractJdbcTemplateDao implements NotifyMessageDao {

    private static final String TABLE_NAME = "api_notify_message";
    private static TypeReference<Map<String, String>> mapTypeReference =
            new TypeReference<Map<String, String>>() {
            };
    private RowMapper<NotifyMessage> rowMapper =
            new RowMapper<NotifyMessage>() {
                @Override
                public NotifyMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    NotifyMessage notifyMessage = new NotifyMessage();
                    notifyMessage.setId(rs.getLong(1));
                    notifyMessage.setGid(rs.getString(2));
                    notifyMessage.setPartnerId(rs.getString(3));
                    notifyMessage.setMessageType(MessageType.valueOf(rs.getString(4)));
                    notifyMessage.setService(rs.getString(5));
                    notifyMessage.setVersion(rs.getString(6));
                    notifyMessage.setUrl(rs.getString(7));
                    notifyMessage.setContent(rs.getString(8));
                    notifyMessage.setParameters(
                            JSON.parseObject(notifyMessage.getContent(), mapTypeReference));
                    notifyMessage.setSendCount(rs.getInt(9));
                    notifyMessage.setNextSendTime(rs.getTimestamp(10));
                    notifyMessage.setStatus(TaskStatus.valueOf(rs.getString(11)));
                    notifyMessage.setExecuteStatus(TaskExecuteStatus.valueOf(rs.getString(12)));
                    notifyMessage.setCreateTime(rs.getTimestamp(13));
                    notifyMessage.setUpdateTime(rs.getTimestamp(14));
                    notifyMessage.setRespInfo(rs.getString(15));
                    notifyMessage.setRequestNo(rs.getString(16));
                    notifyMessage.setMerchOrderNo(rs.getString(17));
                    String protocol = rs.getString(18);
                    notifyMessage.setProtocol(Strings.isNotBlank(protocol) ? ApiProtocol.valueOf(protocol) : ApiProtocol.JSON);
                    notifyMessage.setSignType(rs.getString(19));
                    notifyMessage.setSign(rs.getString(20));
                    return notifyMessage;
                }
            };

    private String getSelectSql() {
        return "select id,gid,partner_id as partnerId,message_type as messageType,service,version,url,content,send_count as sendCount,"
                + "next_send_time as nextSendTime,status,execute_status as executeStatus,create_time as createTime,update_time as updateTime,"
                + "resp_info as respInfo,request_no as requestNo, merch_order_no as merchOrderNo,protocol,sign_type as signType,sign from "
                + TABLE_NAME;
    }

    @Override
    public PageInfo<NotifyMessage> query(
            PageInfo<NotifyMessage> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap) {

        StringBuilder sb = new StringBuilder();
        sb.append(getSelectSql()).append(" where 1 = 1");

        String gid = (String) map.get("LLIKE_gid");
        if (Strings.isNotBlank(gid)) {
            sb.append(" and gid like '").append(gid).append("%'");
        }
        String partnerId = (String) map.get("EQ_partnerId");
        if (Strings.isNotBlank(partnerId)) {
            sb.append(" and partner_id = '").append(partnerId).append("'");
        }
        String requestNo = (String) map.get("LLIKE_requestNo");
        if (Strings.isNotBlank(requestNo)) {
            sb.append(" and request_no like '").append(requestNo).append("%'");
        }
        String merchOrderNo = (String) map.get("LLIKE_merchOrderNo");
        if (Strings.isNotBlank(merchOrderNo)) {
            sb.append(" and merch_order_no like '").append(merchOrderNo).append("%'");
        }
        String status = (String) map.get("EQ_status");
        if (Strings.isNotBlank(status)) {
            sb.append(" and status = '").append(status).append("'");
        }
        String executeStatus = (String) map.get("EQ_executeStatus");
        if (Strings.isNotBlank(executeStatus)) {
            sb.append(" and execute_status = '").append(executeStatus).append("'");
        }

        if (orderMap == null || orderMap.size() == 0) {
            sb.append(" order by id desc");
        } else {
            sb.append(" order by ");
            for (Map.Entry<String, Boolean> entry : orderMap.entrySet()) {
                sb.append(" " + entry.getKey() + " " + (entry.getValue() ? "ase" : "desc") + " ");
            }
        }

        return query(pageInfo, sb.toString(), NotifyMessage.class);
    }

    @Override
    public NotifyMessage get(Long id) {
        String sql = getSelectSql() + " where id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public void insert(final NotifyMessage notifyMessage) {
        final String sql =
                "insert into "
                        + TABLE_NAME
                        + "("
                        + (getDbType() == DbType.oracle ? "id," : "")
                        + "gid,partner_id,message_type,service,version,url,content,send_count,next_send_time,status,execute_status,"
                        + "create_time,update_time,resp_info,request_no,merch_order_no,protocol,sign_type,sign) "
                        + " values("
                        + (getDbType() == DbType.oracle ? "seq_api_notify_message.nextval," : "")
                        + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps = con.prepareStatement(sql, new int[]{1});
                        ps.setString(1, notifyMessage.getGid());
                        ps.setString(2, notifyMessage.getPartnerId());
                        ps.setString(3, notifyMessage.getMessageType().code());
                        ps.setString(4, notifyMessage.getService());
                        ps.setString(5, notifyMessage.getVersion());
                        ps.setString(6, notifyMessage.getUrl());
                        ps.setString(7, notifyMessage.getContent());
                        ps.setInt(8, notifyMessage.getSendCount());
                        ps.setTimestamp(9, null);
                        ps.setString(10, notifyMessage.getStatus().code());
                        ps.setString(11, notifyMessage.getExecuteStatus().code());
                        Date now = new Date();
                        ps.setTimestamp(12, new Timestamp(now.getTime()));
                        ps.setTimestamp(13, new Timestamp(now.getTime()));
                        if (Strings.isBlank(notifyMessage.getRespInfo())) {
                            ps.setNull(14, Types.VARCHAR);
                        } else {
                            ps.setString(14, HtmlUtils.htmlEscape(notifyMessage.getRespInfo()));
                        }

                        ps.setString(15, notifyMessage.getRequestNo());
                        ps.setString(16, notifyMessage.getMerchOrderNo());
                        ps.setString(17, notifyMessage.getProtocol().code());
                        ps.setString(18, notifyMessage.getSignType());
                        ps.setString(19, notifyMessage.getSign());
                        return ps;
                    }
                },
                keyHolder);
        Long generatedId = keyHolder.getKey().longValue();
        notifyMessage.setId(generatedId);
    }

    @Override
    public void updateStatus(NotifyMessage notifyMessage) {
        String sql =
                "update "
                        + TABLE_NAME
                        + " set update_time = "
                        + getSqlDate()
                        + " ,status = ?,execute_status = ?,send_count = ?,next_send_time=?, resp_info=?"
                        + " where id = ?";
        jdbcTemplate.update(
                sql,
                notifyMessage.getStatus().code(),
                notifyMessage.getExecuteStatus().code(),
                notifyMessage.getSendCount(),
                notifyMessage.getNextSendTime(),
                notifyMessage.getRespInfo(),
                notifyMessage.getId());
    }

    @Override
    public void updateForManage(NotifyMessage notifyMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(TABLE_NAME).append(" set update_time = ").append(getSqlDate());
        sb.append(", url=?").append(",status=?").append(",next_send_time=?").append(" where id=?");
        jdbcTemplate.update(
                sb.toString(),
                notifyMessage.getUrl(),
                notifyMessage.getStatus().code(),
                notifyMessage.getNextSendTime(),
                notifyMessage.getId());
    }

    protected String getSqlDate() {
        return getDbType() == DbType.mysql ? "now()" : "sysdate";
    }

    @Override
    public int updateProccessingStatus(NotifyMessage notifyMessage) {
        String sql =
                "update "
                        + TABLE_NAME
                        + " set update_time = "
                        + (getDbType() == DbType.mysql ? "now()" : "sysdate")
                        + " ,execute_status = '"
                        + TaskExecuteStatus.Processing.code()
                        + "' where id = ? and status = '"
                        + TaskStatus.Waitting.code()
                        + "' and execute_status = '"
                        + TaskExecuteStatus.Unprocessed.code()
                        + "'";
        return jdbcTemplate.update(sql, notifyMessage.getId());
    }

    @Override
    public void updateProccessingStatus(List<NotifyMessage> notifyMessages) {

        List<Long> ids = Lists.newArrayList();
        for (NotifyMessage nm : notifyMessages) {
            ids.add(nm.getId());
        }
        String idsStr = Strings.replace(ids.toString(), "[", "(");
        idsStr = Strings.replace(idsStr, "]", ")");
        String sql =
                "update "
                        + TABLE_NAME
                        + " set update_time = "
                        + (getDbType() == DbType.mysql ? "now()" : "sysdate")
                        + " ,execute_status = '"
                        + TaskExecuteStatus.Processing.code()
                        + "' where id in "
                        + idsStr;
        jdbcTemplate.update(sql);
    }

    @Override
    public List<NotifyMessage> listUnProcessed(Integer topNum) {
        String sql =
                getSelectSql()
                        + " where status = '"
                        + TaskStatus.Waitting.code()
                        + "' and execute_status = '"
                        + TaskExecuteStatus.Unprocessed.code()
                        + "' and next_send_time<=now() order by id";
        if (getDbType() == DbType.mysql) {
            sql += " limit 0,5";
        } else if (getDbType() == DbType.oracle) {
            sql = "select * from (" + sql + ") where ROWNUM <= " + topNum;
        }
        return jdbcTemplate.query(sql, new Object[]{}, rowMapper);
    }
}
