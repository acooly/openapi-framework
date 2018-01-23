OpenApi商户开发规范
====


## 网关

测试环境：
https://api.xxx.com/gateway.do

正式环境：
https://api.xxx.com/gateway.do

## 交互流程



## 通讯模式

文档中涉及的通讯机制和交互模式总共有四种，请文档使用开发人员特别注意各服务的通讯机制便于正确的进行通讯对接。

### 同步通讯

![](https://apidoc.yiji.com/website/images/img1.jpg)

说明：

1. **构造请求数据**：商户根据服务平台提供的接口规则，通过程序生成得到签名结果及要传输给服务平台的数据集合。
2. **发送请求数**：把构造完成的数据集合，通过页面链接跳转或表单提交的方式传递给服务平台。
3. **服务处理**：服务平台得到这些集合后，会先进行安全校验等验证，一系列验证通过后便会处理这次发送过来的数据请求。
4. **同步响应数据**：对于处理完成的交易，服务平台以同步响应的方式返回处理结果和数据，响应数据在同一个http连接的response报文的body体中返回，以JSON格式进行传输。
5. **响应数据处理**：商户系统同步接收到http响应后，获取Http响应的body体中的JSON报文体，根据具体服务接

### 异步通讯

![](https://apidoc.yiji.com/website/images/img2.jpg)

说明：

1. **构造请求数据**：商户根据服务平台提供的接口规则，通过程序生成得到签名结果及要传输给服务平台的数据集合。特别的为保障异步通知能正确的通知到商户系统，请保证服务器异步通知URL链接（notifyUrl）上无任何特别字符，如空格、HTML标签、开发系统自带抛出的异常提示信息等，也不能后接queryString。notifyUrl格式实例如下：
	* http://merchant-site/notifyUrl.html (正确)
	* http://merchant-site/notifyUrl.html?busiType=2 (错误，不能后接queryString)
	* http://merchant-site/ notifyUrl.html (错误，url中间不能有空格)
2. **发送请求数据**：商户系统通过HTTP-POST方式发送请求数据到服务平台网关。
3. **服务同步处理**：服务平台接收商户系统请求数据，进行同步业务处理。
4. **同步返回结果**：服务平台同步响应商户系统服务同步处理结果，一般情况返回的结果如果是成功则表示请求已接收或完成部分处理，需要商户系统等待异步通知业务最终处理结果；如果是失败则表示业务处理已终止，无后续异步通知。
5. **同步返回数据处理**：商户系统收到服务平台同步响应数据后，进行相关的业务或状态处理。
6. **服务异步处理**：服务平台返回同步处理结果后，会继续异步处理业务逻辑（如：等待银行处理结果返回后，进行进一步处理等）。
7. **异步通知处理结果**：服务平台在异步完成业务处理，得到该笔业务最终处理结果后，通过商户系统请求时传入的notifyUrl, 使用http-post方式请求notifyUrl，通知最后处理结果。
8. **异步通知数据处理**：商户系统在成功收到异步通知后，需要在响应体(http响应的body中)中打印输出”success” （不包含引号）表示商户系统已成功收到异步通知。如果商户反馈给服务平台的字符不是success这7个字符，服务平台服务器会不断重发通知，直到超过24小时。一般情况下，24小时以内完成8次通知（通知的间隔频率一般是：2m,10m,10m,1h,2h,6h,15h）

>注意：异步通讯的交易最终结果以异步通知为准，商户端必须处理异步通知结果（如没有异步通知的特别情况，请以具体API服务说明为准）。

### 同步转异步

![](https://apidoc.yiji.com/website/images/img3.jpg)

说明：

同步转异步的主体交互模式完全同异步通讯。不同的是异步通知是可选的，只有在同步响应返回resultCode=EXECUTE\_PROCESSING的时候才会有异步通知。具体根据"异步通讯"第5步的时候商户系统需要对返回的resultCode进行如下判断：

* 如果resultCode=EXECUTE\_PROCESSING,那么表示该业务返回时没有完成最终处理，当前正在处理中，需要等待后续异步通知最终结果；
* 如果resultCode=EXECUTE_SUCCESS表示该业务已经完成最终处理，后续无异步通知。
* 如果resultCode为其他值的情况表示处理错误响应，已经是最终结果，无后续异步处理。

### 跳转通讯

跳转通讯主要提供商户发起请求直接跳转到服务平台的页面进行业务处理的场景。如上图所示，主要流程描述如下：

![](https://apidoc.yiji.com/website/images/img4.jpg)

说明：

1. **构造请求数据**：商户根据服务平台提供的接口规则，通过程序生成得到签名结果及要传输给服务平台的数据集合。
2. **构造请求http-form表单**：使用第1步签名后的完整报文，组织页面form表单或对应的POST报文，请求服务平台网关（如果是form表单，action为服务平台网关）。
3. **http-form表单请求**：把构造完成的数据集合，通过页面链接跳转或表单提交的方式传递给服务平台。
4. **服务处理**：服务平台得到这些数据后，会先进行安全校验等验证，验证通过后便会处理这次发送过来的数据请求。
5. **重定向返回**：完成服务处理后，服务平台使用重定向返回服务平台对应功能页面的URL和参数（queryString方式）。
6. **重定向跳转到服务平台功能页面**：用户浏览器收到重定向响应后，会自动重新请求跳转的地址，跳转到对应的服务平台功能页面。
7. **服务平台页面操作和处理**：用户在服务平台功能界面提交相关业务数据后由服务平台完成业务处理。
8. **同步跳转回商户页面**： 用户通过第7步完成业务操作后，服务平台请求第1步请求中的returnUrl，跳转回商户界面，同时通过get参数(queryString方式)同步通知处理结果。
9. **处理和显示结果**：商户系统收到同步通知后，可根据同步通知参数进行业务处理和界面显示。
10. **异步通知处理结果**：在完成同步通知和跳转后，服务平台会根据商户系统请求时传入的notifyUrl进行POST异步通知商户系统处理结果。处理默认同异步通讯。
11. **异步通知数据处理**：根据具体服务接口定义的结构解析获取数据后，进行商户系统的业务处理。

## 通讯协议

通讯协议模块主要定义API接口的技术通信协议的详细说明和报文定义规则。

### 通信协议

所有的基础通信采用http/https协议进行网络通讯，主要推荐以POST为主，同时支持GET。

### 报文协议

报文协议定义请求，响应，通知等报文的组装模式（序列化）。目前我们采用HTTP\_FORM\_JSON混合模式，具体要求如下：

* **请求报文**：采用post-form表单或get-queryString模式组装报文, 所有参数的值在发送前需要进行UTF-8的URLEncode。

	post-form实例：
	
	```http
	POST /gateway.do HTTP/1.1
	Content-Length: 529
	Content-Type: application/x-www-form-urlencoded; charset=UTF-8
	Host: api.xxx.com
	Connection: Keep-Alive
	
	amount=1000.00&buyerCertNo=330702194706165014&buyerUserId=09876543211234567890&buyeryEmail=zhangpu%40163.com&buyeryMobileNo=13898765453&merchOrderNo=12345678901234567890&partnerId=20140411020055684571&password=btf5S9%2F4k7pauo0E%2FWCyPQ%3D%3D&payeeUserId=12345678900987654321&payerUserId=09876543211234567890&protocol=HTTP_FORM_JSON&requestNo=O00116062701414015000000&service=createOrder&sign=556acf767fe0117892c568722f9d9f48&signType=MD5&title=%E5%90%8C%E6%AD%A5%E8%AF%B7%E6%B1%82%E5%88%9B%E5%BB%BA%E8%AE%A2%E5%8D%95&version=1.0
	```

	get-queryString实例:
	
	```http
	http://api.xxx.com/geteway.html?key1=value1&key2=value2&key3=%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6
	```

* **响应报文**：网关处理完成后的同步响应报文采用http-body-json模式组装报文，所有的响应参数采用UTF-8编码json格式，直接写入响应报文的body体数据流。

	实例：
	
	```http
	HTTP/1.1 200 OK
	Date: Sun, 26 Jun 2016 17:35:07 GMT
	Content-Type: application/json;charset=UTF-8
	Content-Length: 100
	Keep-Alive: timeout=15, max=100
	Connection: Keep-Alive

	{
		"amount":"88.66",
		"amountIn":"88.66",
		"requestNo":"20150821000001",
		"tradeNo":"1234567980",
		"resultCode":"EXECUTE_SUCCESS",
		"sign":"05d37ec6daa420c23d13db403d920123",
		"resultMessage":"成功",
		"version":"1.0",
		"protocol":"httpPost",
		"success":"true",
		"service":"withdrawApply",
		"signType":"MD5",
		"partnerId":"20141229020000062199"
	}
	```

* **通知报文**：通知报文是网关完成处理后，主动请求商户端通知处理的结果。其中包括同步通知和异步通知，同步通知的地址为请求报文中传入的returnUrl, 异步通知的地址为请求报文中商户端传入的notifyUrl。同步通知采用http/redirect方式跳转returnUrl通知，采用http-get方式，异步通知采用http-post方式后台同步请求通知商户端。

	同步通知报文实例：
	
	```
	http://www.merchant.com/returnUrl.html?key1=value1&key2=value2&key3=%E6%9C%8D%E5%8A%A1%E6%A1%86%E6%9E%B6
	```
	
	>其中：http://www.merchant.com/returnUrl.html 是商户端请求时传入的returnUrl.
	
	异步通知报文实例：
	
	```http
	POST /openapi/notify.html HTTP/1.1
	Content-Length: 529
	Content-Type: application/x-www-form-urlencoded; charset=UTF-8
	Host: www.merchant.com
	Connection: Keep-Alive
	
	amount=1000.00&...signType=MD5&notifyUrl=http%3A%2F%2Fwww.merchant.com%3A8090%2Fopenapi%2Fnotify&notifyTime=2016-06-02%2012:12:12&version=1.0
	```
	
### 报文语义

所有API服务的报文定义中，我们采用统一的数据类型和状态表示语义。

#### 类型

类型表示报文数据项的数据类型和长度，主要的表示语义如下：

* S(x)：x为数字，表示字符串最大长度为x，最小长度为0，如果该字段状态为必选则最小长度为1
* S(x-y)：x和y为数字，表示字符串长度范围为：x-y，包含x和y
* SF(x): x为数字，表示字符串固定长度为x。
* n(x), x为数字，表示为数字类型，最大长度为x， n(x-y)和nf(x)与字符串同理。
* M：表示money类型，支持两位小数点的元。如:2000.01，10000或100.00
* Object/JSON: 表示对象类型，下面会紧跟Object的子报文结构定义。
* Array/JSON-ARRAY: 表示数组对象类型，下面会紧跟数组成员对象的子报文结构定义。

#### 状态
状态该数据项在段中的填充状态，有三种情况，必选，可选，条件选择。

* “M”：为必选型(Mandatory),表示必须报送该数据项。
* “O”：为可选型(Optional),表示可以填写也可以不填写该数据项，如果不填写则为空。
* “C”：为有条件选择型(Conditional),表示在符合条件的情况下,必须填写此数据项,不符
合条件的情况下,可以不填写此数据项，相关条件一般会在具体报文说明中明确说明。

### 报文定义

网关所有通讯模式的报文定义都遵循统一的规则，所有报文都基于公共报文定义，后续的所有业务报文定义只定义业务部分，公共部分以本模块定义的公共报文作为准，完整的报文由公共报文加上业务报文组成。同时，所有报文数据项的定义都明确类型，长度等关键信息。

完整的报文定义 = 公共报文 + 具体接口定义的业务报文

#### 公共请求报文

|参数名			|参数描述			|类型		 |状态    |实例           |备注
|------------|----------------|----------|-------|--------------|-----------
|requestNo	|请求号				|S(16-40)	 |M      |2016089983    |请求号，要求商户唯一。
|protocol    |协议类型			|S(16)     |O      |HTTP-FORM-JOSN|协议格式。HTTP-FORM-JOSN(默认)
|service	   |服务名称			|S(32)		 |M      |userRegister  |Api服务名称，与服务版本一起唯一标志一个服务
|version		|服务版本			|S(8)		 |O      |1.0           |Api服务版本，与服务名称一起唯一标志一个服务
|partnerId	|商户ID				|SF(20)	 |M      |20160809223120000001|签约的商户或合作商的ID,由平台分配。定长20字符
|signType		|签名方式			|S(16)		 |O      |MD5|签名认证方式，可选值为MD5,SHA1和RSA，MD5为默认值
|sign			|签名			   |S(128)	 |M      |fasdfasdfasdfasdf...|报文签名
|orderNo		|交易订单号			|S(16-40)  |O      |201606260001 |交易类业务的交易订单号，交易类业务如未特殊说明，都根据该订单号支持幂等。
|context		|会话参数			|S(128)	 |O      |{userId:1,busiId:2}|调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端。
|returnUrl	|页面跳转地址	|S(128)	 |O      |http://merc.com/return.html|当服务为跳转服务时，如果传入该值，网关在处理完后会回跳该参数指定的URL。
|notifyUrl	|异步通知地址	|S(128)	 |O      |http://merc.com/notice.html|当服务为跳转服务或异步通知时，如果传入该值，网关在处理完后会异步后台通知该地址处理结果。

>幂等性：orderNo是交易级（订单支付交易等）幂等要素，表示服务会根据orderNo参数维持服务的一致性（注意不是相同的结果，是行为一致）。当用户多次传入相同的orderNo请求交易时，服务会根据orderNo对应的实际交易的状态按一致的处理行为进行处理，比如：该订单对应的交易已处理完成，后续所有相同订单的请求对直接返回成功并处理完成，如果该订单对应的交易正在处理中或挂起中，则用户再次请求会触发业务继续进行~~，并返回当前的状态。


#### 公共响应报文

|参数名			|参数描述			|类型		 |状态    |实例           |备注
|------------|----------------|----------|-------|--------------|-----------
|success		|成功标识			|S(8)		 |M      |true          |表示接口调用是否成功。true：成功false：失败
|requestNo	|请求号				|S(16-40)	 |M      |2016089983    |请求号，要求商户唯一。
|protocol    |协议类型			|S(16)     |M      |HTTP-FORM-JOSN|协议格式。HTTP-FORM-JOSN(默认)
|service	   |服务名称			|S(32)		 |M      |userRegister  |Api服务名称，与服务版本一起唯一标志一个服务
|version		|服务版本			|S(8)		 |M      |1.0           |Api服务版本，与服务名称一起唯一标志一个服务
|partnerId	|商户ID				|SF(20)	 |M      |20160809223120000001|签约的商户或合作商的ID,由平台分配。定长20字符
|signType		|签名方式			|S(16)		 |M      |MD5|签名认证方式，可选值为MD5,SHA1和RSA，MD5为默认值
|sign			|签名			   |S(128)	 |M      |fasdfasdfasdfasdf...|报文签名
|orderNo		|交易订单号			|S(16-40)  |O      |201606260001 |交易类业务的交易订单号，交易类业务如未特殊说明，都根据该订单号支持幂等。
|context		|会话参数			|S(128)	 |O      |{userId:1,busiId:2}|调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端。
|resultCode	|响应编码			|S(32)		 |M|PARAM_ERROR|返回码,EXECUTE_SUCCESS：为处理成功，其他请参考“4.1 返回码”
|resultMessage|响应消息|S(128)|M|参数错误|响应编码对应的消息描述
|resultDetail|响应详情|S(128)|O|手机号码格式错误|服务响应信息详情

>结果判断：
>* success返回的标志只是标志该服务是否执行成功，并不一定确定该服务对应的业务处理成功，请根据具体接口进行判断。比如：异步提现接口，请求后的同步响应success=true,resultCode=EXECUTE\_PROCESSING 则表示：异步提现提交成功，正常处理中，提现并没有成功完成。
>* resultCode=EXECUTE\_SUCCESS和resultCode=EXECUTE\_PROCESSING表示处理成功，其他表示失败。


#### 公共通知报文

网关在设计处理通知报文时，为提高商户端的使用简便和体验，服务的同步通知（跳转通知）和异步通知采用完全相同的结构，只是在同步通知和异步通知时部分数据项填充不一定一致，请跟进具体api服务及业务进行处理。所以我们同步和异步通知的公共报文部分是完全相同的。

|参数名			|参数描述			|类型		 |状态    |实例           |备注
|------------|----------------|----------|-------|--------------|-----------
|requestNo	|请求号				|S(16-40)	 |M      |2016089983    |请求号，要求商户唯一。
|protocol    |协议类型			|S(16)     |M      |HTTP-FORM-JOSN|协议格式。HTTP-FORM-JOSN(默认)
|service	   |服务名称			|S(32)		 |M      |userRegister  |Api服务名称，与服务版本一起唯一标志一个服务
|version		|服务版本			|S(8)		 |M      |1.0           |Api服务版本，与服务名称一起唯一标志一个服务
|partnerId	|商户ID				|SF(20)	 |M      |20160809223120000001|签约的商户或合作商的ID,由平台分配。定长20字符
|signType		|签名方式			|S(16)		 |M      |MD5|签名认证方式，可选值为MD5,SHA1和RSA，MD5为默认值
|sign			|签名			   |S(128)	 |M      |fasdfasdfasdfasdf...|报文签名
|orderNo		|交易订单号			|S(16-40)  |O      |201606260001 |交易类业务的交易订单号，交易类业务如未特殊说明，都根据该订单号支持幂等。
|context		|会话参数			|S(128)	 |O      |{userId:1,busiId:2}|调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端。
|resultCode	|响应编码			|S(32)		 |M|PARAM_ERROR|返回码,EXECUTE_SUCCESS：为处理成功，其他请参考“4.1 返回码”
|resultMessage|响应消息|S(128)|M|参数错误|响应编码对应的消息描述
|resultDetail|响应详情|S(128)|O|手机号码格式错误|服务响应信息详情
|notifyTime|通知时间|SF(19)|O|2016-02-02 12:02:12|通知的发送时间。格式为yyyy-MM-dd HH:mm:ss


## 错误码与错误处理

### 错误码

错误码的定义在报文中统一使用响应码表示。响应码中除了EXECUTE\_SUCCESS和EXECUTE\_PROCESSING外的其他响应码都表示错误码。

所有API服务都是通过错误码标识处理结果，包括同步响应，跳转同步通知和异步通知我们采用这一统一的原则。错误码的表示方式采用三元错误消息方式，由resultCode,resultMessage和resultDetai三个数据项表示唯一的错误消息，其中resultDetail为可选。

* resultCode: 表示错误消息的编码Key，如果商户端需对特定的错误场景进行处理，请跟进错误码进行处理。
* resultMessage: 表示错误码对应的描述文字。
* resultDetail: 表示错误的详细描述，该数据线为可选。

错误码主要分为系统错误码和业务错误码两大类，系统错误码表示网关做的基础验证和处理失败对应的错误码；业务错误码表示具体api服务业务处理错误对应的错误码。系统错误码有明确的定义，请参考下表，业务错误码根据具体的业务定义，不做统一定义。

|错误代码|含义
|-------|------
|EXECUTE\_SUCCESS|交易成功
|EXECUTE\_PROCESSING|交易处理中
|INTERNAL\_ERROR|系统内部错误
|SERVICE\_NOT\_FOUND\_ERROR|服务不存在
|PARAMETER\_ERROR|参数错误
|PARAM\_FORMAT\_ERROR|参数格式错误
|UNAUTHENTICATED|认证(签名)错误
|UNAUTHORIZED|未授权的服务
|REQUEST\_NO\_NOT_UNIQUE|商户请求号不唯一
|FIELD\_NOT\_UNIQUE|对象字段重复
|REDIRECT\_URL\_NOT\_EXIST|重定向服务需设置redirectUrl
|PARTNER\_NOT\_REGISTER|合作伙伴没有注册
|PARTNER\_NOT\_PRODUCT|商户没有配置产品
|UNSUPPORTED\_SECHEME|不支持的请求协议


### 结果判断原则

所有交易同步响应和异步通知的响应结果中都有resultCode数据项，请根据resultCode的响应值判断交易的最终结果。具体判断方法如下：
* resultCode=EXECUTE\_SUCCESS : 表示最终结果为成功。
* resultCode=EXECUTE\_PROCESSING：表示交易正在处理中，需要等待异步通知来确定最终交易结果
* resultCode=其他： 表示交易错误或失败。

## 安全

网关API服务的安全方案中，需要商户端完成的主要是签名验签和加解密数据。提供两种安全方案：

1. **摘要方案**：摘要签名(MD5)/对称加密(ASE)
2. **证书方案**：证书签名(RSA)/非对称加密(RSA)

**安全方案选择**
具体采用哪种方案，根据商户开户时确定。在技术实现上，通过请求报文中的signType确定选择哪种方案。具体要求如下：

* signType=RSA: 表示选择证书方案。需要使用商户证书私钥对请求报文签名；使用网关证书对收到的报文验签；使用网关证书按需对数据加密；使用商户证书私钥对收到的密文数据项解密。
* signType为其他: 包括MD5,Sha1Hex，Sha2Hex等，表示选择摘要方案。商户使用商户安全码对报文进行摘要签名和验签，对安全数据项按需进行ASE对称加密和解密。

### 商户秘钥

商户开户后，网关运营会邮件方式发送商户开户的账户信息，认证信息和商户秘钥信息给商户接口人。这里对商户端通过邮件获取的商户秘钥进行介绍说明，主要用于后续的安全签名和数据加解密。

* **摘要安全模式**：通讯报文的签名，验签使用摘要算法（MD5，Sha1Hex和Sha256Hex等）实现和数据加解密采用ASE对称加密算法，商户收到的秘钥为32字节的字符串秘钥，我们命名为secrtKey，用于报文的摘要签名，验签和对称加解密的秘钥。 例如：c9cef22553afujh64b04a012f9cb8ea9

* **证书安全模式**：如果商户采用的是证书安全模式，通讯报文的签名，验签和加解密都使用RSA算法。商户会收到一对秘钥，包含商户证书（${partnerId}.pfx）和网关证书(server.cer)的两个文件。商户端的秘钥对(文件)为：

	* 商户证书: 商户的keystore证书（PFX格式），包含用户的私钥和公钥证书，主要用于请求报文签名和收到报文的数据解密，例如：20160010101010101.pfx, 如果是联调试环境，一般命名为: 20160010101010101.snet.pfx，同时会邮件发送给商户商户证书文件的保护密码(keystorePassword)。
	
	* 网关证书：网关的公钥证书，主要用户对收到的报文（响应，通知）验签和发送数据的加密.例如：server.cer,如果是联调环境，一般命名为:server.snet.cer



### 签名和验签

所有Api服务的安全认证采用数字签名方式，目前支持的签名和验签算法主要包括摘要签名和证书签名两种方式，其中摘要签名支持MD5，Sha1Hex和Sha256Hex，证书方式签名支持RSA算法。

在签名和验签前，我们首先需要处理的是生成报文对应的待签字符串，下面我们首先介绍待签字符串的生成，然后在分别介绍两种签名方式的具体计算和验证方法。


#### 待签字符串生成

**需要参与签名的参数**

请求报文，响应报文和通知报文，都采用相同的规则：对发送或接收到的所有参数（包括公共报文部分），除去sign参数外，都是需要参与计算待签名字符串的参数。

>注意：报文的签名数据默认为全签，以java为例子，建议直接通过request.getParameters()获取全部的参数，我们API服务的接口通信报文中不存在同名参数的多个值，所有该方法返回的所有参数值都可以直接按没有数组的情况处理。

**生成待签名字符串**

根据不同的报文场景（请求，响应，通知等），确定好待签名的数据项，采用如下格式进行组装（参数数组）：

```java
string[] parameters={ 
	"service=fastpay", 
	"partnerId=20121015300000032621", 
	"returnUrl= http://www.test.com/yiji/return_url.asp", 
	"orderNo=6741334835157966", 
	"tradeName=xxx电视机", 
	"tradeAmount=100" 
};
```

对数组里的每一个成员按**字符ASC码**的顺序排序，若遇到相同首字符，则看第二个字符，以此类推。排序完成之后，再把所有数组值以“&”字符连接起来，这串字符串便是待签名字符串，例如：

```http
orderNo=6741334835157966&partnerId=20121015300000032621&returnUrl=http://www.test.com/yiji/return_url.asp&service=fastpay&tradeAmount=100&tradeName=xxx电视机
```

>注意：
>1. 没有值的参数无需传递，也无需包含到待签名数据中；
>2. 签名时将字符转化成字节流时指定字符集全部采用UTF-8；
>3. 待签字符串中的特殊字符无需签名，根据HTTP协议，传递参数的值中如果存在特殊字符（如：&、@等）和中文字符，那么该值需要做UTF-8编码的URL-Encoding，这样请求接收方才能接收到正确的参数值。这种情况下，待签名数据应该是原生值而不是encoding之后的值。例如：调用某接口需要对请求参数email进行数字签名，那么待签名数据应该是email=test@msn.com，而不是email=test%40msn.com。

**复杂报文签名**

所有的请求报文和通知报文（异步通知，同步跳转通知）采用FORM方式的key/value方式组报的主要是为便于form表单提交，如果API服务某个数据项是复杂对象(Object)类型或数组类型(Array)，则需要把该项的值转换为标准JSON字符串后作为签名的参数值。

1. 请求报文的签名

	我们使用一个示例来进行说明，在标准支付接口中创建订单时，会要求传入的商品列表字段：goodlist，需要请求商户传递本次支付的所有商品信息（多个），每个商品信息为一个对象结构，包括：name，price，quantity等数据项，这个数据线的类型为复杂类型Array，那么:

	待签数据(无需URLEncoding)：

	```http
	goodsInfos=[{"goodType":"actual","name":"天子精品1","price":400.00,"quantity":1,"referUrl":"http://	acooly.cn/tianzi"} ,{"goodType":"actual","name":"天子精品2","price":400.00,"quantity":	1,"referUrl":"http://acooly.cn/tianzi"}]
	```

	发送数据(需要URLEncoding)：

	```
	goodsInfos =%5B%7B%22goodType%22%3A%22actual%22%2C%22name%22%3A%22%E5%A4%A9%E5%AD%90%E7%B2%BE	%E5%93%811%22%2C%22price%22%3A400.00%2C%22quantity%22%3A1%2C%22referUrl%22%3A%22http%3A%2F	%2Facooly.cn%2Ftianzi%22%7D+%2C%7B%22goodType%22%3A%22actual%22%2C%22name%22%3A%22%E5%A4%A9%E5%AD	%90%E7%B2%BE%E5%93%812%22%2C%22price%22%3A400.00%2C%22quantity%22%3A1%2C%22referUrl%22%3A%22http%3A	%2F%2Facooly.cn%2Ftianzi%22%7D%5D
	```

2. 通知报文的验签
复杂结构时生成待签字符串的方式与（1. 请求报文的签名）相同。

3. 响应报文的验签
	响应报文采用在http协议Body体写入JSON结构体方式响应数据。请求方收到的同步响应为标准JSON结构体。如果是单级结构（类似	key/value）时，则采用通用验证签方式组织待签字符串。如果是多级结构，则需要把二级以下的Json结构体以字符串方式作为一级属性	的数据项参与验证签名。

	复杂响应报文示例：
	
	```java
	{
    	"goodInfos": [
	        {
              "goodType": "actual", 
              "name": "天子精品1", 
              "price": 400, 
              "quantity": 1, 
              "referUrl": "http://acooly.cn/tianzi"
        	  }, 
        	  {
              "goodType": "actual", 
              "name": "天子精品2", 
              "price": 400, 
              "quantity": 1, 
              "referUrl": "http://acooly.cn/tianzi"
        	  }
    	], 
		"orderNo": "41111111111111111113", 
		//……
	}
	```

	响应报文待签字符串：
	
	```http
	goodsInfos=[{"goodType":"actual","name":"天子精品1","price":400.00,"quantity":1,"referUrl":"http://acooly.cn/tianzi"} ,{"goodType":"actual","name":"天子精品2","price":400.00,"quantity":1,"referUrl":"http://acooly.cn/tianzi"}]
	```


#### 摘要签名验签

摘要方式签名主要支持MD5，Sha1Hex和Sha256Hex，他们都采用标准的摘要算法实现。算法原则如下：

1. **签名明文**: 签名和验签的明文为代签字符串后接商户安全码（我们这里命名为secretKey商户开户时已邮件发送给商户联系人），请注意待签字符串与secretKey间没有任何连接字符。

	例如:

	* 代签字符串为：key1=val1&key2=val2, 
	* secretKey为: c9cef22553afujh64b04a012f9cb8ea9, 
	* 则计算签名的明文为：key1=val1&key2=val2<span style="color:red">c9cef22553afujh64b04a012f9cb8ea9</span>

2. **签名计算**：首先转换签名明文(P)为UTF-8的字节数组（byte[]）,通过标准摘要算法计算出摘要值的字节数组形式（byte[]），然后转换摘要的字节数组为Hex（小写字母的16进制形式）的字符串格式(S)则为摘要签名。
	**伪代码公式为: S=Hex(MD5(P.getBytes("UTF-8")))**

	>摘要签名计算出的值作为请求报文的sign参数值一并请求网关。

3. **验证签名**：验证签名是在商户端收到网站的同步响应报文或通知报文后，对这些报文进行签名验证。原则为，使用“2. **签名计算**”的方法对报文计算签名值为signClient,然后与报文中传入的sign参数值（服务器端计算的签名）进行字符串比较，如果相同则表示验证签名通过，报文在传输过程中未被篡改。


#### 证书签名和验签

证书签名和验签基于商户证书，使用非对称加密算法RSA实现。如果我们选择使用证书签名方式，则要求商户在开通服务时选择证书加密选项，商户完成开户时，会收到包含商户证书（pfx）和网关证书(cer)的两个文件，这两个文件是完成我们签名和后续数据加密的秘钥对文件。同时另外一种获取秘钥文件的方式是登录开放平台，自助下载证书文件。

>在开始介绍证书签名和验签前，商户端开发人员需要具备一定的证书和非对称加密的技术和理论基础。

**商户证书说明：**

* 商户证书格式为pfx格式,keystore类型（keystoreType）为PKCS12，keystore保护密码(keystorePassword)联调环境为6个1（111111），计算RSA签名时，需要从pfx文件中获取私钥数据，你可以使用各语言的通用API加载并获取（推荐方案），也可以通过openssl命令导出私钥文件。
* RSA算法实现：JCE标准实现
* RSA秘钥长度：2048位。
* RSA算法名称：SHA1withRSA


**签名和验签计算：**

1. **签名明文**: 签名和验签的明文为代签字符串。这里请注意与摘要方式的区别是无需后接任何秘钥。
2. **签名计算**: 签名的计算使用标准RSA签名算法，请根据商户端语言环境选择具体的实现，使用商户证书的**商户私钥**对请求报文进行签名计算，计算出的结果为256字节（byte数组）的签名数据,请对签名数据进行标准base64编码，最后得到344字节的字符串则为报文的签名。
3. **验证签名**: 验证签名的计算仍然使用RSA标准算法，使用网关的证书公钥进行验签。

>注意：请参考客户端工具中的java代码示例。

### 加密和解密

为保证报文中私密信息的安全性，我们在传输过程中，要求报文的部分数据项需要加密传输。根据报文定义中，明确说明的需要密文的数据项，对数据项的数据进行加密和解密处理。

>注意：请求方数据加密在签名前完成，签名的待签数据以密文计算；解密数据在验签后处理，验签明文以密文计算。

#### ASE加密和解密

如果报文的signType采用非RSA方式，我们采用ASE算法对需要加密的数据项加密。

**加解密说明**：

* 加密秘钥为商户安全码(secrtKey，全数组和字母组合)的前16字节，长度为128位, 使用16字节字符串转换为UTF-8编码的byte数组。
* ASE算法的模式为ECB，填充方式为PKCS5Padding。算法/模式/填充为：AES/ECB/PKCS5Padding
* 加密的明文以UTF-8编码转换为byte数组作为加密入参
* 加密后密文需要Base64编码默认作为最终加密数据用于签名和传输
* 解密时，需要对密文进行Base64解码后作为解密入参
* 解密后的明文byte数组需要以UTF-8编码转换为字符串，作为最终的明文数据。

**加密伪代码(java)**

```java
//ASE 秘钥：商户安全码前16字节
byte[] aseKey = secrtKey.substring(0,16).getBytes();
String cipherText = Base64Encode( AESEncrypt( plainText.getByte("UTF-8"), aseKey) );
```

**解密伪代码(java)**

```java
//ASE 秘钥：商户安全码前16字节
byte[] aseKey = secrtKey.substring(0,16).getBytes();
String plainText = new String(AESDecrypt(Base64Decode(cipherText), aseKey), "UTF-8");
```

#### RSA加密和解密

如果报文的signType为RSA,表示我们采用RSA（证书）非对称加解密数据项。在介绍RSA加密和解密前，需要你先了解和参考：“商户秘钥” 和 “证书签名和验签”。

RSA加密和解密与摘要方式的使用场景一样，都是按报文定义，明确需要加密和解密的数据项才进行加解密处理。不同摘要方式加解密的是RSA是非对称加密算法，加密和解密采用的是一对秘钥。商户的秘钥对都保持在商户证书文件(pfx)中。

在本场景中，涉及商户测对请求报文按需加密和对收到的报文按需解密，我们分别介绍两种场景对秘钥和算法使用详情。

>注意：RSA加密是采用分段加密方式，每次加密的明文数据长度为秘钥长度减去11个字节，然后把分段加密的数据连接起来则为密文，解密逻辑相同，只是反向行为。


**加密**

* 秘钥：使用的秘钥为网关的证书公钥（server.cer）进行加密，秘钥长度2048位。
* 算法：模式为ECB，填充方式为PKCS5Padding。算法/模式/填充为：RSA/ECB/PKCS5Padding(java的JCE默认参数)
* 编码：明文数据采用UTF-8编码，密文使用BASE64编码。

伪代码：

```java
// 从服务网关公钥加载公钥。
PublicKey publicKey = loadPublicKeyFormCert("/path/server.cer");
// 使用标准RSA加密原始数据，然后使用Base64编码
String cipherText = Base64Encode(RSAEncrypt(plainText.getBytes("UTF-8"), publicKey));
```

**解密**

* 秘钥：使用的秘钥为商户证书的私钥（merchant.pfx）进行解密，秘钥长度2048位。
* 算法：模式为ECB，填充方式为PKCS5Padding。算法/模式/填充为：RSA/ECB/PKCS5Padding(java的JCE默认参数)
* 编码：解密后的明文byte数组使用UTF-8编码转换为字符串。

伪代码：

```java
// 从商户证书加载私钥。
PrivateKey merchantPrivateKey = loadPrivateKeyFormPfx("/path/merchant.pfx");
// 使用标准RSA解密密文数据，然后使用转换为UTF-8的字符串
String plainText = new String(RSAEncrypt(Base64Decode(cipherText), merchantPrivateKey), "UTF-8");
```


## 代码工具

代码工具中提供的代码块以java语言编写，作为商户端开发接入的参考实现.

### 获取所有参数

商户端在收集请求参数，通知参数时，需要获取请求域（scope）内所有的参数。

第三方依赖包：

```xml
<dependency>
	<groupId>javax.servlet</groupId>
	<artifactId>javax.servlet-api</artifactId>
	<version>3.0.1</version>
	<scope>provided</scope>
</dependency>
```

参考代码：

```java
    /**
     * 获取所有参数,
     * 我们不允许用户传入多个同名参数.如果传入，则获取首个参数值
     *
     * @param request ServletRequest
     * @return 所有参数Map
     */
    public static Map<String, String> getParameters(ServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String[] values = request.getParameterValues(name);
            if (values == null || values.length == 0) {
                continue;
            }
            String value = values[0];
            // 注意：这里是判断不为null,没有包括空字符串的判断。
            if (value != null) {
                params.put(name, value);
            }
        }
        return params;
    }
```

### 待签字符串组装

参考代码：

```java
    /**
     * 生成待签字符串
     *
     * @param data 原始map数据
     * @return 待签字符串
     */
    public static String buildWaitingForSign(Map<String, String> data) {
        if (data == null || data.size() == 0) {
            throw new IllegalArgumentException("请求数据不能为空");
        }
        String waitingForSign = null;
        Map<String, String> sortedMap = new TreeMap<>(data);
        // 如果sign参数存在,去除sign参数,不参与签名
        if (sortedMap.containsKey("sign")) {
            sortedMap.remove("sign");
        }
        StringBuilder stringToSign = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (entry.getValue() != null) {
                stringToSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        stringToSign.deleteCharAt(stringToSign.length() - 1);
        waitingForSign = stringToSign.toString();
        logger.debug("代签名字符串:{}", waitingForSign);
        return waitingForSign;
    }
```

### 摘要签名和验签

摘要方式签名，这里采用MD5方式提供参考，目前采用apache-commons的标准工具实现。

依赖第三方组件：apache commons-codec-1.8

```xml
<dependency>
	<groupId>commons-codec</groupId>
	<artifactId>commons-codec</artifactId>
	<version>1.8</version>
</dependency>
```

#### 签名

参考代码：

```java
    /**
     * MD5摘要签名
     *
     * @param waitToSignStr
     * @param key
     * @return
     */
    public static String signMD5(String waitToSignStr, String key) {
        // MD5摘要签名计算
        String signature = DigestUtils.md5Hex(waitToSignStr + key);
        return signature;
    }


    /**
     * 报文数据MD5摘要签名
     * <p>
     * 对报文数据签名,并回填签名到报文数据map中
     *
     * @param data 报文数据
     * @param key  商户安全码
     * @return 保护签名的报文数据
     */
    public static Map<String, String> signMD5(Map<String, String> data, String key) {
        String waitToSignStr = buildWaitingForSign(data);
        // MD5摘要并转换为Hex格式
        String signature = DigestUtils.md5Hex(waitToSignStr + key);
        data.put("sign", signature);
        return data;
    }
```

#### 验签

参考代码：

```java
    /**
     * 验证MD5签名
     *
     * @param waitToSignStr 待签字符串
     * @param key           商户安全码
     * @param verifySign    待验证签名
     * @return 验签结果。 true: 成功, false: 失败
     */
    public static boolean verifyMD5(String waitToSignStr, String key, String verifySign) {
        // MD5摘要签名计算
        String signature = DigestUtils.md5Hex(waitToSignStr + key);
        return verifySign.equals(signature);
    }

    /**
     * 验证MD5签名
     *
     * @param data       报文数据
     * @param key        商户安全码
     * @param verifySign 待验证签名
     * @return 验签结果。 true: 成功, false: 失败
     */
    public static boolean verifyMD5(Map<String, String> data, String key, String verifySign) {
        // 生成待签字符串
        String waitToSignStr = buildWaitingForSign(data);
        // MD5摘要签名计算
        String signature = DigestUtils.md5Hex(waitToSignStr + key);
        return verifySign.equals(signature);
    }

```

### 发送报文组装

在完成加密，签名处理后，我们需要对处理后的数据进行编码处理后才能发送给网关。

>GET方式请求不需要进行URLEncoding，如果是POST方式提交不用进行编码处理，POST-FORM模式需要指定Header:  Content-Type: application/x-www-form-urlencoded; charset=UTF-8

参考代码：

```java
    /**
     * 生成发送数据
     *
     * @param map 报文数据
     * @return 编码后的发送数据
     */
    public static String getMessage(Map<String, String> map) {
        StringBuilder messageBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (Strings.isNotBlank(entry.getValue())) {
                messageBuilder.append(entry.getKey()).append("=")
                        .append(Encodes.urlEncode(entry.getValue()))
                        .append("&");
            }
        }
        messageBuilder.deleteCharAt(messageBuilder.length() - 1);
        return messageBuilder.toString();
    }
```

### 加载公钥

商户端在加密发送数据项和对收到报文的验签时，需要使用服务网关的公钥证书。我们需要从服务网关证书文件中加载这个证书。

第三发依赖包：

这里依赖spring-core主要是使用Spring的Resource加载功能，可代替。

```xml
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
	<!-- spring的版本支持2.5及以上版本 -->
	<version>3.2.8.RELEASE</version>
</dependency>
```


参考代码：

```java
    /**
     * 从证书文件加载公钥
     * <p>
     * 用于获取服务网关的公钥,用于发送数据加密和收到报文的验签
     * </p>
     *
     * @param certFileUri 证书文件URI,支持物理路径和classpath:
     * @return JCE标准公钥对称, 用户后续加密和验签
     */
    public static PublicKey loadPublicKeyFromCert(String certFileUri) {
        InputStream in = null;
        try {
            Resource resource = new DefaultResourceLoader().getResource(certFileUri);
            in = resource.getInputStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate x509 = (X509Certificate) cf.generateCertificate(in);
            return x509.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("加载公钥失败:" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ig
                }

            }
        }
    }
```

### 加载私钥

第三发依赖包：

这里依赖spring-core主要是使用Spring的Resource加载功能，可代替。

```xml
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-core</artifactId>
	<!-- spring的版本支持2.5及以上版本 -->
	<version>3.2.8.RELEASE</version>
</dependency>
```


参考代码

```java
    /**
     * 商户证书(pfx)加载私钥
     *
     * 用于: 商户发送签名的签名和收到报文数据的解密。
     *
     * @param keystoreUri pfx文件的URI,支持物理路径和classpath:
     * @param keystorePassword pfx保护密码
     * @return JCE标准私钥
     */
    public static PrivateKey loadPrivateKeyFromKeyStore(String keystoreUri, String keystorePassword) {
        InputStream in = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            Resource resource = new DefaultResourceLoader().getResource(keystoreUri);
            in = resource.getInputStream();
            keyStore.load(in, keystorePassword.toCharArray());

            Enumeration<String> enumas = keyStore.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
            }
            return (PrivateKey) keyStore.getKey(keyAlias, keystorePassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("通过keystore加载私钥失败:" + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ig
                }

            }
        }
    }
```

### 证书签名和验签

#### 证书签名

商户端在发送报文前，需要使用**商户证书的私钥**对报文进行签名。

参考代码：

```java
    /**
     * RSA私钥签名(使用商户证书私钥)
     *
     * @param waitToSignStr 待签字符串
     * @param privateKey    商户证书私钥
     * @return 签名base64编码结果
     */
    public static String signBase64(String waitToSignStr, PrivateKey privateKey) {
        try {
            byte[] dataBytes = waitToSignStr.getBytes("UTF-8");
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(privateKey);
            signature.update(dataBytes);
            byte[] result = signature.sign();
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            throw new RuntimeException("RAS私钥签名失败:" + e.getMessage());
        }
    }
```

#### 证书验签

商户端在收到网关报文（同步响应，同步跳转通知，异步通知）时，需要使用**网关证书**公钥对报文验签。

参考代码：

```java
    /**
     * 公钥验签(使用网关证书公钥)
     *
     * @param waitToSignStr 待签字符串
     * @param signBase64    网关的签名
     * @param publicKey     网关的公钥
     * @return 验证结果 true:通过,false:未通过
     */
    public static boolean verifyBase64(String waitToSignStr, String signBase64, PublicKey publicKey) {
        try {
            byte[] dataBytes = waitToSignStr.getBytes("UTF-8");
            byte[] signBytes = Base64.decodeBase64(signBase64);
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(publicKey);
            signature.update(dataBytes);
            return signature.verify(signBytes);
        } catch (Exception e) {
            throw new RuntimeException("RSA公钥验签失败: " + e.getMessage());
        }
    }
```

### 对称加解密

#### AES加密

代码参考：

```java
    /**
     * ASE加密
     *
     * @param plainText 明文
     * @param secretKey 商户安全码
     * @return 密文
     */
    public static String AESEncrypt(String plainText, String secretKey) {
        try {
            // 使用商户安全码前16字节作为加密秘钥
            byte[] secretKeyBytes = secretKey.substring(0, 16).getBytes("UTF-8");
            // 明文数据
            byte[] plainBytes = plainText.getBytes("UTF-8");
            SecretKey key = new SecretKeySpec(secretKeyBytes, "AES");
            // 获取Cipher对象较为耗费资源,如追求性能,请对cipher做缓存方案;
            // 通过算法/模式/填充获取cipher对象时,采用默认AES,表示: AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherBytes = cipher.doFinal(plainBytes);
            // 转换为base64(utf-8)字符串作为密文
            return Base64.encodeBase64String(cipherBytes);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败: " + e.getMessage());
        }
    }
```

#### AES解密

参考代码：

```java
    /**
     * ASE解密
     *
     * @param cipherText 密文
     * @param secretKey  商户安全码
     * @return 明文
     */
    public static String AESDecrypt(String cipherText, String secretKey) {
        try {
            // 使用商户安全码前16字节作为加密秘钥
            byte[] secretKeyBytes = secretKey.substring(0, 16).getBytes("UTF-8");
            // 密文数据: base64(utf-8)字符串
            byte[] cipherBytes = Base64.decodeBase64(cipherText);
            SecretKey key = new SecretKeySpec(secretKeyBytes, "AES");
            // 获取Cipher对象较为耗费资源,如追求性能,请对cipher做缓存方案;
            // 通过算法/模式/填充获取cipher对象时,采用默认AES,表示: AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            // 转换为utf-8字符串则为明文
            return new String(cipherBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败: " + e.getMessage());
        }
    }
```

### 非对称加解密

#### 加密

参考代码：

```java
    /**
     * 公钥加密
     *
     * @param plainText 明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptByPublicKey(String plainText, PublicKey publicKey) {
        ByteArrayOutputStream out = null;
        try {
            byte[] plainBytes = plainText.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = plainBytes.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密: 每段处理的数据为秘钥长度-11
            int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
            int plainLength = keyLength - 11;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > plainLength) {
                    cache = cipher.doFinal(plainBytes, offSet, plainLength);
                } else {
                    cache = cipher.doFinal(plainBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * plainLength;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("公钥加密失败:" + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    //ig
                }
            }
        }
    }
```

#### 解密

参考代码：

```java
    /**
     * 私钥解密
     *
     * @param cipherBase64 密文(base64)
     * @param privateKey   私钥
     * @return 明文
     */
    public static String decryptByPrivateKey(String cipherBase64, PrivateKey privateKey) {
        ByteArrayOutputStream out = null;
        try {
            byte[] encryptedBytes = Base64.decodeBase64(cipherBase64);
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int inputLen = encryptedBytes.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > keyLength) {
                    cache = cipher.doFinal(encryptedBytes, offSet, keyLength);
                } else {
                    cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * keyLength;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("私钥解密失败:" + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    //ig
                }
            }
        }
    }
```

## 代码表

#### 银行


