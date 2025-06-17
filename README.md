github: https://github.com/yanghua021021/ap-product-trading-service   

1.代码工程结构:   
src/main/java   
├── db  // 数据库初始化建库脚本   
├── main   
│   ├── java     
│   │   ├── com.test.product.trading   
│   │   │   ├── common      // 公共领域   
│   │   │   │   ├── config     // 配置   
│   │   │   │   ├── constant   // 常量   
│   │   │   │   ├── enums      // 枚举   
│   │   │   │   ├── exception  // 异常   
│   │   │   │   ├── injector   // 注入器   
│   │   │   │   ├── rsp        // 返回   
│   │   │   │   ├── tool       // 工具   
│   │   │   ├── merchant    // 商家领域   
│   │   │   │   ├── controller // REST API   
│   │   │   │   ├── entity     // 实体   
│   │   │   │   ├── job        // 定时任务   
│   │   │   │   ├── mapper     // mapper   
│   │   │   │   └── service    // 服务层   
│   │   │   ├── order       // 订单领域   
│   │   │   │   ├── controller // REST API   
│   │   │   │   ├── entity     // 实体   
│   │   │   │   ├── mapper     // mapper   
│   │   │   │   └── service    // 服务层   
│   │   │   ├── user        // 用户领域   
│   │   │   │   ├── controller // REST API   
│   │   │   │   ├── entity     // 实体   
│   │   │   │   ├── mapper     // mapper   
│   │   │   │   └── service    // 服务层   
│   │   │   ├── BootApplication // 启动类   
│   ├── resources   // 配置文件   
│   │   ├── mapper  // mybatis mapper xml文件   
├── test   
└── └── java        // junit测试代码   
pom.xml   
README.md   
   
2.数据库初始化:   
本次数据库采用PostgreSQL 15.2   
首先建立一个数据库，例如:    
psql -h 127.0.0.1 -p 5432 -U postgres -d postgres -c "CREATE DATABASE testdb WITH ENCODING 'UTF8';"   
然后将src/db目录下的ddl.sql导入数据库，例如:    
psql -h 127.0.0.1 -p 5432 -U postgres -d testdb -f ddl.sql   
   
3.代码工程构建（本次采用JDK17版本）:   
mvn clean package -Dmaven.test.skip=true   
   
4.运行:   
nohup java -jar target/product-trading-service.jar  2>&1 &   
   
5.添加商户（请根据实际环境替换IP地址）:   
接口url: http://127.0.0.1:17560/merchant/info/add/v1   
接口类型: POST   
入参说明: merchantNumber:商家手机号码, merchantName:商家名称   
入参示例: {"merchantNumber": "13910001001","merchantName": "小张的大店铺"}   
   
6.添加商品数量（请根据实际环境替换IP地址）:   
接口url: http://127.0.0.1:17560/product/stock/add/v1   
接口类型: POST   
入参说明: merchantNumber:商家手机号码, sku:商品sku, name:商品名称, price:商品单价, productQty:商品数量   
入参示例: {"merchantNumber":"13910001001", "sku":"Nike-AJ1-38-WHITE", "name":"Nike Air Jordan 第1代篮球鞋 38码 白色", "price":1299.99, "productQty":10}   
   
7.添加用户（请根据实际环境替换IP地址）:   
接口url: http://127.0.0.1:17560/user/info/add/v1   
接口类型: POST   
入参说明: userNumber:用户手机号码, userName:用户名称   
入参示例: {"userNumber": "13920002002","userName": "王小虎"}   
   
8.用户账户充值（请根据实际环境替换IP地址）:   
接口url: http://127.0.0.1:17560/user/account/recharge/v1   
接口类型: POST   
入参说明: userNumber:用户手机号码, amount:充值金额   
入参示例: {"userNumber": "13920002002","amount": 1000}   
   
9.用户购买商品（请根据实际环境替换IP地址）:   
接口url: http://127.0.0.1:17560/order/product/buy/v1   
接口类型: POST   
入参说明: userNumber:用户手机号码, merchantNumber:商家手机号码, sku:商品sku, productQty:购买商品数量   
入参示例: {"userNumber": "13920002002","merchantNumber": "13910001001", "sku":"Nike-AJ1-38-WHITE", "productQty":1}   
   
10.商家每日定时结算job:   
com.test.product.trading.merchant.job.MerchantScheduledJob.clearBatch()    
此方法通过@Scheduled注解来定时运行，定时运行时间可修改application-test.properties配置文件中的配置项:   
merchant.scheduled.clear.time.scheduled=0 0 1 * * ?   
