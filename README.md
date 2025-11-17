# 🍔 在线点餐系统

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.13-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

一个基于Spring Boot和原生HTML的现代化在线点餐系统，提供完整的用户端和商家端功能。采用前后端分离架构，具有响应式设计和良好的用户体验。

## ✨ 功能特性

### 🎯 核心功能
- **用户管理**: 注册、登录、个人信息管理
- **商品展示**: 分类浏览、搜索、详情查看
- **购物车**: 添加商品、数量调整、实时计算
- **订单系统**: 下单、支付、状态跟踪、历史查询
- **管理后台**: 商品管理、分类管理、订单处理、数据统计

### 🚀 技术亮点
- **防重复提交**: 基于JavaScript的表单重复提交保护
- **加载提示**: 统一的LoadingManager提升用户体验
- **响应式设计**: 适配桌面和移动设备
- **RESTful API**: 标准化的接口设计
- **数据验证**: 前后端双重验证保证数据安全

## 🛠 技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.6.13 | 核心框架，提供自动配置和快速开发 |
| Spring Data JPA | - | 数据访问层，简化数据库操作 |
| MySQL | 8.0+ | 关系型数据库，存储业务数据 |
| Maven | 3.6+ | 项目构建和依赖管理 |

### 前端技术
| 技术 | 说明 |
|------|------|
| HTML5 | 语义化页面结构 |
| CSS3 | 现代化样式设计，支持响应式布局 |
| JavaScript ES6+ | 交互逻辑和动态功能 |
| Fetch API | 现代化的HTTP请求方式 |

### 开发工具
- **IDE**: IntelliJ IDEA / Eclipse
- **版本控制**: Git
- **API测试**: Postman / 内置测试页面

## 📁 项目结构

```
OrderingSystem/
├── 📄 README.md                    # 项目说明文档
├── 📄 pom.xml                      # Maven项目配置
├── 📄 .gitignore                   # Git忽略文件配置
│
├── 📂 src/main/java/com/order/     # Java源代码目录
│   ├── 📂 controller/              # 控制器层 - 处理HTTP请求
│   │   ├── 📄 UserController.java
│   │   ├── 📄 ProductController.java
│   │   ├── 📄 CategoryController.java
│   │   ├── 📄 ShoppingCartController.java
│   │   └── 📄 OrderController.java
│   │
│   ├── 📂 service/                 # 业务逻辑层 - 核心业务处理
│   │   ├── 📄 UserService.java
│   │   ├── 📄 ProductService.java
│   │   ├── 📄 CategoryService.java
│   │   ├── 📄 ShoppingCartService.java
│   │   └── 📄 OrderService.java
│   │
│   ├── 📂 repository/              # 数据访问层 - 数据库操作
│   │   ├── 📄 UserRepository.java
│   │   ├── 📄 ProductRepository.java
│   │   ├── 📄 CategoryRepository.java
│   │   ├── 📄 ShoppingCartRepository.java
│   │   └── 📄 OrderRepository.java
│   │
│   ├── 📂 entity/                  # 实体类 - 数据模型
│   │   ├── 📄 User.java
│   │   ├── 📄 Product.java
│   │   ├── 📄 Category.java
│   │   ├── 📄 ShoppingCart.java
│   │   └── 📄 Order.java
│   │
│   ├── 📂 common/                  # 通用工具类
│   │   └── 📄 Result.java          # 统一响应结果封装
│   │
│   └── 📂 config/                  # 配置类
│       ├── 📄 CorsConfig.java      # 跨域配置
│       └── 📄 DatabaseConfig.java   # 数据库配置
│
├── 📂 src/main/resources/          # 资源文件目录
│   ├── 📂 static/                  # 静态资源
│   │   ├── 📄 index.html           # 🏠 首页 - 商品展示
│   │   ├── 📄 cart.html            # 🛒 购物车页面
│   │   ├── 📄 orders.html          # 📋 订单管理页面
│   │   ├── 📄 admin.html           # 👨‍💼 管理后台页面
│   │   ├── 📄 test.html            # 🧪 API测试页面
│   │   └── 📂 js/                  # JavaScript文件
│   │       ├── 📄 api.js           # API接口封装
│   │       ├── 📄 utils.js         # 工具函数
│   │       └── 📄 managers.js      # 加载和提交管理器
│   │
│   ├── 📂 db/                      # 数据库相关
│   │   └── 📄 init.sql             # 数据库初始化脚本
│   │
│   └── 📄 application.properties   # 📋 应用配置文件
│
└── 📂 src/test/java/               # 测试代码目录
```

## 🚀 快速开始

### 📋 环境要求
| 组件 | 最低版本 | 推荐版本 |
|------|----------|----------|
| JDK | 11 | 17 |
| MySQL | 8.0 | 8.0+ |
| Maven | 3.6 | 3.8+ |

### 🗄️ 数据库配置

1. **创建数据库**
```sql
-- 创建数据库
CREATE DATABASE ordering_system 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE ordering_system;
```

2. **配置连接信息**

编辑 `src/main/resources/application.properties`：

```properties
# 数据库连接配置
spring.datasource.url=jdbc:mysql://localhost:3306/ordering_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# 服务器配置
server.port=8080
server.servlet.context-path=/
```

### ▶️ 运行应用

#### 方式一：使用Maven命令
```bash
# 1. 进入项目目录
cd OrderingSystem

# 2. 编译项目
mvn clean compile

# 3. 启动应用
mvn spring-boot:run
```

#### 方式二：使用IDE
1. 在IDE中导入Maven项目
2. 找到主启动类 `OrderingSystemApplication.java`
3. 右键选择 "Run" 或 "Debug"

#### 方式三：打包运行
```bash
# 打包项目
mvn clean package

# 运行jar包
java -jar target/ordering-system-1.0.0.jar
```

### 🌐 访问应用

启动成功后，在浏览器中访问：

| 页面 | URL | 说明 |
|------|-----|------|
| 🏠 首页 | http://localhost:8080/index.html | 商品浏览和购买 |
| 🛒 购物车 | http://localhost:8080/cart.html | 购物车管理 |
| 📋 订单 | http://localhost:8080/orders.html | 订单查看和管理 |
| 👨‍💼 管理后台 | http://localhost:8080/admin.html | 商品和订单管理 |
| 🧪 API测试 | http://localhost:8080/test.html | 接口测试工具 |

## 📚 API接口文档

### 🔐 用户相关接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| POST | `/api/user/register` | 用户注册 | username, password, phone, nickname |
| POST | `/api/user/login` | 用户登录 | username, password |
| GET | `/api/user/{id}` | 获取用户信息 | 路径参数：用户ID |
| PUT | `/api/user/{id}` | 更新用户信息 | 用户信息JSON |

### 🍔 商品相关接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/product/list` | 获取商品列表 | 可选：categoryId, keyword |
| GET | `/api/product/{id}` | 获取商品详情 | 路径参数：商品ID |
| GET | `/api/product/available` | 获取可用商品 | 无 |
| POST | `/api/product/add` | 添加商品 | 商品信息JSON |
| PUT | `/api/product/{id}` | 更新商品 | 商品信息JSON |
| DELETE | `/api/product/{id}` | 删除商品 | 路径参数：商品ID |

### 🏷️ 分类相关接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/category/list` | 获取分类列表 | 无 |
| GET | `/api/category/{id}` | 获取分类详情 | 路径参数：分类ID |
| POST | `/api/category/add` | 添加分类 | 分类信息JSON |
| PUT | `/api/category/{id}` | 更新分类 | 分类信息JSON |
| DELETE | `/api/category/{id}` | 删除分类 | 路径参数：分类ID |

### 🛒 购物车相关接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/cart/user/{userId}` | 获取用户购物车 | 路径参数：用户ID |
| POST | `/api/cart/add` | 添加商品到购物车 | userId, productId, quantity |
| PUT | `/api/cart/update` | 更新购物车商品数量 | cartId, quantity |
| DELETE | `/api/cart/remove` | 移除购物车商品 | cartId |
| DELETE | `/api/cart/clear/{userId}` | 清空购物车 | 路径参数：用户ID |

### 📋 订单相关接口
| 方法 | 路径 | 说明 | 参数 |
|------|------|------|------|
| GET | `/api/order/user/{userId}` | 获取用户订单 | 路径参数：用户ID |
| GET | `/api/order/{orderId}` | 获取订单详情 | 路径参数：订单ID |
| POST | `/api/order/create` | 创建订单 | 订单信息JSON |
| PUT | `/api/order/{orderId}/cancel` | 取消订单 | 路径参数：订单ID |
| PUT | `/api/order/{orderId}/pay` | 支付订单 | 路径参数：订单ID |
| PUT | `/api/order/{orderId}/complete` | 完成订单 | 路径参数：订单ID |

## 🗄️ 数据库设计

### 👤 用户表 (users)
| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | AUTO_INCREMENT |
| username | VARCHAR(50) | 用户名 | UNIQUE, NOT NULL |
| password | VARCHAR(100) | 密码 | NOT NULL |
| phone | VARCHAR(20) | 手机号 | UNIQUE |
| nickname | VARCHAR(50) | 昵称 | |
| avatar | VARCHAR(255) | 头像URL | |
| email | VARCHAR(100) | 邮箱 | |
| address | TEXT | 地址 | |
| status | TINYINT | 状态 | DEFAULT 1 |
| created_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| updated_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

### 🍔 商品表 (products)
| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | AUTO_INCREMENT |
| name | VARCHAR(100) | 商品名称 | NOT NULL |
| description | TEXT | 商品描述 | |
| price | DECIMAL(10,2) | 价格 | NOT NULL |
| original_price | DECIMAL(10,2) | 原价 | |
| image | VARCHAR(255) | 商品图片URL | |
| category_id | BIGINT | 分类ID | FOREIGN KEY |
| stock | INT | 库存数量 | DEFAULT 0 |
| status | TINYINT | 状态 | DEFAULT 1 |
| sort_order | INT | 排序 | DEFAULT 0 |
| created_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| updated_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

### 🏷️ 分类表 (categories)
| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | AUTO_INCREMENT |
| name | VARCHAR(50) | 分类名称 | NOT NULL |
| description | TEXT | 分类描述 | |
| sort_order | INT | 排序 | DEFAULT 0 |
| status | TINYINT | 状态 | DEFAULT 1 |
| created_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| updated_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

### 🛒 购物车表 (shopping_cart)
| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | AUTO_INCREMENT |
| user_id | BIGINT | 用户ID | FOREIGN KEY |
| product_id | BIGINT | 商品ID | FOREIGN KEY |
| quantity | INT | 商品数量 | DEFAULT 1 |
| created_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| updated_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

### 📋 订单表 (orders)
| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | AUTO_INCREMENT |
| order_no | VARCHAR(50) | 订单号 | UNIQUE, NOT NULL |
| user_id | BIGINT | 用户ID | FOREIGN KEY |
| total_amount | DECIMAL(10,2) | 订单总金额 | NOT NULL |
| status | VARCHAR(20) | 订单状态 | DEFAULT 'pending' |
| remark | TEXT | 订单备注 | |
| delivery_address | TEXT | 配送地址 | |
| contact_name | VARCHAR(50) | 联系人姓名 | |
| contact_phone | VARCHAR(20) | 联系电话 | |
| created_time | DATETIME | 创建时间 | DEFAULT CURRENT_TIMESTAMP |
| updated_time | DATETIME | 更新时间 | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP |

### 📦 订单项表 (order_items)
| 字段 | 类型 | 说明 | 约束 |
|------|------|------|------|
| id | BIGINT | 主键 | AUTO_INCREMENT |
| order_id | BIGINT | 订单ID | FOREIGN KEY |
| product_id | BIGINT | 商品ID | FOREIGN KEY |
| product_name | VARCHAR(100) | 商品名称 | |
| product_price | DECIMAL(10,2) | 商品价格 | |
| quantity | INT | 商品数量 | |
| subtotal | DECIMAL(10,2) | 小计金额 | |
| image | VARCHAR(255) | 商品图片 | |

## 📖 使用指南

### 👤 用户端操作流程

1. **🏠 浏览商品**
   - 访问首页查看所有商品
   - 使用分类筛选快速找到目标商品
   - 通过搜索功能查找特定商品

2. **🛒 购物车管理**
   - 点击"加入购物车"添加商品
   - 在购物车页面调整商品数量
   - 实时查看总价计算

3. **📋 下单流程**
   - 确认购物车商品
   - 填写配送信息
   - 选择支付方式并确认下单

4. **📦 订单跟踪**
   - 在订单页面查看所有历史订单
   - 实时跟踪订单状态变化
   - 进行支付、取消、确认收货等操作

### 👨‍💼 管理员操作流程

1. **📊 数据概览**
   - 查看系统总体统计数据
   - 监控商品、订单、用户数量
   - 分析营收情况

2. **🍔 商品管理**
   - 添加新商品并设置详细信息
   - 编辑现有商品信息
   - 管理商品上下架状态
   - 调整商品库存

3. **🏷️ 分类管理**
   - 创建商品分类
   - 编辑分类信息
   - 管理分类显示顺序

4. **📋 订单处理**
   - 查看所有用户订单
   - 更新订单状态（接单、拒绝、配送等）
   - 处理退款和售后

## ⚙️ 部署指南

### 🐳 Docker部署（推荐）

1. **创建Dockerfile**
```dockerfile
FROM openjdk:11-jre-slim
COPY target/ordering-system-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

2. **构建镜像**
```bash
docker build -t ordering-system .
```

3. **运行容器**
```bash
docker run -d -p 8080:8080 --name ordering-system ordering-system
```

### 🐙 Docker Compose部署

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/ordering_system
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=rootpassword
    depends_on:
      - db
    networks:
      - app-network

  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: ordering_system
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  mysql_data:
```

### 🚀 传统部署

1. **环境准备**
```bash
# 安装Java 11+
sudo yum install java-11-openjdk-devel

# 安装MySQL 8.0+
sudo yum install mysql-server

# 安装Maven
sudo yum install maven
```

2. **数据库配置**
```bash
# 启动MySQL
sudo systemctl start mysqld

# 创建数据库和用户
mysql -u root -p
```

3. **应用部署**
```bash
# 打包应用
mvn clean package -DskipTests

# 后台运行
nohup java -jar target/ordering-system-1.0.0.jar > app.log 2>&1 &
```

## 🔧 配置说明

### 📋 application.properties 详细配置

```properties
# ========================================
# 数据库配置
# ========================================
spring.datasource.url=jdbc:mysql://localhost:3306/ordering_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ========================================
# JPA配置
# ========================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# ========================================
# 服务器配置
# ========================================
server.port=8080
server.servlet.context-path=/
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.enabled=true
server.servlet.encoding.force=true

# ========================================
# 日志配置
# ========================================
logging.level.com.order=DEBUG
logging.level.org.springframework.web=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.file.path=logs/
logging.file.name=logs/ordering-system.log

# ========================================
# 文件上传配置
# ========================================
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# ========================================
# 跨域配置
# ========================================
spring.mvc.cors.allowed-origins=http://localhost:8080
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.mvc.cors.allowed-headers=*
spring.mvc.cors.allow-credentials=true
```

## 🧪 测试

### 📋 单元测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=UserServiceTest

# 运行测试并生成报告
mvn test jacoco:report
```

### 🔗 API测试
使用内置的测试页面 (http://localhost:8080/test.html) 或Postman进行API测试。

### 📊 性能测试
推荐使用Apache JMeter或Gatling进行压力测试。

## 🔒 安全考虑

### 🛡️ 当前安全措施
- **输入验证**: 前后端双重数据验证
- **SQL注入防护**: 使用JPA预编译语句
- **XSS防护**: HTML转义处理
- **CSRF防护**: 同源策略保护

### 🔐 建议改进
- [ ] 实现JWT令牌认证
- [ ] 添加密码加密存储（BCrypt）
- [ ] 实现角色权限控制
- [ ] 添加API访问频率限制
- [ ] 实现HTTPS安全传输
- [ ] 添加日志审计功能

## 🐛 故障排除

### 常见问题及解决方案

| 问题 | 可能原因 | 解决方案 |
|------|----------|----------|
| 数据库连接失败 | 数据库未启动或配置错误 | 检查MySQL服务状态和连接配置 |
| 端口占用 | 8080端口被其他程序占用 | 修改server.port配置或关闭占用程序 |
| 静态资源404 | 路径配置错误 | 检查static目录位置和访问路径 |
| 跨域问题 | 前后端分离部署 | 配置CORS或使用代理服务器 |
| 内存不足 | JVM堆内存太小 | 调整JVM参数 -Xmx |

### 📝 日志查看
```bash
# 查看应用日志
tail -f logs/ordering-system.log

# 查看错误日志
grep ERROR logs/ordering-system.log

# 实时监控
tail -f logs/ordering-system.log | grep ERROR
```

## 🚀 性能优化

### 📊 数据库优化
- 添加适当的索引
- 优化查询语句
- 使用连接池
- 实现读写分离

### ⚡ 应用优化
- 启用缓存机制
- 使用CDN加速静态资源
- 实现API响应缓存
- 优化图片资源

### 🔧 JVM优化
```bash
# 生产环境JVM参数建议
-Xms2g -Xmx4g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+PrintGCDetails
-XX:+PrintGCTimeStamps
```

## 🤝 贡献指南

### 📋 开发流程
1. Fork项目到个人仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建Pull Request

### 📝 代码规范
- 使用统一的代码格式化配置
- 遵循Java编码规范
- 添加必要的注释和文档
- 编写单元测试

### 🐛 Bug报告
使用GitHub Issues提交bug报告，请包含：
- 详细的问题描述
- 重现步骤
- 环境信息
- 相关日志

## 📄 许可证

本项目采用 [MIT License](LICENSE) 开源协议。

## 🙏 致谢

感谢以下开源项目和工具：
- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java开发框架
- [MySQL](https://www.mysql.com/) - 可靠的关系型数据库
- [Bootstrap](https://getbootstrap.com/) - 响应式CSS框架
- [Font Awesome](https://fontawesome.com/) - 优秀的图标库


⭐ 如果这个项目对您有帮助，请给我们一个Star！