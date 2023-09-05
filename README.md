# JAVA web server 구현

### 간략한 설명
* JAVA Socket 클래스를 활용한 web server 구현

<br>

![socket](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcSH8fU%2FbtqvxsTPQ2E%2Fpnl61uUJOAPdf73whDlTW0%2Fimg.png)

<br>

### JAVA 입출력 스트림(Input/Output Stream)

---

#### IO 패키지
   * 프로그램에서는 데이터를 외부에서 읽고 다시 외부로 출력하는 작업이 빈번히 일어난다.
   * 사용자로부터 키보드를 통해 입력될 수도 있고, 파일 또는 네트워크로부터 입력될 수도 있다.
   * 반대로 모니터로 출력될 수도 있고, 파일로 출력되어 저장될 수도 있다.

   <br>

#### 입력 스트림과 출력 스트림
   * 프로그램이 출발지냐 또는 도착지냐에 따라서 스트림의 종류가 결정되는데, 항상 프로그램을 기준으로 데이터가 들어오면 입력 스트림이고, 데이터가 나가면 출력 스트림이다.
   * 네트워크상의 다른 프로그램과 데이터 교환을 하기 위해서는 양쪽 모두 입력/출력 스트림이 따로 필요하다.
   * 스트림의 특성이 단방향이므로 하나의 스트림으로 입력과 출력을 모두 할 수 없다.

![input/output stream](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbvH00I%2FbtqydR4e1Ju%2FDaCDExX0OKOaZquiHWzWLK%2Fimg.png)

<br>

#### 스트림 클래스
   * 스트림 클래스는 크게 두 종류로 구분된다. 하나는 바이트(byte) 기반 스트림이고, 다른 하나는 문자(character) 기반 스트림이다.
   * 바이트 기반 스트림(Input/OutputStream)은 그림, 멀티미디어 등 모든 종류의 데이터를 받고 보낼 수 있다.
   * 문자 기반 스트림(Reader/Writer)은 문자만 받고 보낼 수 있다.

   <br>

#### InputStream 기본 메소드
1. read()
   * 입력 스트림으로부터 1바이트를 읽고, 4바이트 int 타입으로 리턴한다. 따라서 int 타입의 4바이트 중 마지막 1바이트에만 데이터가 저장된다. 
   * 더이상 읽어올 데이터가 없다면 -1을 리턴한다. 

2. read(byte[] b)
   * 입력 스트림으로부터 읽은 바이트를 매개값으로 주어진 바이트 배열 b에 저장하고 실제로 읽은 바이트 수를 리턴한다.

3. close()
   * InputStream을 더 이상 사용하지 않을 경우에는 close() 메소드로 사용했던 시스템 자원을 풀어준다.

```
   InputStream is = new FileInputStream("C:/test.jpg");
   int readByteNo;
   byte[] readBytes = new byte[100];
   while ((readByteNo = is.read(readBytes)) != -1 {
   ...
   }
   is.close();
```
   <br>

#### OutputStream 기본 메소드
1. write(int b)
   * 매개 변수로 주어진 int(4바이트) 값에서 끝에 있는 1바이트만 출력 스트림으로 보낸다.

2. write(byte[] b)
   * 매개값으로 주어진 바이트 배열의 모든 바이트를 출력 스트림으로 보낸다.

3. flush()
   * 출력 스트림은 내부에 작은 버퍼가 있어서 데이터가 출력되기 전에 버퍼에 쌓여있다가 순서대로 출력된다.
   * flush() 메소드는 버퍼에 잔류하고 있는 데이터를 모두 출력시키고 버퍼를 비우는 역할을 한다.

4. close()
   * OutputStream을 더 이상 사용하지 않을 경우에는 close() 메소드로 사용했던 시스템 자원을 풀어준다.

```
   OutputStream os = new FileOutputStream("C:/test.txt");
   byte[] data = "ABC".getByte();
   os.write(data); // "ABC" 모두 출력
   os.flush();
   os.close();
```

<br>

#### 파일 입출력
1. File 클래스
   * File 클래스는 파일 크기, 속성, 이름 등의 정보를 얻어내는 기능과 파일 생성 및 삭제 기능을 제공하고 있다.
   * 그리고 디렉토리를 생성하고 디렉토리에 존재하는 파일 리스트를 얻어내는 기능도 있다.
   * 그러나 파일의 데이터를 읽고 쓰는 기능은 지원하지 않아 스트림을 사용해야 한다.

2. FileInputStream
   * FileInputStream 클래스는 파일로부터 바이트 단위로 읽어들일 때 사용하는 바이트 기반 입력 스트림이다.
   * 바이트 단위로 읽기 때문에 그림, 오디오, 비디오, 텍스트 파일 등 모든 종류의 파일을 읽을 수 있다.

```
   FileInputStream fis = new FileInputStream("C:/Temp/image.fig");
```

3. FileOutputStream
   * FileOutputStream 클래스는 바이트 단위로 데이터를 파일엘 저장할 때 사용하는 바이트 기반 출력 스트림이다.
   * 바이트 단위로 저장하기 때문에 그림, 오디오, 비디오, 텍스트 파일 등 모든 종류의 파일을 저장할 수 있다.
   
```
   FileOutputStream fos = new FileOutputStream("C:/Temp/data.txt");
   FileOutputStream fos = new FileOutputStream("C:/Temp/data.txt", true);
```
   * 파일이 이미 존재할 경우, 데이터를 출력하면 파일을 덮어쓰게 되므로, 기존의 파일 내용은 사라지게 된다.
   * 기존의 파일 내용 끝에 데이터를 추가할 경우에는 FileOutputStream 생성자의 두 번째 매개값을 true로 주면 된다.

<br>

   * 콘솔 입력
      1. System.in 필드
      - 자바는 프로그램이 콘솔로부터 데이터를 입력받을 수 있도록 System 클래스의 in 정적 필드를 제공하고 있다.
      - System.in은 InputStream 타입의 필드이므로 다음과 같이 InputStream 변수로 참조가 가능하다.
      
```
   InputStream is = System.in;
   int asciiCode = is.read();
   char inputChar = (char) is.read();
```
      
   - 키보드로부터 어떤 키가 입력되었는지 확인하려면 InputStream의 read()메소드로 한 바이트를 읽으면 된다.
   - 리턴된 int 값에는 십진수 아스키 코드가 들어있다.
   - 숫자로된 아스키 코드 대신에 입력한 문자를 직접 얻고 싶다면 read() 메소드로 읽은 아스키 코드를 char로 타입 변환하면 된다.

<br>

#### 보조 스트림
   * 다른 스트림과 열결되어 여러 가지 편리한 기능을 제공해주는 스트림을 말한다.
   * 자체적으로 입출력을 수행할 수 없기 때문에 입력/출력 소스와 바로 연결되는 InputStream, Reader, OutputStream, Writer 등에 연결해서 입출력을 수행한다.
   * 보조 스트림을 생성할 때에는 자신이 연결될 스트림을 다음과 같이 생성자의 매개값으로 받는다.
   ```
   보조스트림 변수 = new 보조스트림(연결스트림)
   
   InputStream is = System.in;
   InputStreamReader reader = new InputStreamReader(is);
   ```

<br>

#### 문자 변환 보조스트림 (InputStreamReader, OutputStreamWriter)
   * 소스 스트림이 바이트 기반 스트림이면서 입출력 데이터가 문자라면 Reader와 Writer로 변환해서 사용하는 것을 고려해야 한다.
   * Reader와 Writer는 문자 단위로 입출력하기 때문에 바이트 기반 스트림보다는 편리하고, 문자셋의 종류를 지정할 수 있기 때문에 다양한 문자를 입출력할 수 있다.

   <br>

#### 성능 향상 보조스트림 (BufferedInputStream, BufferedReader, BufferedOutputStream, BufferedWriter)
   * 프로그램이 입출력 소스와 직접 작업하지 않고 중간에 메모리 버퍼와 작업함으로써 실행 성능을 향상시킬 수 있다.
   * 버퍼는 데이터가 쌓이기를 기다렸다가 꽉 차게 되면 데이터를 한번에 하드 디스크로 보냄으로써 출력 횟수를 줄여준다.

![input/output stream](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbqvSvk%2FbtrfU3bgriV%2FrzdE2FhXT6ayWpgimaJXO0%2Fimg.png)

<br>

### 자바 네트워크 기초

---

#### Socket이란
* TCP/IP 기반 네트워크 통신에서 데이터 송수신의 마지막 접점을 말한다.
* 소켓통신은 이러한 소켓을 통해 서버-클라이언트간 데이터를 주고받는 양방향 연결 지향성 통신이다.

#### 서버와 클라이언트
* 소켓통신에서는 서버와 클라이언트가 존재하며, 서버(Server)는 데이터를 제공하는 쪽을 말하며, 클라이언트(Client)는 데이터를 요청하여 제공받는 쪽을 말한다.
* 서버는 클라이언트가 요청(request)하는 내용을 처리해주고, 응답(response)를 클라이언트로 보낸다.

#### IP 주소
* 컴퓨터에도 집처럼 고유한 주소가 있는데, 이것이 IP(Internet Protocol) 주소이다.
* IP 주소는 네트워크 어답터(Lan 카드)마다 할당되는데, 한 개의 컴퓨터에 두 개의 어답터가 장착되어 있다면, 두 개의 IP주소를 할당할 수 있다.
* 연결할 상대방 컴퓨터의 IP 주소를 모른다면 프로그램들은 통신을 할 수 없다. 
* 전화번호를 모르면 114로 문의하듯이, 프로그램은 DNS(Domakn Name System)를 이용해서 연결할 컴퓨터의 IP주소를 찾는다.
* 서비스를 제공하는 대부분의 서버는 도메인 이름을 가지고 있는데, 다음과 같이 DNS에 도메인 이름으로 IP를 등록해 놓는다.

> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 도메인 이름 &nbsp; : &nbsp; 등록된 IP 주소
>
> &nbsp; &nbsp; www.naver.com &nbsp; : &nbsp;  222.122.195.5


#### 포트(Port)
* 한 대의 컴퓨터에는 다양한 서버 프로그램들이 실행될 수 있다. 
예를 들어 웹 서버, DBMS 등이 하나의 IP 주소를 갖는 컴퓨터에서 동시에 실행될 수 있다.
* IP는 컴퓨터의 네트워크 어댑터까지만 갈 수 있는 정보이기 때문에 컴퓨터 내에서 실행하는 서버를 선택하기 위해서는 포트(Port)라는 추가적인 정보가 필요하다.
* 클라이언트도 서버에서 보낸 정보를 받기 위해 포트 번호가 필요한데, 
서버와 같이 고정적인 포트번호가 아니라 운영체제가 자동으로 부여하는 동적 포트 번호를 사용한다. 

<br>

#### TCP 네트워킹
* TCP는 연결 지향적 프로토콜이다. 연결 지향 프로토콜이란 클라이언트와 서버가 연결된 상태에서 데이터를 주고받는 프로토콜을 말한다.
* 클라이언트가 연결 요청을 하고, 서버가 연결을 수락하면 통신 선로가 고정되고, 모든 데이터는 고정된 통신 선로를 통해서 순차적으로 전달된다.
* 자바는 TCP 네트워킹을 위해 java.net.ServerSocket과 java.net.Socket 클래스를 제공하고 있다.

<br>

#### 서버 구현
* 서버쪽에는 ServerSocket이라는 클래스를 이용하여 이것으로 클라이언트 연결이 올때까지 대기한다.
*  accept() 메소드가 바로 클라이언트 연결을 대기하는 메소드이고 연결이 성립될때까지 대기한다.
*  accept()는 연결이 되면 실제 통신하기 위해 Socket 객체를 넘겨주며 넘겨받은 Socket으로 실제 통신을 한다.


<br>

### 테스트 방법
   * 서버를 켜고 브라우저를 이용하여 logger로 데이터 값을 확인
   * break point를 지정해서 debug 모드로 한줄씩 실행하며 확인

### 구현 스펙 ( O / X )
O 1. HTTP/1.1 의 Host 헤더를 해석가능해야 합니다.
   * 브라우저에서의 요청을 입력 스트림인 Reader로 읽어서 Header 객체에 필요한 정보를 저장

<br>

O 2. 다음 사항을 설정 파일로 관리가능해야 합니다. (ex properties, json, xml, yml 등)

O    2-1. 구현한 웹서버의 동작 포트를 설정가능해야 합니다. (ex 80, 8080)

O    2-2. HTTP/1.1 의 Host 별로 HTTP_DOC_ROOT 를 지정가능해야 합니다.

O    2-3. HTTP/1.1 의 Host 별로 403, 404, 500 오류일 때 출력할 서버상에 있는 HTML 파일 경로를 지정가능해야 합니다.
   * .properties 파일에 port 설정값과 HTTP_DOC_ROOT를 지정하도록 함.
   * Config 클래스로 처음 한번에 읽어들이도록 함.

   <br>

O 3. HTTP 오류코드를 처리해야 합니다.

O    3-1. 403 오류의 경우 아래와 같은 Case 에서 발생해야 합니다.

O       3-1-1. 설정된 HTTP_DOC_ROOT 외 다른 범위의 파일에 접근하려 할 경우.. ex) http://127.0.0.1/../../../etc/passwd

O       3-1-2. 확장자가 허용되지 않은 mime type 인 파일을 요청받았을 때 (ex .exe .com 등)

O    3-2. 404 오류의 경우 구현한 웹서버의 HTTP_DOC_ROOT 상에 없는 자원을 요청받았을 때 발생해야 합니다.

O    3-3. 500 오류의 경우 웹서버 내부에서 로직 오류가 발생했을때 발생해야 합니다.
   * 각 에러코드의 조건에 맞게끔 RequestProcessor 클래스에서 분기문으로 처리

<br>

O 4. 요구사항 Spec 구현을 검증할 unit test code 를 작성해 주세요.
   * junit으로 테스트할 코드를 작성

<br>
