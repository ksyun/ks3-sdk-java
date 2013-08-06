北京金山云网络技术有限公司标准存储服务开发工具包Java版
KS3 SDK for Java

版权所有 （C）金山云科技有限公司

Copyright (C) Kingsoft Cloud
All rights reserved.

http:/ks3.ksyun.com/

环境要求：
- J2SE Development Kit (JDK) 6.0或以上版
- Apache Commons （HTTP Client和Logging）等（已包含在SDK的下载压缩包中）。
- 必须注册有KSC用户账户，并开通KS3服务。


更新日志：

------2013年4月22日------
1.增加了getObjectListByOptions方法，可以根据prefix、marker等信息获取ObjectList。
2.增加了getObejctByOptions方法，实现了可用于分块下载的range功能和用户与判断的If-Match、If-Modified-Since等功能。
3.增加了getPresignedUrl方法，用户可以自己根据指定参数生成预签名的url。

------2013年7月10日------
1.减少了对第三方的包的依赖。
2.优化了网络访问部分，性能更高，稳定性更强。
3.增加了对安卓的优化，安卓可以直接使用当前的SDK。
4.ObjectInfo对象已经可以进行序列化，因而用户可以对Object的List使用MC等容器直接进行缓存。
5.修正了特殊参数下签名计算错误引起的请求失败的bug。
6.更正了包的命名规则。
7.改进了其他若干细节。

------2013年7月18日------
1.修正了objectkey中不能含有"/"的bug。
2.对于ObjectListOptions中的setMarker等方法，现在已经可以设置空字符串("")。

------2013年8月5日------
1.修正了prefix中含有特殊符号搜索结果为空的问题。
2.对于SDK中的DTO，均增加并实现了toJson方法，用户可以更容易的得到格式化的结果来进行传输。
3.优化了生成预签名的下载链接的实现逻辑，前端打开新生成的链接可以直接弹出保存窗口。
4.修正了objectKey中含有空格、型号等特殊符号的情况下会导致部分功能出现异常的问题。
5.封装了ResponseException，当用户通过SDK调用S3成功但返回值不为200时，抛出此异常。（即请求成功，调用失败。例如：创建的bucket已经存在。）

