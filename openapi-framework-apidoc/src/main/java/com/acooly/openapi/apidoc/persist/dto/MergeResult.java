package com.acooly.openapi.apidoc.persist.dto;

import com.acooly.openapi.apidoc.persist.enums.ApiDocEntityType;
import com.acooly.openapi.apidoc.persist.enums.ApiDocMergeType;
import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangpu@acooly.cn
 * @date 2018-08-12 19:29
 */
@Getter
@Setter
public class MergeResult {


    private ApiDocEntityType entityType;

    private String entityNo;

    private ApiDocMergeType mergeType;

    public MergeResult() {
    }

    public MergeResult(ApiDocEntityType entityType, String entityNo, ApiDocMergeType mergeType) {
        this.entityType = entityType;
        this.entityNo = entityNo;
        this.mergeType = mergeType;
    }

    public static MergeResult messageOf(String entityNo, ApiDocMergeType mergeType) {
        return new MergeResult(ApiDocEntityType.MESSAGE, entityNo, mergeType);
    }

    public static MergeResult serviceOf(String entityNo, ApiDocMergeType mergeType) {
        return new MergeResult(ApiDocEntityType.SERVICE, entityNo, mergeType);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("")
                .add("entityType", entityType)
                .add("entityNo", entityNo)
                .add("mergeType", mergeType)
                .omitNullValues()
                .toString();
    }
}
