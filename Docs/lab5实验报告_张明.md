## 张明 lab5实验总结

### 1、负责任务

（1）微服务框架的搭建

（2）所有新增微服务的单元测试

### 2、微服务框架

在lab4的研究的基础上，我们成功地搭建了基于Eureka平台的微服务系统，并采用RestTemplate作为服务间通信的手段。整个微服务结构展示在uml图中，主要的service包含了UserService，BookService，violaitonService，CommentService以及LogService。服务之间通过RestTemplate进行调用。

### 3、单元测试

在进行本lab的单元测试工作时，因为涉及到了服务之间的调用，我主要采用了Mock测试的方法进行单元测试，在数据层面之外进行了后端方法功能的逻辑测试。以BookServiceTests为例：

```java
@Mock
private BookRepository bookRepository;
@Mock
private CopyRepository copyRepository;

@Mock
private RestTemplate restTemplate;

@InjectMocks
private BookService bookService;
@InjectMocks
private CopyService copyService;
```

通过@Mock注解，虚假声明一个BookRepository，同时通过@InjectMocks注解，声明mock注入的服务

通过Mockito.when().thenReturn()方法，来控制service中的方法执行到mockRepository时的返回值，具体实现可以看这个例子：

```java
@Test
void getBookByRightISBN(){
    String isbn="1000000000";
    Book mockBook = new Book();
    mockBook.setISBN(isbn);
    Mockito.when(bookRepository.findBookByISBN(isbn)).thenReturn(mockBook);
    Mockito.when(copyRepository.countCopiesByISBN(isbn)).thenReturn(5);
    Mockito.when(copyRepository.countCopiesByISBNAndStatus(isbn,"在库")).thenReturn(3);
    HashMap<String, Object> map = bookService.getBookByISBN(isbn).getMap();
    assertEquals("获取书籍成功",map.get("message"));
    System.out.println(bookService.getBookByISBN(isbn).getMap());
}
```

可以看到，Mockito方法让被mock注解过的bookRepository在执行findBookByISBN(isbn)时return到一个自定义的mockBook上，这样就可以手动控制数据库返回的结果，进行不同情况的测试。

但这样的测试缺乏了对数据库方法层面的确认，如果在不相信JPA的功能的时候，还需要进行一定的集成测试的工作。

最后，感谢三位组员的付出！
