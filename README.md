#统一处理异常框架工具
##1、quick start
首先我们引入unify-exception-handler-1.0.jar包到lib文件中，
再在配置文件application.properties中写入
```propertie
unify.exception-handler.enable = true
```
ok这样就搞定了！就可以来测试一下是否有用！先编写一个controller类
```java
@RestController
public class api{
    @GetMapping("/test")
        public void test(){
            throw new RuntimeException("我是运行异常");
        }
}
```
直接运行并访问http://localhost:8080/test
在浏览器端得到返回
![](https://tva1.sinaimg.cn/large/008eGmZEly1gmnjp6vlnlj31fw0k6adb.jpg)
##2、深入了解
我们定义了一个Result类来封装信息
```java
class Result<T>{
    Integer code;
    //错误信息
    String message;
    //返回的数据信息
    T data;
}
```
此工具默认返回的是JSON格式的数据信息，但是可以通过在配置文件中配置
```propertie
unify.exception-handler.handler-type = view
unify,exception-handler.view-path = classpath:/templates/error.html
```
来开启返回对应错误页面 默认页面是为resources下templates目录中的error.html
我们在次启动访问http://localhost:8080/test
![](https://tva1.sinaimg.cn/large/008eGmZEly1gmnkgqykx2j326w0g0gok.jpg)
我们也可以自己定义我们错误页面格式
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>错误页面</title>
    <style>
        .exception_body{
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }
        .exception_tip{
            font-size: 41px;
            color: crimson;
        }
    </style>
</head>
<body>
<div class="exception_body">
    <div class="exception_tip">错误页面</div>
    <div>
        <p th:text="${result.message} +':'+ ${result.data} + '!'"/>
    </div>
</div>

</body>
</html>
```
#3、自定义异常及返回信息
> 自定义异常

首先我们需要创建一个自己的异常类继承基类ExceptionStrategy
```java
public class MyException extends ExceptionStrategy {
    private IEnum iEnum;
    private String message;
    public MyException(IEnum iEnum,String message){
        super(message);
        this.iEnum = iEnum;
        this.message = message;

    }
    public MyException(IEnum iEnum) {
        super();
        this.iEnum = iEnum;
    }
    public IEnum getEnumStrategy(){
        return this.iEnum;
    }
}
```
> 自定义返回枚举

其次我们在创建一个自己的错误返回枚举类继承IEnum
```java
public enum MyEnum implements IEnum {
    MY_EXCEPTION(3,"我的异常");
    private Integer code;
    private String value;
     MyEnum(Integer code,String value){
        this.code = code;
        this.value = value;
    }
    public Integer getCode(){
        return this.code;
    }
    public  String getValue(){
        return this.value;
    }
}
```
> 访问结果

我们最后把自己的异常在api类中修改为如下代码：
```java
@RestController
public class api{
    @GetMapping("/test")
        public void test(){
            throw new MyException(MyEnum.MY_EXCEPTION,"抛一个漂亮的错误");
        }
}
```
OK启动并访问 http://localhost:8080/test
![](https://tva1.sinaimg.cn/large/008eGmZEly1gmnl9b0jw0j318c08it9u.jpg)

