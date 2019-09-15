/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年3月17日
 *
 */
package com.acooly.openapi.framework.service.dao.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.Strings;
import com.acooly.module.ds.AbstractJdbcTemplateDao;
import com.acooly.openapi.framework.common.dto.OrderDto;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.acooly.openapi.framework.common.utils.Dates;
import com.acooly.openapi.framework.service.dao.OrderInfoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhangpu
 * @date 2014年7月27日
 */
@Repository
public class OrderInfoDaoImpl extends AbstractJdbcTemplateDao implements OrderInfoDao {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoDaoImpl.class);

    private static final String SELECT_FIELDS =
            "id,gid,partner_id as partnerId,service,version,raw_add_time as rawAddTime,"
                    + "raw_update_time as rawUpdateTime,protocol as protocol,notify_url as notifyUrl,return_url as returnUrl,business_info as businessInfo,"
                    + "sign_type as signType, request_no as requestNo,context,access_key as accessKey";
    private RowMapper<OrderDto> rowMapper =
            (rs, rowNum) -> {
                OrderDto orderInfo = new OrderDto();
                orderInfo.setId(rs.getLong(1));
                orderInfo.setGid(rs.getString(2));
                orderInfo.setPartnerId(rs.getString(3));
                orderInfo.setService(rs.getString(4));
                orderInfo.setVersion(rs.getString(5));
                orderInfo.setRawAddTime(rs.getTimestamp(6));
                orderInfo.setRawUpdateTime(rs.getTimestamp(7));
                String protocol = rs.getString(8);
                orderInfo.setProtocol(Strings.isNotBlank(protocol) ? ApiProtocol.valueOf(protocol) : ApiProtocol.JSON);
                orderInfo.setNotifyUrl(rs.getString(9));
                orderInfo.setReturnUrl(rs.getString(10));
                orderInfo.setBusinessInfo(rs.getString(11));
                orderInfo.setSignType(rs.getString(12));
                orderInfo.setRequestNo(rs.getString(13));
                orderInfo.setContext(rs.getString(14));
                orderInfo.setAccessKey(rs.getString(15));
                return orderInfo;
            };

    @Override
    public PageInfo<OrderDto> query(
            PageInfo<OrderDto> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder
                .append("select ")
                .append(SELECT_FIELDS)
                .append(" from ")
                .append(getTable(null))
                .append(" where 1 = 1");
        String partnerId = (String) map.get("EQ_partnerId");
        if (Strings.isNotBlank(partnerId)) {
            sqlBuilder.append(" and partner_id = '" + partnerId + "'");
        }
        String gid = (String) map.get("EQ_gid");
        if (Strings.isNotBlank(gid)) {
            sqlBuilder.append(" and gid = '" + gid + "'");
        }
        String requestNo = (String) map.get("EQ_requestNo");
        if (Strings.isNotBlank(requestNo)) {
            sqlBuilder.append(" and request_no = '" + requestNo + "'");
        }
        String orderNo = (String) map.get("EQ_orderNo");
        if (Strings.isNotBlank(orderNo)) {
            sqlBuilder.append(" and order_no = '" + orderNo + "'");
        }
        String service = (String) map.get("EQ_service");
        if (Strings.isNotBlank(service)) {
            sqlBuilder.append(" and service = '" + service + "'");
        }

        // todo:临时处理方案,后续再抽象类中提供预处理查询模式,通过JDBC驱动层屏蔽数据类型转换
        Date startTime = (Date) map.get("GTE_rawAddTime");
        if (startTime != null) {
            if (getDbType() == DbType.oracle) {
                sqlBuilder.append(
                        " and raw_add_time >= to_date('"
                                + Dates.format(startTime)
                                + "','yyyy-mm-dd hh24:mi:ss'");
            } else {
                sqlBuilder.append(" and raw_add_time >= '" + Dates.format(startTime) + "'");
            }
        }
        Date endTime = (Date) map.get("LTE_rawAddTime");
        if (endTime != null) {
            if (getDbType() == DbType.oracle) {
                sqlBuilder.append(
                        " and raw_add_time <= to_date('" + Dates.format(endTime) + "','yyyy-mm-dd hh24:mi:ss'");
            } else {
                sqlBuilder.append(" and raw_add_time <= '" + Dates.format(endTime) + "'");
            }
        }

        if (orderMap == null || orderMap.size() == 0) {
            sqlBuilder.append(" order by id desc");
        } else {
            sqlBuilder.append(" order by ");
            for (Map.Entry<String, Boolean> entry : orderMap.entrySet()) {
                sqlBuilder.append(" " + entry.getKey() + " " + (entry.getValue() ? "ase" : "desc") + " ");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("query sql: {}, map:{}", sqlBuilder, map);
        }

        return query(pageInfo, sqlBuilder.toString(), OrderDto.class);
    }

    @Override
    public void insert(final OrderDto orderInfo) {
        String sql =
                "insert into "
                        + getTable(orderInfo.getPartnerId())
                        + "("
                        + (getDbType() == DbType.oracle ? "id," : "")
                        + "gid,access_key,partner_id,service,version,notify_url,return_url,business_info,raw_add_time,raw_update_time,sign_type,request_no,context,protocol) "
                        + " values("
                        + (getDbType() == DbType.oracle ? "seq_api_order_info.nextval," : "")
                        + "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
                sql,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setString(1, orderInfo.getGid());
                        ps.setString(2, orderInfo.getAccessKey());
                        ps.setString(3, orderInfo.getPartnerId());
                        ps.setString(4, orderInfo.getService());
                        ps.setString(5, orderInfo.getVersion());
                        ps.setString(6, orderInfo.getNotifyUrl());
                        ps.setString(7, orderInfo.getReturnUrl());
                        ps.setString(8, orderInfo.getBusinessInfo());
                        ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
                        ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
                        ps.setString(11, orderInfo.getSignType());
                        ps.setString(12, orderInfo.getRequestNo());
                        ps.setString(13, orderInfo.getContext());
                        ps.setString(14, orderInfo.getProtocol().code());
                    }
                });
    }

    @Override
    public void update(OrderDto orderInfo) {
        String sql =
                "update "
                        + getTable(orderInfo.getPartnerId())
                        + " set raw_update_time = now() ,notify_url = ?,return_url = ?,business_info = ? "
                        + " where id = ?";
        jdbcTemplate.update(
                sql,
                orderInfo.getNotifyUrl(),
                orderInfo.getReturnUrl(),
                orderInfo.getBusinessInfo(),
                orderInfo.getId());
    }

    @Override
    public int countByPartnerIdAndOrderNo(String partnerId, String requestNo) {
        String sql =
                "select count(*) from " + getTable(partnerId) + " where partner_id = ? and request_no = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, partnerId, requestNo);
    }

    @Override
    public OrderDto findByPartnerIdAndOrderNo(String partnerId, String requestNo) {
        String sql =
                "select "
                        + SELECT_FIELDS
                        + " from "
                        + getTable(partnerId)
                        + " where partner_id = ? and request_no = ?";

        if (logger.isDebugEnabled()) {
            logger.debug("findByPartnerIdAndOrderNo sql: {}", sql);
        }
        return jdbcTemplate.queryForObject(sql, rowMapper, partnerId, requestNo);
    }

    @Deprecated
    @Override
    public OrderDto findByGid(String gid) {
        String sql = "select " + SELECT_FIELDS + " from " + getTable(null) + " where gid = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, gid);
    }

    @Override
    public List<OrderDto> findByGid(String partnerId, String gid) {
        String sql =
                "select "
                        + SELECT_FIELDS
                        + " from "
                        + getTable(partnerId)
                        + " where gid = ? order by id desc";
        if (logger.isDebugEnabled()) {
            logger.debug("findByGid sql: {}, partnerId:{}, gid:{}", sql, partnerId, gid);
        }
        return jdbcTemplate.query(sql, new Object[]{gid}, rowMapper);
    }

    @Override
    public List<OrderDto> findGidByTrade(
            String partnerId, String service, String version, String orderNo) {
        String sql =
                "select "
                        + SELECT_FIELDS
                        + " from "
                        + getTable(partnerId)
                        + " where partner_id = ? and order_no=? and service = ? and version=? ";
        Object[] args = new Object[]{partnerId, orderNo, service, version};
        if (logger.isDebugEnabled()) {
            logger.debug("findGidByTrade sql: {}, args:{}", sql, Arrays.toString(args));
        }
        return jdbcTemplate.query(sql, args, rowMapper);
    }

    protected String getTable(String partnerId) {
        return "api_order_info";
    }
}
