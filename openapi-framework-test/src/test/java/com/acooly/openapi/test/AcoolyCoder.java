
package com.acooly.openapi.test;
import com.acooly.coder.Generator;
import com.acooly.coder.generate.impl.DefaultCodeGenerateService;
import org.apache.commons.lang3.StringUtils;

/**
 * 代码生成工具
 */
public class AcoolyCoder {
    public static void main(String[] args) {
        DefaultCodeGenerateService service = (DefaultCodeGenerateService) Generator.getGenerator();
        //set workspace if possible
        if (StringUtils.isBlank(service.getGenerateConfiguration().getWorkspace())) {
            String workspace = getProjectPath() + "openapi-framework-apidoc";
            service.getGenerateConfiguration().setWorkspace(workspace);
        }
        //set root pacakge if possible
        if (StringUtils.isBlank(service.getGenerateConfiguration().getRootPackage())) {
            service.getGenerateConfiguration().setRootPackage(getRootPackage());
        }
        service.generateTable("api_doc_catalog","api_doc_content");
    }

    public static String getProjectPath() {
        String file = AcoolyCoder.class.getClassLoader().getResource(".").getFile();
        String testModulePath = file.substring(0, file.indexOf("/target/"));
        String projectPath = testModulePath.substring(0, testModulePath.lastIndexOf("/"));
        return projectPath + "/";
    }

    private static String getRootPackage() {
        return "com.acooly.openapi.apidoc.persist";
    }
}
