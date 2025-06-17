package com.test.product.trading.common.tool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class MPCodeGenerator {

    public static void main(String[] args) {

        //模块名，必填
        String moduleName = "trading";

        //需要生成的表名，必填
        List<String> tables = new ArrayList<String>();
        tables.add("prod_united_view_api");

        //去除表前缀，非必填
        //String offSetTablePrefix = "t_";
        String offSetTablePrefix = "";

        //属性值填充策略，非必填
        List<IFill> tableFillList = new ArrayList<>();
        //tableFillList.add(new Property("crtTm", FieldFill.INSERT));
        //tableFillList.add(new Property("modTm", FieldFill.INSERT_UPDATE));

        if (moduleName.length() == 0 || tables.size() == 0) {
            System.out.println("请输入模块名和需要生成的表名!");
            return;
        }

        String projectPath = System.getProperty("user.dir");
        //全局配置
        GlobalConfig config = new GlobalConfig.Builder()
                .author("yh")                                      //作者
                .outputDir(projectPath + "/src/main/java")          //生成文件输出路径(写到java目录)
                .enableSwagger()                                    //开启swagger
                .commentDate("yyyy-MM-dd")                          //注释日期格式
                .fileOverride()                                     //覆盖文件(需谨慎使用)
                .dateType(DateType.ONLY_DATE)                       //设置时间对应类型
                .disableOpenDir()                                   //生成后不要打开目录
                .build();

        //包名配置
        PackageConfig packageConfig = new PackageConfig.Builder()
                .parent("com.test.product")
                .moduleName(moduleName)
                .controller("controller")
                .service("service")
                .serviceImpl("service.impl")
                .entity("common.entity")
                .mapper("mapper")
                .xml("mapper")
                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, projectPath + "/src/main/resources/mapper/" + moduleName))
                .build();

        //策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addTablePrefix(offSetTablePrefix)                  //需要抵消的表前缀
                .addInclude(tables)                                 //设置生成需要映射的表名
                //.enableCapitalMode()                              //策略开启⼤写命名
                .serviceBuilder()                                   //构建Service层
                .formatServiceFileName("%sService")                 //业务层接口类命名
                .formatServiceImplFileName("%sServiceImpl")         //业务层接口实现类命名
                .entityBuilder()                                    //构建实体类
                .columnNaming(NamingStrategy.underline_to_camel)    //字段名驼峰命名
                .naming(NamingStrategy.underline_to_camel)          //表名驼峰命名
                .enableLombok()                                     //添加lombock的getter、setter注解
                .enableChainModel()                                 //启动链式写法@Accessors(chain = true)
                .enableColumnConstant()                             //启动属性转常量功能@FieldNameConstants
                .logicDeleteColumnName("deleted")                   //逻辑删除字段，标记@TableLogic
                .enableTableFieldAnnotation()                       //启动字段注解
                .addTableFills(tableFillList)                       //属性值填充策略
                .controllerBuilder()                                //构建Controller类
                .enableHyphenStyle()                                //映射路径使用连字符格式，而不是驼峰
                .formatFileName("%sController")                     //Controller类命名
                //.superClass()                   //Controller 类继承 BaseController
                .enableRestStyle()                                  //标记@RestController注解
                .mapperBuilder()                                    //构建mapper接口类
                .enableBaseResultMap()                              //生成基本的resultMap
                .formatMapperFileName("%sMapper")                   //Mapper接口类明名
                .superClass(BaseMapper.class)                       //Mapper接口类集成 BaseMapper
                .enableMapperAnnotation()                           //标记@Mapper注解
                .formatXmlFileName("%sMapper")                      //Mapper.xml命名
                .enableBaseColumnList()                             //生成基本的SQL片段
                .build();

        //数据源配置
        DataSourceConfig.Builder dataSourceConfigBuilder = new DataSourceConfig
                .Builder(
                "jdbc:mysql://172.20.101.53:3308/app?characterEncoding=UTF-8&useSSL=false&useUnicode=true&allowPublicKeyRetrieval=true",
                "root",
                "root");

        dataSourceConfigBuilder.typeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String tableField) {
                if (tableField.toLowerCase().contains("date") || tableField.toLowerCase().contains("timestamp") || tableField.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                }
                return super.processTypeConvert(globalConfig, tableField);
            }
        });

        //创建代码生成器对象，加载配置
        AutoGenerator autoGenerator = new AutoGenerator(dataSourceConfigBuilder.build());
        autoGenerator.global(config);
        autoGenerator.packageInfo(packageConfig);
        autoGenerator.strategy(strategyConfig);

        //执行
        autoGenerator.execute();

        System.out.println("========================================  Done 相关代码生成完毕  =====================================");
    }

}
