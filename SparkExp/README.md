# 项目概述

## 项目结构

```
.
├── README.md
├── build.sbt  项目配置文件
├── downloadScript.sh  下载dblp的完整xml数据的脚本
├── mongdb
│   └── docker-compose.yml  mongodb的compose文件，用于启动mongodb
├── other  其他一些参考资料
│   ├── dblp.dtd
│   └── dblpxml.pdf
├── src 源代码文件
│   ├── main
│   └── test  测试代码
└── whole  存放完整dblp xml数据，需要执行下载脚本下载后才会有目录内的文件
    ├── dblp.xml
    ├── dblp.xml.gz
    ├── dblp.xml.gz.md5
    └── dblp_cvt.xml
```

## 主要的依赖

```yaml
spark-core: 2.4.5
spark-sql: 2.4.5
spark-xml: 0.8.0
mongo-spark-connector: 2.4.1
```

# Quick Start

clone代码到本地
```bash
  git clone
```

运行下载脚本（下载过程比较慢，得自己想办法加快速的。下载完后需要自行解压为dblp.xml）
```bash
  bash downloadScript.sh
```

部署mongodb

```bash
  docker-compose up -d
```

