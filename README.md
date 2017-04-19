# subsidy
![subsidy.svg](https://travis-ci.org/evcar-zd/subsidy.svg?branch=master)

国补指标改进

## 国家政策
+ [关于2016-2020年新能源汽车推广应用财政支持政策的通知 财建[2015]134号](http://jjs.mof.gov.cn/zhengwuxinxi/zhengcefagui/201504/t20150429_1224515.html)
+ [关于调整新能源汽车推广应用财政补贴政策的通知 财建[2016]958号](http://jjs.mof.gov.cn/zhengwuxinxi/tongzhigonggao/201612/t20161229_2508628.html)

## build

### 前提条件
- [Git 2.0+](http://git-scm.com/downloads)
- [JDK 1.8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Gradle 3.0+](http://gradle.org/gradle-download/)
- [nvm 1.1+](https://github.com/creationix/nvm)

### 前提条件 - 环境配置
执行以下命令,确认版本符合前提条件中指定的要求
```SHELL
git --version
java -version
gradle --version
nvm version
```

### 前提条件 - node
从 https://github.com/nodejs/node/blob/master/doc/changelogs/CHANGELOG_V6.md 查询可供使用的node版本(建议使用node 6.10+)
执行以下命令配置node环境
```SHELL
nvm install 6.10.2
nvm use 6.10.2
nvm on
```

### 前提条件 - 依赖组件
执行以下命令安装依赖组件
```SHELL
npm install -g gulp-cli
npm install
```
提示: 在中国内地可以使用--registry参数来指定taobao的镜像来执行npm组件的安装
```SHELL
npm install -g bower gulp karma-cli --registry=http://registry.npm.taobao.org
npm install --registry=http://registry.npm.taobao.org
```

### 检测是否缺少依赖组件
随着开发的进展,其它开发人员会添加新的依赖项,如果缺少依赖项,程序就无法正常工作
执行以下命令检测是否缺少依赖项
```SHELL
npm ls --depth=0 #检测packages.json依赖
```


## 配置文件
本项目使用的配置文件位于
- [$/src/main/resources/application.yml](https://github.com/evcar-zd/subsidy/blob/master/src/main/resources/application.yml)
- 默认激活dev配置,因此,可以在`$/src/*/resources/`下创建一个名为`application-dev.yml`的配置文件,按自己的需要重载配置项
- 也可以通过定义一个名为spring.profiles.active的系统属性来指定激活的配置,例如:
```SHELL
gradle -Dspring.profiles.active=product bootRun
```
- 那么直接运行时 $/src/main/resources/application-product.yml 将被激活.
- 单元测试时 $/src/test/resources/application-product.yml 将被激活.
- 没有在`application-product.yml`里定义的配置,会继承`application.yml`里的定义.

## 运行
```SHELL
gradle bootRun
# or
java -jar build/libs/subsidy-1.0.jar
```