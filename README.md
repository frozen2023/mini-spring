# mini-spring项目文档
- 目录
  - [项目介绍](#项目介绍)
  - [项目结构](#项目结构)
  - [IOC容器](#IOC容器)
  - [ApplicationContext](#ApplicationContext)
  - [SpringMVC](#SpringMVC)
  - [循环依赖](#循环依赖)

#### 项目介绍
> 实现简单的spring框架，完成了ioc容器创建，实现了xml方式和注解两种方式注册bean，部分springaop功能， 以及springmvc模块

#### 项目结构
```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─chen
│  │  │      └─springframework
│  │  │          ├─aop  ---- (springaop模块)
│  │  │          │  ├─aspectj
│  │  │          │  ├─framework
│  │  │          │  │  ├─adapter
│  │  │          │  │  └─autoproxy
│  │  │          │  ├─support
│  │  │          │  └─target
│  │  │          ├─beans  ---- (ioc容器与beanFactory模块)
│  │  │          │  └─factory
│  │  │          │      ├─annotation
│  │  │          │      ├─config
│  │  │          │      ├─support
│  │  │          │      └─xml
│  │  │          ├─context  ---- (ApplicationContext模块)
│  │  │          │  ├─annotation
│  │  │          │  ├─event
│  │  │          │  └─support
│  │  │          ├─controller  ---- (这个不是功能模块,只是为了测试)
│  │  │          ├─core		---- (核心模块,主要实现了xml资源的读取)
│  │  │          │  ├─io
│  │  │          │  └─type
│  │  │          │      └─classreading
│  │  │          ├─stereotype  ---- (一些Bean的注解)
│  │  │          ├─util  ---- (一些工具类)
│  │  │          ├─web  ---- (WebApplicationContext模块)
│  │  │          │  ├─bind
│  │  │          │  │  └─annotation
│  │  │          │  ├─context
│  │  │          │  │  ├─request
│  │  │          │  │  └─support
│  │  │          │  └─method
│  │  │          │      └─support
│  │  │          └─webmvc  ---- (springmvc核心模块)
│  │  │              └─servlet
│  │  │                  ├─handler
│  │  │                  └─mvc
│  │  │                      └─method
│  │  │                          └─annotation
│  │  └─resources
│  │      └─chen
│  │          └─springframework
│  │              └─webmvc
│  │                  └─servlet
│  └─test
│      └─java
│          └─test
│              └─chen
│                  └─springframework
│                      ├─beans
│                      ├─context
│                      ├─controller
│                      └─core
│                          └─io

```
####  IOC容器
实现IOC容器最核心的类就是BeanFactory，整个IOC容器就是基于BeanFactory的实现类及其扩展类实现的。
 ```java
 public interface BeanFactory {  
  
    Object getBean(String name) throws BeansException;  
  
    Object getBean(String name, Object... args) throws BeansException;  
  
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;  
  
    <T> T getBean(Class<T> requiredType) throws BeansException;  
  
    boolean isSingleton(String name);  
  
    boolean isTypeMatch(String name, Class<?> typeToMatch);  
}
 ```
该接口中定义了核心方法getBean,而真正的方法交由其子类实现，Spring在实现这个类时扩展了许多类，每个类都实现了一部分功能，通过层层继承与实现，最终得到了具备完整功能的DefaultListableBeanFactory，例如AbstractBeanFactory类就实现了getBean方法的模板，同时定义了createBean交给子类实现，AbstractAutowireCapableBeanFactory实现了通过BeanDefinition创建实例的功能，其子类DefaultListableBeanFactory则实现BeanDefinition的注册和获取功能，可以说每个类的分工明确。同时，Spring对外界还提供了许多扩展的方法，例如BeanPostProcessor，BeanFactoryPostProcessor，InstantiationAwareBeanPostProcessor等，以及提供了获取Spring提供的资源的途径(即各种Aware)，这些扩展接口的方法均已融入Bean的生命周期中，允许用户在Bean的生产过程中进行操作，可扩展性极强。

####  ApplicationContext
ApplicationContext字面意思就是应用上下文，具有扫描Bean定义，注册Bean定义，获取Bean的功能
```java
public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {  
    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;  
}
```
因为继承了ListableBeanFactory，所有有getBean的能力，而其自身及实现类和子类则负责扫描和注册BeanDefinition。下面是AbstractApplicationContext中所实现的refresh方法:
```java
public void refresh() throws BeansException {  
  
    ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();  // 获取BeanFactory 交由子类实现
  
    prepareBeanFactory(beanFactory);  // BeanFactory 的初始化工作
  
    postProcessBeanFactory(beanFactory);  // 提供给子类扩展的方法
  
    invokeBeanFactoryPostProcessors(beanFactory);  // 执行所有BeanFactoryPostProcessor 
  
    registerBeanPostProcessors(beanFactory); // 注册所有BeanPostProcessor
  
    initApplicationEventMulticaster(); // 初始化事件广播器 
  
    registerListeners();  // 注册监听器
  
    finishBeanFactoryInitialization(beanFactory); // 结束初始化  
}

protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {  
    refreshBeanFactory();  
    return getBeanFactory();  
}
```
可以看到BeanFactoryPostProcessor的执行时机以及其他组件的初始化时机。而获取BeanFactory的工作则是交由子类AbstractRefreshableApplicationContext实现
```java
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {  
  
    private DefaultListableBeanFactory beanFactory;  
  
    @Override  
    protected void refreshBeanFactory() throws BeansException {  
        DefaultListableBeanFactory beanFactory = createBeanFactory();  
        loadBeanDefinitions(beanFactory);  
        this.beanFactory = beanFactory;  
    }  
  
    @Override  
    protected ConfigurableListableBeanFactory getBeanFactory() {  
        DefaultListableBeanFactory beanFactory = this.beanFactory;  
        if (beanFactory == null) {  
            throw new IllegalStateException("BeanFactory not initialized or already closed - " +  
                    "call 'refresh' before accessing beans via the ApplicationContext");  
        }  
        return beanFactory;  
    }  
  
    protected DefaultListableBeanFactory createBeanFactory() {  
        return new DefaultListableBeanFactory();  
    }  
  
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);  
}
```
将加载BeanDefinitions的工作再交由子类实现，不同的ApplicationContext有不同的实现。而在ClassPathXmlApplicationContext中的实现如下
```java
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {  
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);  
  
    beanDefinitionReader.setResourceLoader(this);  
  
    initBeanDefinitionReader(beanDefinitionReader);  
    loadBeanDefinitions(beanDefinitionReader);  
}

protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) {  
    String[] configLocations = getConfigLocations();  
    if (configLocations != null) {  
        for (String configLocation : configLocations) {  
            reader.loadBeanDefinitions(configLocation);  
        }  
    }  
}
```
就是利用XmlBeanDefinitionReader去读取指定xml文件里的BeanDefinition。在ClassPathXmlApplicationContext的构造函数中调用了父类的refresh方法，进行了容器的初始化。

####  SpringMVC
实现SpringMVC，==DispatcherServlet==是重中之重。DispatcherServlet是什么？就是一个普通的Servlet。但是所有请求都会经过它，由它来分发请求。DispatcherServlet会扫描指定配置文件，并找出所有的Controller,并解析所有请求路径与方法的对应关系。Tomcat在启动时会自动调用Servlet的init()方法，在其父类HttpServletBean中实现的init方法如下：
```java
public abstract class HttpServletBean extends HttpServlet {  
  
    @Override  
    public void init() throws ServletException {  
        setPropertyValues();  
        initServletBean();  
    }  
  
    protected void initServletBean() throws ServletException {  
    }  
  
    protected void setPropertyValues() {  
        ServletConfig servletConfig = getServletConfig();  
        String contextConfigLocation = servletConfig.getInitParameter("contextConfigLocation");  
        BeanUtil.setFieldValue(this, "contextConfigLocation", contextConfigLocation);  
    }
```
一是完成web.xml定义的参数的注入，二是进行Servlet及WebApplicationContext的初始化。WebApplicationContext的初始化实际上是根据web.xml中定义的参数contextConfigLocation去创建一个WebApplicationContext，并调用其refresh方法完成BeanDefinition的加载，Servlet的初始化则是加载一些组件。

```java
protected void initStrategies(ApplicationContext context) {  
    // 初始化mvc组件  
    initMultipartResolver(context);  
    initLocaleResolver(context);  
    initThemeResolver(context);  
    initHandlerMappings(context);  
    initHandlerAdapters(context);  
    initHandlerExceptionResolvers(context);  
    initRequestToViewNameTranslator(context);  
    initViewResolvers(context);  
    initFlashMapManager(context);  
}
```
在进行组件加载时，先去BeanFactroy中查找，如果没找到，会加载默认的组件 。
```java
protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {  
    if (defaultStrategies == null) {  
        try {  
            Properties properties = new Properties();  
            InputStream is = DispatcherServlet.class.getResourceAsStream(DEFAULT_STRATEGIES_PATH);  
            properties.load(is);  
            defaultStrategies = properties;  
        } catch (IOException ex) {  
            throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " + ex.getMessage());  
        }  
    }  
  
    String key = strategyInterface.getName();  
    String value = defaultStrategies.getProperty(key);  
  
    if (value != null) {  
        String[] classNames = value.split(",");  
        List<T> strategies = new ArrayList<>(classNames.length);  
        for (String className : classNames) {  
            try {  
                Class<?> beanClass = Class.forName(className);  
                Object defaultStrategy = createDefaultStrategy(context, beanClass);  
                strategies.add((T) defaultStrategy);  
            } catch (ClassNotFoundException e) {  
                throw new RuntimeException("Could not find DispatcherServlet's default strategy class [" + className +  
                        "] for interface [" + key + "]", e);  
            }  
        }  
        return strategies;  
    } else {  
        return Collections.emptyList();  
    }  
}
```
去同目录下的DispatcherServlet.properties文件下读取组件名。
```properties
chen.springframework.webmvc.servlet.HandlerMapping=chen.springframework.webmvc.servlet.mvc.method.annotation.RequestMappingHandlerMapping  
  
chen.springframework.webmvc.servlet.HandlerAdapter=chen.springframework.webmvc.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
```
到此初始化完成。在处理请求时，doGet,doPost方法最终都会来到DispatcherServlet的doDispatch方法。在doDispatch中真正进行请求的分发和处理。

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {  
    HandlerExecutionChain mappedHandler = null;  
    ModelAndView mv = null;  
  
    mappedHandler = getHandler(request); // 获取处理器执行链
  
    if (mappedHandler == null) {  
        return;  
    }  
  
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler()); //获取处理器适配器 
  
    if (!mappedHandler.applyPreHandle(request, response)) {  
        return;  
    }   // 执行拦截器前置方法
  
    mv = ha.handle(request, response, mappedHandler.getHandler());  // 真正进行请求的处理
  
    mappedHandler.applyPostHandle(request, response, mv);// 执行拦截器后置方法  
  
    processDispatchResult(request, response, mappedHandler, mv); //  进行视图解析
  
}
```
getHandler就是通过HandlerMapping去产生一个HandlerExecutionChain，即带拦截器的处理器执行链，而这里的HandlerMapping我只写了一个RequestMappingHandlerMapping，这个处理器映射器会解析所有Controller里的@RequestMapping方法(初始化阶段)，然后生成一个uri与Method的对应关系(一个Map),在处理请求时，根据uri返回一个方法，这个方法就是Handler，然后交给
RequestMappingHandlerAdapter，并由适配器执行最终的处理。
下面是适配器处理的流程：
```java
@Override  
protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {  
    ModelAndView mav;  
    mav = invokeHandlerMethod(request, response, handlerMethod);  
    return mav;  
}  
  
@Override  
protected boolean supportsInternal(HandlerMethod handlerMethod) {  
    return true;  
}  
  
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,  
                                           HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {  
    ServletWebRequest webRequest = new ServletWebRequest(request, response);  
    ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);  
    if (this.returnValueHandlers != null) {  
        invocableMethod.setReturnValueHandlers(this.returnValueHandlers);  
    }  
    ModelAndView mav = new ModelAndView();  
    invocableMethod.invokeAndHandle(webRequest, mav);  
    if (mav.isRequestHandled()) {  
        return null;  
    }  
    return mav;  
}
```
就是先生成一个可执行的ServletInvocableHandlerMethod，通过这个方法来执行。ServletInvocableHandlerMethod在执行时会调用许多处理器，我这里就实现了一个混合返回值处理器HandlerMethodReturnValueHandlerComposite，在这个处理器的handleReturnValue方法中，会根据其内部所有返回值处理器找出最适配的一个，而这里我就实现了一个RequestResponseBodyMethodProcessor，这个处理器适配方法或者类上有@ResponseBody的注解的方法，在其handleReturnValue方法中，会将方法执行后的返回值直接通过response写回，并标记请求已处理，当请求被标记为已处理时，适配器的handle方法就会返回空的ModelAndView，就没有后续的视图解析了。这就是@RestController或者@ResponseBody直接返回json字符串的原理。(@RestController时@Controller和@ResponseBody的混合注解)

我没有实现视图解析器，因为我感觉现在基本都是前后端分离，后端很少会管视图跳转了(还有就是时间不够)

####  循环依赖
>循环依赖是什么?
>
循环依赖就是两个或两个以上的Bean互相持有对方，形成一个闭环。
![输入图片说明](/images/循环依赖.1.png)
>Spring是如何解决循环依赖的?

采用三级缓存。下面是定义在DefaultSingletonBeanRegistry类中的三级缓存:
```java
private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);  // 一级缓存
  
private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);  // 二级缓存
  
private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16); // 三级缓存
```
再看获取单例Bean的核心方法:
```java
protected Object getSingleton(String beanName, boolean allowEarlyReference) {  
    Object singletonObject = this.singletonObjects.get(beanName); // 从一级缓存中获取  
    if (singletonObject == null) {  
        singletonObject = this.earlySingletonObjects.get(beanName);  // 从二级缓存中获取
        if (singletonObject == null && allowEarlyReference) {  
            synchronized (this.singletonObjects) {  
                singletonObject = this.singletonObjects.get(beanName);  
                if (singletonObject == null) {  
                    singletonObject = this.earlySingletonObjects.get(beanName);  
                    if (singletonObject == null) {  
                        ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);  
                        if (singletonFactory != null) {  
                            singletonObject = singletonFactory.getObject();  // 从三级缓存中获取
                            this.earlySingletonObjects.put(beanName, singletonObject);  
                            this.singletonFactories.remove(beanName);  
                        }  
                    }  
                }  
            }  
        }  
    }  
    return singletonObject;  
}
```
可以看到获取Bean的顺序是从一级到三级，从三级缓存(ObjectFactory)中获取到的Bean会被存入二级缓存。

>为什么三级缓存能解决循环依赖问题

来看看AbstractAutowireCapableBeanFactory的doCreateBean方法:

```java
protected Object doCreateBean(String beanName, BeanDefinition bd, Object[] args)  
        throws BeansException {  
  
    Object bean;  
    Object exposedObject;  
    try {  
        bean = createBeanInstance(beanName, bd, args);  
  
        boolean earlySingletonExposure = (bd.isSingleton() && this.allowCircularReferences &&  
                isSingletonCurrentlyInCreation(beanName));  
        if (earlySingletonExposure) {  
            Object finalBean = bean;  
            addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, bd, finalBean));  
        }  
  
        exposedObject = bean;  
        populateBean(beanName, bd, bean);  
        exposedObject = initializeBean(beanName, exposedObject, bd);  
  
  
        if (earlySingletonExposure) {  
            // 从二级缓存中获取代理对象  
            Object earlySingletonReference = getSingleton(beanName, false);  
            if (earlySingletonReference != null) {  
                if (exposedObject == bean) {  
                    exposedObject = earlySingletonReference;  
                }  
            }  
        }  
    } catch (Exception e) {  
        throw new BeansException("Instantiation of bean failed", e);  
    }  
  
    registerDisposableBeanIfNecessary(beanName, bean, bd);  
  
    return exposedObject;  
}
```
在创建完实例之后(内部属性值并未被注入)，往三级缓存中添加了一个ObjectFactory，这个ObjectFactory的getObject方法会返回这个初始的实例。
假如有两个Bean，A和B，A内部引用了B，B内部引用了A，在进行A的创建时，首先将获得A的初始实例的Object Factory存入三级缓存，然后进行A的属性填充，发现引用了B，于是去创建B，在进行B的属性填充时，发现引用了A，于是从三级缓存中获取到了A的初始实例，于是B的最终实例就创建成功了，A获得到了B的最终实例，于是A的最终实例也创建完成了。
但是有一个问题，上面解决的关键是先创建一个初始实例，如果A在构造方法里引用了B，A的初始实例就无法创建，这种方案就不可行了。当然Spring也提供了新的解决方案，比如懒加载(先注入代理对象，首次使用时再创建对象完成注入)，只是我还没有实现。

>二级缓存的作用？

上述提到的解决方案中貌似只用到了三级缓存，那为什么还要设置一个二级缓存呢？

假设有这样一种情况
![输入图片说明](/images/循环依赖.2.png)
在有代理的情况下，怎么保证引用的A是同一个对象?
进行A的创建，发现A引用B，进行B的创建，有代理的情况下，获取A实例的Object Factory将返回A的代理对象，于是B注入了A的代理对象proxyA，并将proxyA 放入二级缓存中，完成B最终实例的创建，又回到A的创建，发现A引用C，进行C的创建，C在属性注入时发现了二级缓存中B引用的proxyA，于是注入这个proxy A，因此B，C引用的是同一个代理对象。如果没有二级缓存，C就要从三级缓存中获取新的代理对象了。




 


