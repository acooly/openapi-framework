<!DOCTYPE html>
<html>
<head>
		<@includePage path="/docs/common/meta.html"/>
		<@includePage path="/docs/common/include.html"/>
    <link rel="stylesheet" type="text/css" href="/portal/style/css/api.css"/>
    <link rel="stylesheet" type="text/css" href="/portal/style/css/spec.css"/>
</head>
<body>
<!--顶部-->
		<@includePage path="/docs/common/header.html"/>
<!--end-->

<!--面包屑-->
<div class="crumb">
    <div class="w1190">
            <span class="layui-breadcrumb">
              <a href="/docs/index.html">文档中心</a>
              <a><cite>开发规范</cite></a>
            </span>
    </div>
</div>

<!--正文-->
<div class="container">
    <!--左边-->
    <div class="container-left">
                <@includePage path="/docs/apischeme/menu.html?page=spec"/>
    </div>
    <!--右边-->
    <div class="container-main">

        <div id="spec_dir" style="display: none;">
            <ul class="site-dir">
                <li><a href="#intetive" class="layui-this"><cite>交互流程</cite></a></li>
                <li><a href="#transfer"><cite>通讯模式</cite><em>4种模式</em></a></li>
                <ul style="padding-left: 20px;">
                    <li><a href="#transfer_sync">同步模式</a></li>
                    <li><a href="#transfer_asyn">异步模式</a></li>
                    <li><a href="#transfer_sync_asyn" class="">同步转异步</a></li>
                    <li><a href="#transfer_redirect">跳转模式</a></li>
                </ul>
                <li><a href="#protocol"><cite>通讯协议</cite></a></li>
                <ul style="padding-left: 20px;">
                    <li><a href="#protocol_tx">传输协议</a></li>
                    <li><a href="#protocol_bw">报文协议</a></li>
                    <li><a href="#protocol_yy" class="">报文语义</a></li>
                </ul>
                <li><a href="#errorcode"><cite>返回码</cite></a></li>
                <li><a href="#security"><cite>安全</cite><em>签名和加密</em></a></li>
                <ul style="padding-left: 20px;">
                    <li><a href="#merchantKey">商户秘钥</a></li>
                    <li><a href="#merchantSign">签名和验签</a></li>
                    <li><a href="#merchantCrypter">加密和解密</a></li>
                </ul>
                <li><a href="#codetools"><cite>代码工具</cite><em>Java</em></a></li>
            </ul>
        </div>

        <div class="doc-header">
            <div class="doc-header-title"><i class="layui-icon">&#xe62a;</i> 开发规范</div>
        </div>
        <div class="doc-content spec">

            <h2 id="toc_2"><a name="intetive">交互流程</a></h2>
            <p style="text-align: center"><img width="70%" src="/portal/images/spec/interactive.jpg" alt=""></p>
            <h2 id="toc_3"><a name="transfer">通讯模式</a></h2>

            <p>文档中涉及的通讯机制和交互模式总共有四种，请文档使用开发人员特别注意各服务的通讯机制便于正确的进行通讯对接。</p>

            <h3 id="toc_4"><a name="transfer_sync">同步通讯</a></h3>

            <p style="text-align: center"><img width="70%" src="/portal/images/spec/sync.jpg" alt=""></p>

            <p>说明：</p>

            <ol>
                <li><strong>1.构造请求报文</strong>：商户根据服务平台提供的接口规则，通过程序生成得到签名结果及要传输给服务平台的数据集合。这里需要注意处理顺序为：参数检查 -> 按需加密 -> 签名</li>
                <li><strong>2.发送请求报文</strong>：把构造完成的数据集合，通过页面链接跳转或表单提交的方式传递给服务平台。</li>
                <li><strong>3.服务同步处理</strong>：服务平台得到这些集合后，会先进行安全校验等验证，一系列验证通过后便会处理这次发送过来的数据请求。</li>
                <li><strong>4.同步响应数据</strong>：对于处理完成的交易，服务平台以同步响应的方式返回处理结果和数据，响应数据在同一个http连接的response报文的body体中返回，以JSON格式进行传输。</li>
                <li><strong>5.响应数据处理</strong>：商户系统同步接收到http响应后，获取Http响应的body体中的JSON报文体，根据具体服务接</li>
            </ol>

            <h3 id="toc_5"><a name="transfer_asyn">异步通讯</a></h3>

            <p style="text-align: center"><img width="70%" src="/portal/images/spec/asyn.jpg" alt=""></p>

            <p>说明：</p>

            <ol>
                <li><strong>1. 构造请求数据</strong>：商户根据服务平台提供的接口规则，通过程序生成得到签名结果及要传输给服务平台的数据集合。特别的为保障异步通知能正确的通知到商户系统，请保证服务器异步通知URL链接（notifyUrl）上无任何特别字符，如空格、HTML标签、开发系统自带抛出的异常提示信息等，也不能后接queryString。notifyUrl格式实例如下：

                    <ul>
                        <li>http://merchant-site/notifyUrl.html (正确)</li>
                        <li>http://merchant-site/notifyUrl.html?busiType=2 (错误，不能后接queryString)</li>
                        <li>http://merchant-site/ notifyUrl.html (错误，url中间不能有空格)</li>
                    </ul>
                </li>
                <li><strong>2. 发送请求数据</strong>：商户系统通过HTTP-POST方式发送请求数据到服务平台网关。</li>
                <li><strong>3. 服务同步处理</strong>：服务平台接收商户系统请求数据，进行同步业务处理。</li>
                <li><strong>4. 同步返回结果</strong>：服务平台同步响应商户系统服务同步处理结果，一般情况返回的结果如果是成功则表示请求已接收或完成部分处理，需要商户系统等待异步通知业务最终处理结果；如果是失败则表示业务处理已终止，无后续异步通知。
                </li>
                <li><strong>5. 同步返回数据处理</strong>：商户系统收到服务平台同步响应数据后，进行相关的业务或状态处理。</li>
                <li><strong>6. 服务异步处理</strong>：服务平台返回同步处理结果后，会继续异步处理业务逻辑（如：等待银行处理结果返回后，进行进一步处理等）。</li>
                <li><strong>7. 异步通知处理结果</strong>：服务平台在异步完成业务处理，得到该笔业务最终处理结果后，通过商户系统请求时传入的notifyUrl, 使用http-post方式请求notifyUrl，通知最后处理结果。</li>
                <li><strong>8. 异步通知数据处理</strong>：商户系统在成功收到异步通知后，需要在响应体(http响应的body中)中打印输出”success”
                    （不包含引号）表示商户系统已成功收到异步通知。如果商户反馈给服务平台的字符不是success这7个字符，服务平台服务器会不断重发通知，直到超过24小时。一般情况下，24小时以内完成8次通知（通知的间隔频率一般是：2m,10m,10m,1h,2h,6h,15h）
                </li>
            </ol>

            <blockquote>
                <p>注意：异步通讯的交易最终结果以异步通知为准，商户端必须处理异步通知结果（如没有异步通知的特别情况，请以具体API服务说明为准）。</p>
            </blockquote>

            <h3 id="toc_6"><a name="transfer_sync_asyn">同步转异步</a></h3>

            <p>同步转异步的主体交互模式完全同异步通讯。不同的是异步通知是可选的，只有在同步响应返回code=PROCESSING的时候才会有异步通知。具体根据<q>异步通讯</q>第5步的时候商户系统需要对返回的code进行如下判断：
            </p>

            <ul>
                <li>如果code=PROCESSING,那么表示该业务返回时没有完成最终处理，当前正在处理中，需要等待后续异步通知最终结果；</li>
                <li>如果code=SUCCESS表示该业务已经完成最终处理，后续无异步通知。</li>
                <li>如果code为其他值的情况表示处理错误响应，已经是最终结果，无后续异步处理。</li>
            </ul>

            <h3 id="toc_7"><a name="transfer_redirect">跳转通讯</a></h3>

            <p>跳转通讯主要提供商户发起请求直接跳转到服务平台的页面进行业务处理的场景。如上图所示，主要流程描述如下：</p>

            <p style="text-align: center"><img width="70%" src="/portal/images/spec/redirect.jpg" alt=""></p>

            <p>说明：</p>

            <ol>
                <li><strong>1.构造请求数据</strong>：商户根据服务平台提供的接口规则，通过程序生成得到签名结果及要传输给服务平台的数据集合。</li>
                <li><strong>2.构造请求http-form表单</strong>：使用第1步签名后的完整报文，组织页面form表单或对应的POST报文，请求服务平台网关（如果是form表单，action为服务平台网关）。</li>
                <li><strong>3.http-form表单请求</strong>：把构造完成的数据集合，通过页面链接跳转或表单提交的方式传递给服务平台。</li>
                <li><strong>4.服务处理</strong>：服务平台得到这些数据后，会先进行安全校验等验证，验证通过后便会处理这次发送过来的数据请求。</li>
                <li><strong>5.重定向返回</strong>：完成服务处理后，服务平台使用重定向返回服务平台对应功能页面的URL和参数（queryString方式）。</li>
                <li><strong>6.重定向跳转到服务平台功能页面</strong>：用户浏览器收到重定向响应后，会自动重新请求跳转的地址，跳转到对应的服务平台功能页面。</li>
                <li><strong>7.服务平台页面操作和处理</strong>：用户在服务平台功能界面提交相关业务数据后由服务平台完成业务处理。</li>
                <li><strong>8.同步跳转回商户页面</strong>： 用户通过第7步完成业务操作后，服务平台请求第1步请求中的returnUrl，跳转回商户界面，同时通过get参数(queryString方式)同步通知处理结果。</li>
                <li><strong>9.处理和显示结果</strong>：商户系统收到同步通知后，可根据同步通知参数进行业务处理和界面显示。</li>
                <li><strong>10.异步通知处理结果</strong>：在完成同步通知和跳转后，服务平台会根据商户系统请求时传入的notifyUrl进行POST异步通知商户系统处理结果。处理默认同异步通讯。</li>
                <li><strong>11.异步通知数据处理</strong>：根据具体服务接口定义的结构解析获取数据后，进行商户系统的业务处理。</li>
            </ol>

            <h2 id="toc_8"><a name="protocol">通讯协议</a></h2>

            <p>通讯协议模块主要定义API接口的技术通信协议的详细说明和报文定义规则。</p>

            <h3 id="toc_9"><a name="protocol_tx">传输协议</a></h3>

            <p>所有的基础通信采用http/https协议进行网络通讯，主要推荐以POST为主，同时支持GET。</p>

            <h3 id="toc_10"><a name="protocol_bw">报文协议</a></h3>

            <p>报文协议定义请求，响应，通知等报文的组装模式（序列化）。目前我们采用json/form模式，具体要求如下：</p>

            <ul>
                <li><p><strong>请求报文</strong>：采用application/json或application/x-www-form-urlencoded组装报文, 所有参数的值在发送前需要进行UTF-8 URLEncode。</p>

                    <p>application/json实例：</p>

                    <pre class="line-numbers"><code class="language-http">
POST /gateway.do HTTP/1.1
Content-Length: 529
Content-Type: application/json;charset=UTF-8
x-api-accessKey=test
x-api-sign=a8b539bba201cdcc1838c4f73276e528
x-api-signType=MD5
Host: api.xxx.com
Connection: Keep-Alive

{
  "buyerCertNo": "330702194706165014",
  "buyerUserId": "09876543211234567890",
  "buyeryEmail": "qiuboboy@qq.com",
  "buyeryMobileNo": "13898765453",
  "context": "1cf9afa1-3a63-4434-b5fb-bbbe306d76f5",
  "ext": {
    "oid": "123456"
  }
  "partnerId": "test",
  "payeeUserId": "12345678900987654321",
  "payerUserId": "09876543211234567890",
  "requestNo": "48cbbdc5-5a29-4545-a0ed-3942eb519eb9",
  "service": "createOrder",
  "title": "同步请求创建订单",
  "version": "1.0"
}
</code></pre>

                    <p>application/x-www-form-urlencoded实例:</p>

                    <pre class="line-numbers"><code class="language-http">
POST /gateway.do HTTP/1.1
Content-Length: 529
Content-Type: application/json;charset=UTF-8
x-api-accessKey=test
x-api-sign=a8b539bba201cdcc1838c4f73276e528
x-api-signType=MD5
Host: api.xxx.com
Connection: Keep-Alive

body={"buyerCertNo":"330702194706165014","buyerUserId":"09876543211234567890","context":"22aa964a-f4bc-43bd-be70-cddfba881187","ext":{"xx":"oo"},"partnerId":"test","password":"6960p9k8Mj+ok30v/SdA5g==","payeeUserId":"12345678900987654321","payerUserId":"09876543211234567890","requestNo":"0e95458f-1898-4333-ab4c-731cb8cb50b9","service":"createOrder","title":"同步请求创建订单","version":"1.0"}
                                </code></pre>

                    <blockquote>
                        *  x-api-accessKey、x-api-sign、x-api-signType这三个认证参数也可以放在请求url中，比如上面的例子，请求地址为:
                        /gateway.do?x-api-accessKey=test&x-api-sign=a8b539bba201cdcc1838c4f73276e528&x-api-signType=MD5
                        <br>
                        * 表单方式提交时：请求数据存放在body参数中
                    </blockquote>
                </li>


                <li><p><strong>响应报文</strong>：网关处理完成后的同步响应报文为json格式，所有的响应参数采用UTF-8编码json格式，直接写入响应报文的body体数据流。</p>

                    <p>实例：</p>

                    <pre class="line-numbers"><code class="language-http">HTTP/1.1 200 OK

Date: Sun, 26 Jun 2016 17:35:07 GMT
Content-Type: application/json;charset=UTF-8
Content-Length: 100
x-api-signType: MD5
x-api-sign: f2362b57befefb2054302a07be33ba6b
Keep-Alive: timeout=15, max=100
Connection: Keep-Alive

{"code":"SUCCESS","context":"22aa964a-f4bc-43bd-be70-cddfba881187","ext":{},"message":"成功","partnerId":"test","requestNo":"0e95458f-1898-4333-ab4c-731cb8cb50b9","service":"createOrder","status":"enable","success":true,"version":"1.0"}
</code></pre>
                </li>

                <li><p><strong>通知报文</strong>：通知报文是网关完成处理后，主动请求商户端通知处理的结果。其中包括同步通知和异步通知，同步通知的地址为请求报文中传入的returnUrl,
                    异步通知的地址为请求报文中商户端传入的notifyUrl。同步通知采用http/redirect方式跳转returnUrl通知，采用http-get方式，异步通知采用http-post方式后台同步请求通知商户端。</p>

                    <p>同步通知报文实例：</p>

                    <pre><code>http://www.merchant.com/returnUrl.html?x-api-signType=MD5&x-api-sign=47fd51a27ff63456686668b07392ab49&body=%7B%22code%22%3A%22SUCCESS%22%2C%22context%22%3A%221ca29b24-abca-4ace-8bae-277e5db79890%22%2C%22ext%22%3A%7B%7D%2C%22message%22%3A%22%E6%88%90%E5%8A%9F%22%2C%22partnerId%22%3A%22test%22%2C%22requestNo%22%3A%22b1bb149d-d1e9-4db2-8e33-0f3d0f7f36cf%22%2C%22service%22%3A%22withdraw%22%2C%22success%22%3Atrue%2C%22tradeNo%22%3A%22324862a678366339cc4047447b3b8d88%22%2C%22version%22%3A%221.0%22%7D
                               </code></pre>

                    <blockquote>
                        <p>其中：http://www.merchant.com/returnUrl.html是商户端请求时传入的returnUrl.响应内容放在body参数中。</p>
                    </blockquote>

                    <p>异步通知报文实例：</p>

                    <pre class="line-numbers"><code class="language-http">

POST /openapi/notify.html HTTP/1.1
Content-Length: 529
Content-Type: application/json;charset=UTF-8
x-api-signType: MD5
x-api-sign: f2362b57befefb2054302a07be33ba6b
Host: www.merchant.com
Connection: Keep-Alive

{"code":"SUCCESS","context":"22aa964a-f4bc-43bd-be70-cddfba881187","ext":{},"message":"成功","partnerId":"test","requestNo":"0e95458f-1898-4333-ab4c-731cb8cb50b9","service":"createOrder","status":"enable","success":true,"version":"1.0"}
</code></pre>
                </li>
            </ul>

            <h3 id="toc_11"><a name="protocol_yy">报文语义</a></h3>

            <p>所有API服务的报文定义中，我们采用统一的数据类型和状态表示语义。</p>

            <h4 id="toc_12">类型</h4>

            <p>类型表示报文数据项的数据类型和长度，主要的表示语义如下：</p>

            <ul>
                <li>S(x)：x为数字，表示字符串最大长度为x，最小长度为0，如果该字段状态为必选则最小长度为1</li>
                <li>S(x-y)：x和y为数字，表示字符串长度范围为：x-y，包含x和y</li>
                <li>SF(x): x为数字，表示字符串固定长度为x。</li>
                <li>n(x), x为数字，表示为数字类型，最大长度为x， n(x-y)和nf(x)与字符串同理。</li>
                <li>M：表示money类型，支持两位小数点的元。如:2000.01，10000或100.00</li>
                <li>Object/JSON: 表示对象类型，下面会紧跟Object的子报文结构定义。</li>
                <li>Array/JSON-ARRAY: 表示数组对象类型，下面会紧跟数组成员对象的子报文结构定义。</li>
            </ul>

            <h4 id="toc_13">状态</h4>

            <p>状态该数据项在段中的填充状态，有三种情况，必选，可选，条件选择。</p>

            <ul>
                <li>“M”：为必选型(Mandatory),表示必须报送该数据项。</li>
                <li>“O”：为可选型(Optional),表示可以填写也可以不填写该数据项，如果不填写则为空。</li>
                <li>“C”：为有条件选择型(Conditional),表示在符合条件的情况下,必须填写此数据项,不符
                    合条件的情况下,可以不填写此数据项，相关条件一般会在具体报文说明中明确说明。
                </li>
            </ul>

            <h3 id="toc_14">报文定义</h3>

            <p>网关所有通讯模式的报文定义都遵循统一的规则，所有报文都基于公共报文定义，后续的所有业务报文定义只定义业务部分，公共部分以本模块定义的公共报文作为准，完整的报文由公共报文加上业务报文组成。同时，所有报文数据项的定义都明确类型，长度等关键信息。</p>

            <p>完整的报文定义 = 公共报文 + 具体接口定义的业务报文</p>

            <h4 id="toc_15">公共请求报文</h4>

            <table>
                <thead>
                <tr>
                    <th>参数名</th>
                    <th>参数描述</th>
                    <th>类型</th>
                    <th>状态</th>
                    <th>实例</th>
                    <th>备注</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>partnerId</td>
                    <td>商户ID</td>
                    <td>SF(20)</td>
                    <td>M</td>
                    <td>20160809223120000001</td>
                    <td>签约的商户或合作商的ID,由平台分配。定长20字符</td>
                </tr>
                <tr>
                    <td>service</td>
                    <td>服务名称</td>
                    <td>S(32)</td>
                    <td>M</td>
                    <td>userRegister</td>
                    <td>Api服务名称，与服务版本一起唯一标志一个服务</td>
                </tr>
                <tr>
                    <td>version</td>
                    <td>服务版本</td>
                    <td>S(8)</td>
                    <td>O</td>
                    <td>1.0</td>
                    <td>Api服务版本，与服务名称一起唯一标志一个服务</td>
                </tr>
                <tr>
                    <td>requestNo</td>
                    <td>请求号</td>
                    <td>S(16-40)</td>
                    <td>M</td>
                    <td>2016089983</td>
                    <td>请求号，要求商户唯一。</td>
                </tr>
                <tr>
                    <td>ext</td>
                    <td>扩展参数</td>
                    <td>S(16-40)</td>
                    <td>O</td>
                    <td>{userId:1,busiId:2}</td>
                    <td>交易类业务的交易订单号，交易类业务如未特殊说明，都根据该订单号支持幂等。</td>
                </tr>
                <tr>
                    <td>context</td>
                    <td>会话参数</td>
                    <td>S(128)</td>
                    <td>O</td>
                    <td>id=123456</td>
                    <td>调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端。</td>
                </tr>
                <tr>
                    <td>returnUrl</td>
                    <td>页面跳转地址</td>
                    <td>S(128)</td>
                    <td>O</td>
                    <td><a href="http://merc.com/return.html">http://merc.com/return.html</a></td>
                    <td>当服务为跳转服务时，如果传入该值，网关在处理完后会回跳该参数指定的URL。</td>
                </tr>
                <tr>
                    <td>notifyUrl</td>
                    <td>异步通知地址</td>
                    <td>S(128)</td>
                    <td>O</td>
                    <td><a href="http://merc.com/notice.html">http://merc.com/notice.html</a></td>
                    <td>当服务为跳转服务或异步通知时，如果传入该值，网关在处理完后会异步后台通知该地址处理结果。</td>
                </tr>
                </tbody>
            </table>


            <h4 id="toc_16">公共响应报文</h4>

            <table>
                <thead>
                <tr>
                    <th>参数名</th>
                    <th>参数描述</th>
                    <th>类型</th>
                    <th>状态</th>
                    <th>实例</th>
                    <th>备注</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>success</td>
                    <td>成功标识</td>
                    <td>S(8)</td>
                    <td>M</td>
                    <td>true</td>
                    <td>表示接口调用是否成功。true：成功false：失败</td>
                </tr>
                <tr>
                    <td>code</td>
                    <td>响应编码</td>
                    <td>S(32)</td>
                    <td>M</td>
                    <td>PARAM_ERROR</td>
                    <td>返回码,EXECUTE_SUCCESS：为处理成功，其他请参考“4.1 返回码”</td>
                </tr>
                <tr>
                    <td>message</td>
                    <td>响应消息</td>
                    <td>S(128)</td>
                    <td>M</td>
                    <td>参数错误</td>
                    <td>响应编码对应的消息描述</td>
                </tr>
                <tr>
                    <td>detail</td>
                    <td>响应详情</td>
                    <td>S(128)</td>
                    <td>O</td>
                    <td>手机号码格式错误</td>
                    <td>服务响应信息详情</td>
                </tr>

                <tr>
                    <td>partnerId</td>
                    <td>商户ID</td>
                    <td>SF(20)</td>
                    <td>M</td>
                    <td>20160809223120000001</td>
                    <td>签约的商户或合作商的ID,由平台分配。定长20字符</td>
                </tr>
                <tr>
                    <td>service</td>
                    <td>服务名称</td>
                    <td>S(32)</td>
                    <td>M</td>
                    <td>userRegister</td>
                    <td>Api服务名称，与服务版本一起唯一标志一个服务</td>
                </tr>
                <tr>
                    <td>version</td>
                    <td>服务版本</td>
                    <td>S(8)</td>
                    <td>O</td>
                    <td>1.0</td>
                    <td>Api服务版本，与服务名称一起唯一标志一个服务</td>
                </tr>
                <tr>
                    <td>requestNo</td>
                    <td>请求号</td>
                    <td>S(16-40)</td>
                    <td>M</td>
                    <td>2016089983</td>
                    <td>请求号，要求商户唯一。</td>
                </tr>
                <tr>
                    <td>ext</td>
                    <td>扩展参数</td>
                    <td>S(16-40)</td>
                    <td>O</td>
                    <td>{userId:1,busiId:2}</td>
                    <td>交易类业务的交易订单号，交易类业务如未特殊说明，都根据该订单号支持幂等。</td>
                </tr>
                <tr>
                    <td>context</td>
                    <td>会话参数</td>
                    <td>S(128)</td>
                    <td>O</td>
                    <td>id=123456</td>
                    <td>调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端。</td>
                </tr>

                </tbody>
            </table>

            <blockquote>
                <p>结果判断：<br>
                    * success返回的标志只是标志该服务是否执行成功，并不一定确定该服务对应的业务处理成功，请根据具体接口进行判断。比如：异步提现接口，请求后的同步响应success=true,code=PROCESSING
                    则表示：异步提现提交成功，正常处理中，提现并没有成功完成。<br>
                    * code=SUCCESS和code=PROCESSING表示处理成功，其他表示失败。</p>
            </blockquote>

            <h4 id="toc_17">公共通知报文</h4>

            <p>
                网关在设计处理通知报文时，为提高商户端的使用简便和体验，服务的同步通知（跳转通知）和异步通知采用完全相同的结构，只是在同步通知和异步通知时部分数据项填充不一定一致，请跟进具体api服务及业务进行处理。所以我们同步和异步通知的公共报文部分是完全相同的。</p>


            <h2 id="toc_18"><a name="errorcode">错误码与错误处理</a></h2>

            <h3 id="toc_19">错误码</h3>

            <p>错误码的定义在报文中统一使用响应码表示。响应码中除了SUCCESS和PROCESSING外的其他响应码都表示错误码。</p>

            <p>
                所有API服务都是通过错误码标识处理结果，包括同步响应，跳转同步通知和异步通知我们采用这一统一的原则。错误码的表示方式采用三元错误消息方式，由code,message和detail三个数据项表示唯一的错误消息，其中detail为可选。</p>

            <ul>
                <li>code: 表示错误消息的编码Key，如果商户端需对特定的错误场景进行处理，请跟进错误码进行处理。</li>
                <li>message: 表示错误码对应的描述文字。</li>
                <li>detail: 表示错误的详细描述，该数据线为可选。</li>
            </ul>

            <p>错误码主要分为系统错误码和业务错误码两大类，系统错误码表示网关做的基础验证和处理失败对应的错误码；业务错误码表示具体api服务业务处理错误对应的错误码。系统错误码有明确的定义，请参考下表，业务错误码根据具体的业务定义，不做统一定义。</p>

            <table>
                <thead>
                <tr>
                    <th>错误代码</th>
                    <th>含义</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>SUCCESS</td>
                    <td>交易成功</td>
                </tr>
                <tr>
                    <td>PROCESSING</td>
                    <td>交易处理中</td>
                </tr>
                <tr>
                    <td>INTERNAL_ERROR</td>
                    <td>系统内部错误</td>
                </tr>
                <tr>
                    <td>SERVICE_NOT_FOUND_ERROR</td>
                    <td>服务不存在</td>
                </tr>
                <tr>
                    <td>PARAMETER_ERROR</td>
                    <td>参数错误</td>
                </tr>
                <tr>
                    <td>PARAM_FORMAT_ERROR</td>
                    <td>参数格式错误</td>
                </tr>
                <tr>
                    <td>UNAUTHENTICATED_ERROR</td>
                    <td>认证(签名)错误</td>
                </tr>
                <tr>
                    <td>UNAUTHORIZED_ERROR</td>
                    <td>未授权的服务</td>
                </tr>
                <tr>
                    <td>REQUEST_NO_NOT_UNIQUE</td>
                    <td>商户请求号不唯一</td>
                </tr>
                <tr>
                    <td>FIELD_NOT_UNIQUE</td>
                    <td>对象字段重复</td>
                </tr>
                <tr>
                    <td>REDIRECT_URL_NOT_EXIST</td>
                    <td>重定向服务需设置redirectUrl</td>
                </tr>
                <tr>
                    <td>PARTNER_NOT_REGISTER</td>
                    <td>合作伙伴没有注册</td>
                </tr>
                <tr>
                    <td>PARTNER_NOT_PRODUCT</td>
                    <td>商户没有配置产品</td>
                </tr>
                <tr>
                    <td>UNSUPPORTED_SECHEME</td>
                    <td>不支持的请求协议</td>
                </tr>
                </tbody>
            </table>

            <h3 id="toc_20">结果判断原则</h3>

            <p>所有交易同步响应和异步通知的响应结果中都有code数据项，请根据code的响应值判断交易的最终结果。具体判断方法如下：<br>
                * code=SUCCESS : 表示最终结果为成功。<br>
                * code=PROCESSING：表示交易正在处理中，需要等待异步通知来确定最终交易结果<br>
                * code=其他： 表示交易错误或失败。</p>

            <h2 id="toc_21"><a name="security">安全</a></h2>

            <p>网关API服务的安全方案中，需要商户端完成的主要是签名验签和加解密数据。提供两种安全方案：</p>

            <ol>
                <li><strong>摘要方案</strong>：摘要签名(MD5)/对称加密(ASE)</li>
                <li><strong>证书方案</strong>：证书签名(RSA)/非对称加密(RSA)</li>
            </ol>

            <p><strong>安全方案选择</strong><br>
                具体采用哪种方案，根据商户开户时确定。在技术实现上，通过请求报文中的x-api-signType确定选择哪种方案。具体要求如下：</p>

            <ul>
                <li>x-api-signType=RSA: 表示选择证书方案。需要使用商户证书私钥对请求报文签名；使用网关证书对收到的报文验签；使用网关证书按需对数据加密；使用商户证书私钥对收到的密文数据项解密。</li>
                <li>x-api-signType为其他: 包括MD5,Sha1Hex，Sha2Hex等，表示选择摘要方案。商户使用商户安全码对报文进行摘要签名和验签，对安全数据项按需进行ASE对称加密和解密。</li>
            </ul>

            <h3 id="toc_22"><a name="merchantKey">商户秘钥</a></h3>

            <p>商户开户后，网关运营会邮件方式发送商户开户的账户信息，认证信息和商户秘钥信息给商户接口人。这里对商户端通过邮件获取的商户秘钥进行介绍说明，主要用于后续的安全签名和数据加解密。</p>

            <ul>
                <li><p><strong>摘要安全模式</strong>：通讯报文的签名，验签使用摘要算法（MD5，Sha1Hex和Sha256Hex等）实现和数据加解密采用ASE对称加密算法，商户收到的秘钥为32字节的字符串秘钥，我们命名为secrtKey，用于报文的摘要签名，验签和对称加解密的秘钥。
                    例如：c9cef22553afujh64b04a012f9cb8ea9</p></li>
                <li><p><strong>证书安全模式</strong>：如果商户采用的是证书安全模式，通讯报文的签名，验签和加解密都使用RSA算法。商户会收到一对秘钥，包含商户证书（${partnerId}
                    .pfx）和网关证书(server.cer)的两个文件。商户端的秘钥对(文件)为：</p>

                    <ul>
                        <li><p>商户证书: 商户的keystore证书（PFX格式），包含用户的私钥和公钥证书，主要用于请求报文签名和收到报文的数据解密，例如：20160010101010101.pfx, 如果是联调试环境，一般命名为:
                            20160010101010101.snet.pfx，同时会邮件发送给商户商户证书文件的保护密码(keystorePassword)。</p></li>
                        <li><p>网关证书：网关的公钥证书，主要用户对收到的报文（响应，通知）验签和发送数据的加密.例如：server.cer,如果是联调环境，一般命名为:server.snet.cer</p></li>
                    </ul>
                </li>
            </ul>

            <h3 id="toc_23"><a name="merchantSign">签名和验签</a></h3>

            <p>所有Api服务的安全认证采用数字签名方式，目前支持的签名和验签算法主要包括摘要签名和证书签名两种方式，其中摘要签名支持MD5，Sha1Hex和Sha256Hex，证书方式签名支持RSA算法。</p>

            签名处理过程如下：

            <ol>
                <li>1. 构造请求数据json</li>
                <code>
                {"amount":"100.00","busiType":"T0","context":"1ca29b24-abca-4ace-8bae-277e5db79890","ext":{},"partnerId":"test","requestNo":"b1bb149d-d1e9-4db2-8e33-0f3d0f7f36cf","service":"withdraw","version":"1.0"}
                </code>
                <li>2. 构造签名数据：请求数据+secretKey</li>
                开通商户时，分配的secretKey=06f7aab08aa2431e6dae6a156fc9e034<br/>
                <code>
                {"amount":"100.00","busiType":"T0","context":"1ca29b24-abca-4ace-8bae-277e5db79890","ext":{},"partnerId":"test","requestNo":"b1bb149d-d1e9-4db2-8e33-0f3d0f7f36cf","service":"withdraw","version":"1.0"}06f7aab08aa2431e6dae6a156fc9e034
                </code>

                <li>3. 生成签名</li>
                x-api-sign=2d378cfad3bc3119e326fffa05d4e335

            </ol>


            <h4 id="toc_26">证书签名和验签</h4>

            <p>
                证书签名和验签基于商户证书，使用非对称加密算法RSA实现。如果我们选择使用证书签名方式，则要求商户在开通服务时选择证书加密选项，商户完成开户时，会收到包含商户证书（pfx）和网关证书(cer)的两个文件，这两个文件是完成我们签名和后续数据加密的秘钥对文件。同时另外一种获取秘钥文件的方式是登录开放平台，自助下载证书文件。</p>

            <blockquote>
                <p>在开始介绍证书签名和验签前，商户端开发人员需要具备一定的证书和非对称加密的技术和理论基础。</p>
            </blockquote>

            <p><strong>商户证书说明：</strong></p>

            <ul>
                <li>
                    商户证书格式为pfx格式,keystore类型（keystoreType）为PKCS12，keystore保护密码(keystorePassword)联调环境为6个1（111111），计算RSA签名时，需要从pfx文件中获取私钥数据，你可以使用各语言的通用API加载并获取（推荐方案），也可以通过openssl命令导出私钥文件。
                </li>
                <li>RSA算法实现：JCE标准实现</li>
                <li>RSA秘钥长度：2048位。</li>
                <li>RSA算法名称：SHA1withRSA</li>
            </ul>

            <p><strong>签名和验签计算：</strong></p>

            <ol>
                <li><strong>签名明文</strong>: 签名和验签的明文为代签字符串。这里请注意与摘要方式的区别是无需后接任何秘钥。</li>
                <li><strong>签名计算</strong>: 签名的计算使用标准RSA签名算法，请根据商户端语言环境选择具体的实现，使用商户证书的<strong>商户私钥</strong>对请求报文进行签名计算，计算出的结果为256字节（byte数组）的签名数据,请对签名数据进行标准base64编码，最后得到344字节的字符串则为报文的签名。
                </li>
                <li><strong>验证签名</strong>: 验证签名的计算仍然使用RSA标准算法，使用网关的证书公钥进行验签。</li>
            </ol>

            <blockquote>
                <p>注意：请参考客户端工具中的java代码示例。</p>
            </blockquote>

            <h3 id="toc_27"><a name="merchantCrypter">加密和解密</a></h3>

            <p>为保证报文中私密信息的安全性，我们在传输过程中，要求报文的部分数据项需要加密传输。根据报文定义中，明确说明的需要密文的数据项，对数据项的数据进行加密和解密处理。</p>

            <blockquote>
                <p>注意：请求方数据加密在签名前完成，签名的待签数据以密文计算；解密数据在验签后处理，验签明文以密文计算。</p>
            </blockquote>

            <h4 id="toc_28">ASE加密和解密</h4>

            <p>如果报文的signType采用非RSA方式，我们采用ASE算法对需要加密的数据项加密。</p>

            <p><strong>加解密说明</strong>：</p>

            <ul>
                <li>加密秘钥为商户安全码(secretKey，全数组和字母组合)的前16字节，长度为128位, 使用16字节字符串转换为UTF-8编码的byte数组。</li>
                <li>ASE算法的模式为ECB，填充方式为PKCS5Padding。算法/模式/填充为：AES/ECB/PKCS5Padding</li>
                <li>加密的明文以UTF-8编码转换为byte数组作为加密入参</li>
                <li>加密后密文需要Base64编码默认作为最终加密数据用于签名和传输</li>
                <li>解密时，需要对密文进行Base64解码后作为解密入参</li>
                <li>解密后的明文byte数组需要以UTF-8编码转换为字符串，作为最终的明文数据。</li>
            </ul>

            <p><strong>加密伪代码(java)</strong></p>

            <pre class="line-numbers"><code class="language-java">//ASE 秘钥：商户安全码前16字节
byte[] aseKey = secrtKey.substring(0,16).getBytes();
String cipherText = Base64Encode( AESEncrypt( plainText.getByte(&quot;UTF-8&quot;), aseKey) );</code></pre>

            <p><strong>解密伪代码(java)</strong></p>

            <pre class="line-numbers"><code class="language-java">//ASE 秘钥：商户安全码前16字节
byte[] aseKey = secrtKey.substring(0,16).getBytes();
String plainText = new String(AESDecrypt(Base64Decode(cipherText), aseKey), &quot;UTF-8&quot;);</code></pre>

            <h4 id="toc_29">RSA加密和解密</h4>

            <p>如果报文的signType为RSA,表示我们采用RSA（证书）非对称加解密数据项。在介绍RSA加密和解密前，需要你先了解和参考：“商户秘钥” 和 “证书签名和验签”。</p>

            <p>RSA加密和解密与摘要方式的使用场景一样，都是按报文定义，明确需要加密和解密的数据项才进行加解密处理。不同摘要方式加解密的是RSA是非对称加密算法，加密和解密采用的是一对秘钥。商户的秘钥对都保持在商户证书文件(pfx)中。</p>

            <p>在本场景中，涉及商户测对请求报文按需加密和对收到的报文按需解密，我们分别介绍两种场景对秘钥和算法使用详情。</p>

            <blockquote>
                <p>注意：RSA加密是采用分段加密方式，每次加密的明文数据长度为秘钥长度减去11个字节，然后把分段加密的数据连接起来则为密文，解密逻辑相同，只是反向行为。</p>
            </blockquote>

            <p><strong>加密</strong></p>

            <ul>
                <li>秘钥：使用的秘钥为网关的证书公钥（server.cer）进行加密，秘钥长度2048位。</li>
                <li>算法：模式为ECB，填充方式为PKCS5Padding。算法/模式/填充为：RSA/ECB/PKCS5Padding(java的JCE默认参数)</li>
                <li>编码：明文数据采用UTF-8编码，密文使用BASE64编码。</li>
            </ul>

            <p>伪代码：</p>

            <pre class="line-numbers"><code class="language-java">// 从服务网关公钥加载公钥。
PublicKey publicKey = loadPublicKeyFormCert(&quot;/path/server.cer&quot;);
// 使用标准RSA加密原始数据，然后使用Base64编码
String cipherText = Base64Encode(RSAEncrypt(plainText.getBytes(&quot;UTF-8&quot;), publicKey));</code></pre>

            <p><strong>解密</strong></p>

            <ul>
                <li>秘钥：使用的秘钥为商户证书的私钥（merchant.pfx）进行解密，秘钥长度2048位。</li>
                <li>算法：模式为ECB，填充方式为PKCS5Padding。算法/模式/填充为：RSA/ECB/PKCS5Padding(java的JCE默认参数)</li>
                <li>编码：解密后的明文byte数组使用UTF-8编码转换为字符串。</li>
            </ul>

            <p>伪代码：</p>

            <pre class="line-numbers"><code class="language-java">// 从商户证书加载私钥。
PrivateKey merchantPrivateKey = loadPrivateKeyFormPfx(&quot;/path/merchant.pfx&quot;);
// 使用标准RSA解密密文数据，然后使用转换为UTF-8的字符串
String plainText = new String(RSAEncrypt(Base64Decode(cipherText), merchantPrivateKey), &quot;UTF-8&quot;);</code></pre>

            <h2 id="toc_30"><a name="codetools">代码工具</a></h2>

            <p>代码工具中提供的代码块以java语言编写，作为商户端开发接入的参考实现.</p>

            <h3 id="toc_31">获取所有参数</h3>

            <p>商户端在收集请求参数，通知参数时，需要获取请求域（scope）内所有的参数。</p>

            <p>第三方依赖包：</p>

            <pre class="line-numbers"><code class="language-markup">&lt;dependency&gt;
    &lt;groupId&gt;javax.servlet&lt;/groupId&gt;
    &lt;artifactId&gt;javax.servlet-api&lt;/artifactId&gt;
    &lt;version&gt;3.0.1&lt;/version&gt;
    &lt;scope&gt;provided&lt;/scope&gt;
&lt;/dependency&gt;</code></pre>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * 获取所有参数,
     * 我们不允许用户传入多个同名参数.如果传入，则获取首个参数值
     *
     * @param request ServletRequest
     * @return 所有参数Map
     */
    public static Map&lt;String, String&gt; getParameters(ServletRequest request) {
        Map&lt;String, String&gt; params = Maps.newHashMap();
        Enumeration&lt;String&gt; enumeration = request.getParameterNames();
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
    }</code></pre>

            <h3 id="toc_32">待签字符串组装</h3>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * 生成待签字符串
     *
     * @param data 原始map数据
     * @return 待签字符串
     */
    public static String buildWaitingForSign(Map&lt;String, String&gt; data) {
        if (data == null || data.size() == 0) {
            throw new IllegalArgumentException(&quot;请求数据不能为空&quot;);
        }
        String waitingForSign = null;
        Map&lt;String, String&gt; sortedMap = new TreeMap&lt;&gt;(data);
        // 如果sign参数存在,去除sign参数,不参与签名
        if (sortedMap.containsKey(&quot;sign&quot;)) {
            sortedMap.remove(&quot;sign&quot;);
        }
        StringBuilder stringToSign = new StringBuilder();
        for (Map.Entry&lt;String, String&gt; entry : sortedMap.entrySet()) {
            if (entry.getValue() != null) {
                stringToSign.append(entry.getKey()).append(&quot;=&quot;).append(entry.getValue()).append(&quot;&amp;&quot;);
            }
        }
        stringToSign.deleteCharAt(stringToSign.length() - 1);
        waitingForSign = stringToSign.toString();
        logger.debug(&quot;代签名字符串:{}&quot;, waitingForSign);
        return waitingForSign;
    }</code></pre>

            <h3 id="toc_33">摘要签名和验签</h3>

            <p>摘要方式签名，这里采用MD5方式提供参考，目前采用apache-commons的标准工具实现。</p>

            <p>依赖第三方组件：apache commons-codec-1.8</p>

            <pre class="line-numbers"><code class="language-markup">&lt;dependency&gt;
    &lt;groupId&gt;commons-codec&lt;/groupId&gt;
    &lt;artifactId&gt;commons-codec&lt;/artifactId&gt;
    &lt;version&gt;1.8&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

            <h4 id="toc_34">签名</h4>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
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
     * &lt;p&gt;
     * 对报文数据签名,并回填签名到报文数据map中
     *
     * @param data 报文数据
     * @param key  商户安全码
     * @return 保护签名的报文数据
     */
    public static Map&lt;String, String&gt; signMD5(Map&lt;String, String&gt; data, String key) {
        String waitToSignStr = buildWaitingForSign(data);
        // MD5摘要并转换为Hex格式
        String signature = DigestUtils.md5Hex(waitToSignStr + key);
        data.put(&quot;sign&quot;, signature);
        return data;
    }</code></pre>

            <h4 id="toc_35">验签</h4>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
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
    public static boolean verifyMD5(Map&lt;String, String&gt; data, String key, String verifySign) {
        // 生成待签字符串
        String waitToSignStr = buildWaitingForSign(data);
        // MD5摘要签名计算
        String signature = DigestUtils.md5Hex(waitToSignStr + key);
        return verifySign.equals(signature);
    }
</code></pre>

            <h3 id="toc_36">发送报文组装</h3>

            <p>在完成加密，签名处理后，我们需要对处理后的数据进行编码处理后才能发送给网关。</p>

            <blockquote>
                <p>GET方式请求不需要进行URLEncoding，如果是POST方式提交不用进行编码处理，POST-FORM模式需要指定Header: Content-Type: application/x-www-form-urlencoded;
                    charset=UTF-8</p>
            </blockquote>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * 生成发送数据
     *
     * @param map 报文数据
     * @return 编码后的发送数据
     */
    public static String getMessage(Map&lt;String, String&gt; map) {
        StringBuilder messageBuilder = new StringBuilder();
        for (Map.Entry&lt;String, String&gt; entry : map.entrySet()) {
            if (Strings.isNotBlank(entry.getValue())) {
                messageBuilder.append(entry.getKey()).append(&quot;=&quot;)
                        .append(Encodes.urlEncode(entry.getValue()))
                        .append(&quot;&amp;&quot;);
            }
        }
        messageBuilder.deleteCharAt(messageBuilder.length() - 1);
        return messageBuilder.toString();
    }</code></pre>

            <h3 id="toc_37">加载公钥</h3>

            <p>商户端在加密发送数据项和对收到报文的验签时，需要使用服务网关的公钥证书。我们需要从服务网关证书文件中加载这个证书。</p>

            <p>第三发依赖包：</p>

            <p>这里依赖spring-core主要是使用Spring的Resource加载功能，可代替。</p>

            <pre class="line-numbers"><code class="language-markup">&lt;dependency&gt;
    &lt;groupId&gt;org.springframework&lt;/groupId&gt;
    &lt;artifactId&gt;spring-core&lt;/artifactId&gt;
    &lt;!-- spring的版本支持2.5及以上版本 --&gt;
    &lt;version&gt;3.2.8.RELEASE&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * 从证书文件加载公钥
     * &lt;p&gt;
     * 用于获取服务网关的公钥,用于发送数据加密和收到报文的验签
     * &lt;/p&gt;
     *
     * @param certFileUri 证书文件URI,支持物理路径和classpath:
     * @return JCE标准公钥对称, 用户后续加密和验签
     */
    public static PublicKey loadPublicKeyFromCert(String certFileUri) {
        InputStream in = null;
        try {
            Resource resource = new DefaultResourceLoader().getResource(certFileUri);
            in = resource.getInputStream();
            CertificateFactory cf = CertificateFactory.getInstance(&quot;X.509&quot;);
            X509Certificate x509 = (X509Certificate) cf.generateCertificate(in);
            return x509.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException(&quot;加载公钥失败:&quot; + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ig
                }

            }
        }
    }</code></pre>

            <h3 id="toc_38">加载私钥</h3>

            <p>第三发依赖包：</p>

            <p>这里依赖spring-core主要是使用Spring的Resource加载功能，可代替。</p>

            <pre class="line-numbers"><code class="language-markup">&lt;dependency&gt;
    &lt;groupId&gt;org.springframework&lt;/groupId&gt;
    &lt;artifactId&gt;spring-core&lt;/artifactId&gt;
    &lt;!-- spring的版本支持2.5及以上版本 --&gt;
    &lt;version&gt;3.2.8.RELEASE&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

            <p>参考代码</p>

            <pre class="line-numbers"><code class="language-java">    /**
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
            KeyStore keyStore = KeyStore.getInstance(&quot;PKCS12&quot;);
            Resource resource = new DefaultResourceLoader().getResource(keystoreUri);
            in = resource.getInputStream();
            keyStore.load(in, keystorePassword.toCharArray());

            Enumeration&lt;String&gt; enumas = keyStore.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
            }
            return (PrivateKey) keyStore.getKey(keyAlias, keystorePassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(&quot;通过keystore加载私钥失败:&quot; + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //ig
                }

            }
        }
    }</code></pre>

            <h3 id="toc_39">证书签名和验签</h3>

            <h4 id="toc_40">证书签名</h4>

            <p>商户端在发送报文前，需要使用<strong>商户证书的私钥</strong>对报文进行签名。</p>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * RSA私钥签名(使用商户证书私钥)
     *
     * @param waitToSignStr 待签字符串
     * @param privateKey    商户证书私钥
     * @return 签名base64编码结果
     */
    public static String signBase64(String waitToSignStr, PrivateKey privateKey) {
        try {
            byte[] dataBytes = waitToSignStr.getBytes(&quot;UTF-8&quot;);
            Signature signature = Signature.getInstance(&quot;SHA1withRSA&quot;);
            signature.initSign(privateKey);
            signature.update(dataBytes);
            byte[] result = signature.sign();
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            throw new RuntimeException(&quot;RAS私钥签名失败:&quot; + e.getMessage());
        }
    }</code></pre>

            <h4 id="toc_41">证书验签</h4>

            <p>商户端在收到网关报文（同步响应，同步跳转通知，异步通知）时，需要使用<strong>网关证书</strong>公钥对报文验签。</p>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * 公钥验签(使用网关证书公钥)
     *
     * @param waitToSignStr 待签字符串
     * @param signBase64    网关的签名
     * @param publicKey     网关的公钥
     * @return 验证结果 true:通过,false:未通过
     */
    public static boolean verifyBase64(String waitToSignStr, String signBase64, PublicKey publicKey) {
        try {
            byte[] dataBytes = waitToSignStr.getBytes(&quot;UTF-8&quot;);
            byte[] signBytes = Base64.decodeBase64(signBase64);
            Signature signature = Signature.getInstance(&quot;SHA1withRSA&quot;);
            signature.initVerify(publicKey);
            signature.update(dataBytes);
            return signature.verify(signBytes);
        } catch (Exception e) {
            throw new RuntimeException(&quot;RSA公钥验签失败: &quot; + e.getMessage());
        }
    }</code></pre>

            <h3 id="toc_42">对称加解密</h3>

            <h4 id="toc_43">AES加密</h4>

            <p>代码参考：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * ASE加密
     *
     * @param plainText 明文
     * @param secretKey 商户安全码
     * @return 密文
     */
    public static String AESEncrypt(String plainText, String secretKey) {
        try {
            // 使用商户安全码前16字节作为加密秘钥
            byte[] secretKeyBytes = secretKey.substring(0, 16).getBytes(&quot;UTF-8&quot;);
            // 明文数据
            byte[] plainBytes = plainText.getBytes(&quot;UTF-8&quot;);
            SecretKey key = new SecretKeySpec(secretKeyBytes, &quot;AES&quot;);
            // 获取Cipher对象较为耗费资源,如追求性能,请对cipher做缓存方案;
            // 通过算法/模式/填充获取cipher对象时,采用默认AES,表示: AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance(&quot;AES&quot;);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherBytes = cipher.doFinal(plainBytes);
            // 转换为base64(utf-8)字符串作为密文
            return Base64.encodeBase64String(cipherBytes);
        } catch (Exception e) {
            throw new RuntimeException(&quot;AES加密失败: &quot; + e.getMessage());
        }
    }</code></pre>

            <h4 id="toc_44">AES解密</h4>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * ASE解密
     *
     * @param cipherText 密文
     * @param secretKey  商户安全码
     * @return 明文
     */
    public static String AESDecrypt(String cipherText, String secretKey) {
        try {
            // 使用商户安全码前16字节作为加密秘钥
            byte[] secretKeyBytes = secretKey.substring(0, 16).getBytes(&quot;UTF-8&quot;);
            // 密文数据: base64(utf-8)字符串
            byte[] cipherBytes = Base64.decodeBase64(cipherText);
            SecretKey key = new SecretKeySpec(secretKeyBytes, &quot;AES&quot;);
            // 获取Cipher对象较为耗费资源,如追求性能,请对cipher做缓存方案;
            // 通过算法/模式/填充获取cipher对象时,采用默认AES,表示: AES/ECB/PKCS5Padding
            Cipher cipher = Cipher.getInstance(&quot;AES&quot;);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            // 转换为utf-8字符串则为明文
            return new String(cipherBytes, &quot;UTF-8&quot;);
        } catch (Exception e) {
            throw new RuntimeException(&quot;AES加密失败: &quot; + e.getMessage());
        }
    }</code></pre>

            <h3 id="toc_45">非对称加解密</h3>

            <h4 id="toc_46">加密</h4>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
     * 公钥加密
     *
     * @param plainText 明文
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encryptByPublicKey(String plainText, PublicKey publicKey) {
        ByteArrayOutputStream out = null;
        try {
            byte[] plainBytes = plainText.getBytes(&quot;UTF-8&quot;);
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
            while (inputLen - offSet &gt; 0) {
                if (inputLen - offSet &gt; plainLength) {
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
            throw new RuntimeException(&quot;公钥加密失败:&quot; + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    //ig
                }
            }
        }
    }</code></pre>

            <h4 id="toc_47">解密</h4>

            <p>参考代码：</p>

            <pre class="line-numbers"><code class="language-java">    /**
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
            while (inputLen - offSet &gt; 0) {
                if (inputLen - offSet &gt; keyLength) {
                    cache = cipher.doFinal(encryptedBytes, offSet, keyLength);
                } else {
                    cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * keyLength;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, &quot;UTF-8&quot;);
        } catch (Exception e) {
            throw new RuntimeException(&quot;私钥解密失败:&quot; + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    //ig
                }
            }
        }
    }</code></pre>


            <script type="text/javascript">
                self = "undefined" != typeof window ? window : "undefined" != typeof WorkerGlobalScope && self instanceof WorkerGlobalScope ? self : {};
                var Prism = function () {
                    var e = /\blang(?:uage)?-(?!\*)(\w+)\b/i, t = self.Prism = {
                        util: {
                            encode: function (e) {
                                return e instanceof n ? new n(e.type, t.util.encode(e.content), e.alias) : "Array" === t.util.type(e) ? e.map(t.util.encode) : e.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/\u00a0/g, " ")
                            }, type: function (e) {
                                return Object.prototype.toString.call(e).match(/\[object (\w+)\]/)[1]
                            }, clone: function (e) {
                                var n = t.util.type(e);
                                switch (n) {
                                    case"Object":
                                        var a = {};
                                        for (var r in e) e.hasOwnProperty(r) && (a[r] = t.util.clone(e[r]));
                                        return a;
                                    case"Array":
                                        return e.map(function (e) {
                                            return t.util.clone(e)
                                        })
                                }
                                return e
                            }
                        }, languages: {
                            extend: function (e, n) {
                                var a = t.util.clone(t.languages[e]);
                                for (var r in n) a[r] = n[r];
                                return a
                            }, insertBefore: function (e, n, a, r) {
                                r = r || t.languages;
                                var i = r[e];
                                if (2 == arguments.length) {
                                    a = arguments[1];
                                    for (var l in a) a.hasOwnProperty(l) && (i[l] = a[l]);
                                    return i
                                }
                                var s = {};
                                for (var o in i) if (i.hasOwnProperty(o)) {
                                    if (o == n) for (var l in a) a.hasOwnProperty(l) && (s[l] = a[l]);
                                    s[o] = i[o]
                                }
                                return t.languages.DFS(t.languages, function (t, n) {
                                    n === r[e] && t != e && (this[t] = s)
                                }), r[e] = s
                            }, DFS: function (e, n, a) {
                                for (var r in e) e.hasOwnProperty(r) && (n.call(e, r, e[r], a || r), "Object" === t.util.type(e[r]) ? t.languages.DFS(e[r], n) : "Array" === t.util.type(e[r]) && t.languages.DFS(e[r], n, r))
                            }
                        }, highlightAll: function (e, n) {
                            for (var a, r = document.querySelectorAll('code[class*="language-"], [class*="language-"] code, code[class*="lang-"], [class*="lang-"] code'), i = 0; a = r[i++];) t.highlightElement(a, e === !0, n)
                        }, highlightElement: function (a, r, i) {
                            for (var l, s, o = a; o && !e.test(o.className);) o = o.parentNode;
                            if (o && (l = (o.className.match(e) || [, ""])[1], s = t.languages[l]), a.className = a.className.replace(e, "").replace(/\s+/g, " ") + " language-" + l, o = a.parentNode, /pre/i.test(o.nodeName) && (o.className = o.className.replace(e, "").replace(/\s+/g, " ") + " language-" + l), s) {
                                var u = a.textContent;
                                if (u) {
                                    u = u.replace(/^(?:\r?\n|\r)/, "");
                                    var g = {element: a, language: l, grammar: s, code: u};
                                    if (t.hooks.run("before-highlight", g), r && self.Worker) {
                                        var c = new Worker(t.filename);
                                        c.onmessage = function (e) {
                                            g.highlightedCode = n.stringify(JSON.parse(e.data), l), t.hooks.run("before-insert", g), g.element.innerHTML = g.highlightedCode, i && i.call(g.element), t.hooks.run("after-highlight", g)
                                        }, c.postMessage(JSON.stringify({language: g.language, code: g.code}))
                                    } else g.highlightedCode = t.highlight(g.code, g.grammar, g.language), t.hooks.run("before-insert", g), g.element.innerHTML = g.highlightedCode, i && i.call(a), t.hooks.run("after-highlight", g)
                                }
                            }
                        }, highlight: function (e, a, r) {
                            var i = t.tokenize(e, a);
                            return n.stringify(t.util.encode(i), r)
                        }, tokenize: function (e, n) {
                            var a = t.Token, r = [e], i = n.rest;
                            if (i) {
                                for (var l in i) n[l] = i[l];
                                delete n.rest
                            }
                            e:for (var l in n) if (n.hasOwnProperty(l) && n[l]) {
                                var s = n[l];
                                s = "Array" === t.util.type(s) ? s : [s];
                                for (var o = 0; o < s.length; ++o) {
                                    var u = s[o], g = u.inside, c = !!u.lookbehind, f = 0, h = u.alias;
                                    u = u.pattern || u;
                                    for (var p = 0; p < r.length; p++) {
                                        var d = r[p];
                                        if (r.length > e.length) break e;
                                        if (!(d instanceof a)) {
                                            u.lastIndex = 0;
                                            var m = u.exec(d);
                                            if (m) {
                                                c && (f = m[1].length);
                                                var y = m.index - 1 + f, m = m[0].slice(f), v = m.length, k = y + v, b = d.slice(0, y + 1),
                                                        w = d.slice(k + 1), N = [p, 1];
                                                b && N.push(b);
                                                var O = new a(l, g ? t.tokenize(m, g) : m, h);
                                                N.push(O), w && N.push(w), Array.prototype.splice.apply(r, N)
                                            }
                                        }
                                    }
                                }
                            }
                            return r
                        }, hooks: {
                            all: {}, add: function (e, n) {
                                var a = t.hooks.all;
                                a[e] = a[e] || [], a[e].push(n)
                            }, run: function (e, n) {
                                var a = t.hooks.all[e];
                                if (a && a.length) for (var r, i = 0; r = a[i++];) r(n)
                            }
                        }
                    }, n = t.Token = function (e, t, n) {
                        this.type = e, this.content = t, this.alias = n
                    };
                    if (n.stringify = function (e, a, r) {
                                if ("string" == typeof e) return e;
                                if ("Array" === t.util.type(e)) return e.map(function (t) {
                                    return n.stringify(t, a, e)
                                }).join("");
                                var i = {
                                    type: e.type,
                                    content: n.stringify(e.content, a, r),
                                    tag: "span",
                                    classes: ["token", e.type],
                                    attributes: {},
                                    language: a,
                                    parent: r
                                };
                                if ("comment" == i.type && (i.attributes.spellcheck = "true"), e.alias) {
                                    var l = "Array" === t.util.type(e.alias) ? e.alias : [e.alias];
                                    Array.prototype.push.apply(i.classes, l)
                                }
                                t.hooks.run("wrap", i);
                                var s = "";
                                for (var o in i.attributes) s += o + '="' + (i.attributes[o] || "") + '"';
                                return "<" + i.tag + ' class="' + i.classes.join(" ") + '" ' + s + ">" + i.content + "</" + i.tag + ">"
                            }, !self.document) return self.addEventListener ? (self.addEventListener("message", function (e) {
                        var n = JSON.parse(e.data), a = n.language, r = n.code;
                        self.postMessage(JSON.stringify(t.util.encode(t.tokenize(r, t.languages[a])))), self.close()
                    }, !1), self.Prism) : self.Prism;
                    var a = document.getElementsByTagName("script");
                    return a = a[a.length - 1], a && (t.filename = a.src, document.addEventListener && !a.hasAttribute("data-manual") && document.addEventListener("DOMContentLoaded", t.highlightAll)), self.Prism
                }();
                "undefined" != typeof module && module.exports && (module.exports = Prism);
            </script>
            <script type="text/javascript">
                Prism.languages.clike = {
                    comment: [{pattern: /(^|[^\\])\/\*[\w\W]*?\*\//, lookbehind: !0}, {
                        pattern: /(^|[^\\:])\/\/.*/,
                        lookbehind: !0
                    }],
                    string: /("|')(\\[\s\S]|(?!\1)[^\\\r\n])*\1/,
                    "class-name": {
                        pattern: /((?:(?:class|interface|extends|implements|trait|instanceof|new)\s+)|(?:catch\s+\())[a-z0-9_\.\\]+/i,
                        lookbehind: !0,
                        inside: {punctuation: /(\.|\\)/}
                    },
                    keyword: /\b(if|else|while|do|for|return|in|instanceof|function|new|try|throw|catch|finally|null|break|continue)\b/,
                    "boolean": /\b(true|false)\b/,
                    "function": {pattern: /[a-z0-9_]+\(/i, inside: {punctuation: /\(/}},
                    number: /\b-?(0x[\dA-Fa-f]+|\d*\.?\d+([Ee]-?\d+)?)\b/,
                    operator: /[-+]{1,2}|!|<=?|>=?|={1,3}|&{1,2}|\|?\||\?|\*|\/|~|\^|%/,
                    ignore: /&(lt|gt|amp);/i,
                    punctuation: /[{}[\];(),.:]/
                };
            </script>
            <script type="text/javascript">
                Prism.languages.java = Prism.languages.extend("clike", {
                    keyword: /\b(abstract|continue|for|new|switch|assert|default|goto|package|synchronized|boolean|do|if|private|this|break|double|implements|protected|throw|byte|else|import|public|throws|case|enum|instanceof|return|transient|catch|extends|int|short|try|char|final|interface|static|void|class|finally|long|strictfp|volatile|const|float|native|super|while)\b/,
                    number: /\b0b[01]+\b|\b0x[\da-f]*\.?[\da-fp\-]+\b|\b\d*\.?\d+[e]?[\d]*[df]\b|\b\d*\.?\d+\b/i,
                    operator: {
                        pattern: /(^|[^\.])(?:\+=|\+\+?|-=|--?|!=?|<{1,2}=?|>{1,3}=?|==?|&=|&&?|\|=|\|\|?|\?|\*=?|\/=?|%=?|\^=?|:|~)/m,
                        lookbehind: !0
                    }
                });
            </script>
            <script type="text/javascript">
                Prism.languages.markup = {comment: / <!--[\w\W]*?-->/,prolog:/ <\?.
                + ?\?
                >/,
                doctype:/<!DOCTYPE.+?>/, cdata
                :
                /<!\[CDATA\[[\w\W]*?]]>/i, tag
                :
                {
                    pattern:/<\/?[^\s>\/]+(?:\s+[^\s>\/=]+(?:=(?:("|')(?:\\\1|\\?(?!\1)[\w\W])*\1|[^\s'">=]+))?)*\s*\/?>/i, inside
                :
                    {
                        tag:{
                            pattern:/^<\/?[^\s>\/]+/i, inside
                        :
                            {
                                punctuation:/^<\/?/, namespace
                            :
                                /^[^\s>\/:]+:/
                            }
                        }
                    ,
                        "attr-value"
                    :
                        {
                            pattern:/=(?:('|")[\w\W]*?(\1)|[^\s>]+)/i, inside
                        :
                            {
                                punctuation:/=|>|"/
                            }
                        }
                    ,
                        punctuation:/\/?>/, "attr-name"
                    :
                        {
                            pattern:/[^\s>\/]+/, inside
                        :
                            {
                                namespace:/^[^\s>\/:]+:/
                            }
                        }
                    }
                }
                ,
                entity:/&#?[\da-z]{1,8};/i
                },
                Prism.hooks.add("wrap", function (t) {
                    "entity" === t.type && (t.attributes.title = t.content.replace(/&amp;/, "&"))
                });
            </script>
            <script type="text/javascript">
                Prism.languages.http = {
                    "request-line": {
                        pattern: /^(POST|GET|PUT|DELETE|OPTIONS|PATCH|TRACE|CONNECT)\b\shttps?:\/\/\S+\sHTTP\/[0-9.]+/,
                        inside: {property: /^\b(POST|GET|PUT|DELETE|OPTIONS|PATCH|TRACE|CONNECT)\b/, "attr-name": /:\w+/}
                    },
                    "response-status": {pattern: /^HTTP\/1.[01] [0-9]+.*/, inside: {property: /[0-9]+[A-Z\s-]+$/i}},
                    keyword: /^[\w-]+:(?=.+)/m
                };
                var httpLanguages = {
                    "application/json": Prism.languages.javascript,
                    "application/xml": Prism.languages.markup,
                    "text/xml": Prism.languages.markup,
                    "text/html": Prism.languages.markup
                };
                for (var contentType in httpLanguages) if (httpLanguages[contentType]) {
                    var options = {};
                    options[contentType] = {
                        pattern: new RegExp("(content-type:\\s*" + contentType + "[\\w\\W]*?)\\n\\n[\\w\\W]*", "i"),
                        lookbehind: !0,
                        inside: {rest: httpLanguages[contentType]}
                    }, Prism.languages.insertBefore("http", "keyword", options)
                }
            </script>
            <script type="text/javascript">
                Prism.hooks.add("after-highlight", function (e) {
                    var t = e.element.parentNode, s = /\s*\bline-numbers\b\s*/;
                    if (t && /pre/i.test(t.nodeName) && (s.test(t.className) || s.test(e.element.className))) {
                        s.test(e.element.className) && (e.element.className = e.element.className.replace(s, "")), s.test(t.className) || (t.className += " line-numbers");
                        var a, n = 1 + e.code.split("\n").length, l = new Array(n);
                        l = l.join("<span></span>"), a = document.createElement("span"), a.className = "line-numbers-rows", a.innerHTML = l, t.hasAttribute("data-start") && (t.style.counterReset = "linenumber " + (parseInt(t.getAttribute("data-start"), 10) - 1)), e.element.appendChild(a)
                    }
                });
            </script>


        </div>
    </div>
</div>
<!--end-->

<!--底部-->
		<@includePage path="/docs/common/footer.html"/>
<!--end-->
<script>
    layer.open({
        type: 1,
        title: '目录',
        skin: 'layui-layer-dir',
        offset: [($(window).height() - 500) / 2, ($(window).width() - 165)],
        area: ['150px', '500px'],
        shade: 0,
        content: $('#spec_dir').html()
    });
</script>
</body>
</html>
