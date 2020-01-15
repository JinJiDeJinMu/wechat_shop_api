package com.chundengtai.base.tool;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generator {
    public final static String DB_CONNECTION = "jdbc:mysql://localhost:3306/final?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&allowMultiQueries=true";
    public final static String DB_USER_NAME = "root";
    public final static String DB_PWD = "";
    public final static String SYS_PACKAGE_NAME = "com.chundengtai.base";
    public final static String SYS_AHURTOR = "Royal";

    public static void main(String[] args) {
        String[] tableNames = new String[]{"cdt_user_score", "cdt_score", "cdt_score_flow"};
        String[] modules = new String[]{"service", "web"};//项目模块名，需自定义
        for (String module : modules) {
            moduleGenerator(module, tableNames);
        }
    }

    private static void moduleGenerator(String module, String[] tableNames) {
        GlobalConfig globalConfig = getGlobalConfig(module);// 全局配置
        DataSourceConfig dataSourceConfig = getDataSourceConfig();// 数据源配置
        PackageConfig packageConfig = getPackageConfig(module);// 包配置
        StrategyConfig strategyConfig = getStrategyConfig(tableNames);// 策略配置
        TemplateConfig templateConfig = getTemplateConfig(module);// 配置模板

        InjectionConfig cfg= genCustomerTemplate();
        new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setPackageInfo(packageConfig)
                .setStrategy(strategyConfig)
                .setTemplate(templateConfig)
                .setCfg(cfg)
                .execute();

    }

    private static InjectionConfig genCustomerTemplate() {
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】  ${cfg.abc}
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc" , this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };

        String htmlTemplate = "/template/adminhtml.vm";
//        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(htmlTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return System.getProperty("user.dir") + "/src/main/html/" + tableInfo.getEntityName() + ".html";
            }
        });

        String jsTemplate = "/template/elementui.js.vm";
        focList.add(new FileOutConfig(jsTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return System.getProperty("user.dir") + "/src/main/js/" + tableInfo.getEntityName() + ".js";
            }
        });

        String adminTemplate = "/template/adminController.java.vm";
        focList.add(new FileOutConfig(adminTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return System.getProperty("user.dir") + "/src/main/admin/" + tableInfo.getControllerName() + ".java";
            }
        });

        String sqlTemplate = "/template/menu.sql.vm";
        focList.add(new FileOutConfig(sqlTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return System.getProperty("user.dir") + "/src/main/sql/" + tableInfo.getEntityName() + ".sql";
            }
        });
//        cfg.setFileCreate(new IFileCreate() {
//            @Override
//            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
//                // 判断自定义文件夹是否需要创建
//                checkDir("调用默认方法创建的目录");
//                return false;
//            }
//        });

        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    private static TemplateConfig getTemplateConfig(String module) {
        TemplateConfig templateConfig = new TemplateConfig();
        if ("service".equals(module)) {
            templateConfig.setEntity(new TemplateConfig().getEntity(false))
                    .setMapper(new TemplateConfig().getMapper())//mapper模板采用mybatis-plus自己模板
                    .setXml(new TemplateConfig().getXml())
                    .setService(new TemplateConfig().getService())
                    .setServiceImpl(new TemplateConfig().getServiceImpl())
                    .setController(null);//service模块不生成controller代码
        } else if ("web".equals(module)) {//web模块只生成controller代码
            templateConfig.setEntity(null)
                    .setMapper(null)
                    .setXml(null)
                    .setService(null)
                    .setServiceImpl(null)
                    .setController("/template/Controller.java.vm");
        } else {
            throw new IllegalArgumentException("参数匹配错误，请检查");
        }

        return templateConfig;
    }

    private static StrategyConfig getStrategyConfig(String[] tableNames) {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)//驼峰命名
                .setEntityLombokModel(true)
                .setRestControllerStyle(false)
                .setTablePrefix("nideshop_")
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);
        return strategyConfig;
    }

    private static PackageConfig getPackageConfig(String module) {
        PackageConfig packageConfig = new PackageConfig();
        String packageName = SYS_PACKAGE_NAME;
        if ("service".equals(module)) {
            //packageName += ".web";
        } else if ("web".equals(module)) {

        }
        packageConfig.setParent(packageName)
                .setEntity("bean")
                .setMapper("dao")
                .setService("service")
                .setController("controller");
        return packageConfig;
    }

    private static DataSourceConfig getDataSourceConfig() {
        String dbUrl = DB_CONNECTION;
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        //"com.mysql.cj.jdbc.Driver"
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setDriverName(Driver.class.getName())
                .setUsername(DB_USER_NAME)
                .setPassword(DB_PWD)
                .setUrl(dbUrl);
        return dataSourceConfig;
    }

    private static GlobalConfig getGlobalConfig(String module) {
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOpen(false)//new File(module).getAbsolutePath()得到模块根目录路径，因事Maven项目，代码指定路径自定义调整
                //.setOutputDir(new File(module).getAbsolutePath() + "/src/main/java")//生成文件的输出目录
                .setOutputDir(projectPath + "/src/main/java")//生成文件的输出目录
                .setFileOverride(true)//是否覆盖已有文件
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setActiveRecord(false).setDateType(DateType.ONLY_DATE)
                .setAuthor(SYS_AHURTOR)
                .setServiceName("%sService");
        return globalConfig;
    }

}