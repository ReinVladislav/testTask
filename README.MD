## Начало работы

1. Необходимо создать базу данных PostgreSQL "testTask"
2. Создать в ней таблицы, sql скрипт для создания таблиц в файле restApp\src\main\resources\tables.sql   
3. Указать в файле restApp\src\main\resources\hibernate.properties параметры для подключения к бд:

    - hibernate.connection.url
    - hibernate.connection.username
    - hibernate.connection.password

4. Запустить Spring Boot конфигурацию SoapAppApplication
5. Запустить Spring Boot конфигурацию RestAppApplication

## Пример запроса 


POST http://localhost:8080/person

body:
```json
{
"name": "Тест",
"surname": "Тестов",
"patronymic": "Тестович",
"birthDate": "1990-01-01",
"gender": "MAN",
"document": {
"series": "1333",
"number": "112233",
"type": "PASSPORT",
"issueDate": "2020-01-01"
}
}
```
### Обращение к SOAP сервису напрямую:

POST http://localhost:8081/ws

body:
```xml
<?xml version="1.0"?>
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ex="http://soapapp.com/soapapp">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
      <ex:getPersonRequest>
         <text><![CDATA[
        <person>
            <name>Тест</name>
            <surname>Тестов</surname>
            <patronymic>Тестович</patronymic>
            <birthDate>1990-01-01</birthDate>
            <gender>MAN</gender>
            <document>
                <number>112233</number>
                <series>1333</series>
                <type>PASSPORT</type>
                <issueDate>2020-01-01</issueDate>
            </document>
        </person>
    ]]></text>
      </ex:getPersonRequest>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```


## Задание


Необходимо разработать сервис по преобразованию JSON в XML шаблонизируемым путем.
Сам сервис должен состоять из двух приложений, которые будут взаимодействовать друг с другом:

REST-приложение, обладает следующим функционалом:
POST метод принимающий JSON объект
Сохранения данных в БД
Преобразование JSON в XML (с помощью org.json)
Взаимодействие с SOAP-приложением

    Стек технологий REST-приложения:
    Spring boot
    Hibernate
    PostgreSQL
    Apache HttpClient
    Swagger
    org.json

SOAP-приложение, обладает следующим функционалом:
SOAP метод принимающий данные в CDATA
Парсинг XML
Преобразование структуры XML при помощи XSLT

    Стек технологий SOAP-приложения:
    Spring boot
    Spring WS
    XSLT
    W3C

## Общее описание как должен работать сервис:

Начальной точкой является REST-приложение, в него подается JSON (пример далее по тексту), который сохраняется в БД, далее преобразует JSON в XML и отправляет данные в SOAP-приложение. Далее SOAP-приложение преобразует XML с помощью XSLT (именно в XSLT необходимо преобразовывать данные) и возвращается в ответ. После получения данных REST-приложением в БД сохраняется ответ и возвращается пользователю. Методы обоих приложений синхронные. Также необходимо добавить в корень REST проекта файл с названием AXITEST.MD, внутри указать используемую версию библиотеки hibernate-core.

## Пример входных данных

**JSON для REST-приложения**:

```json
{
  "name": "Тест",
  "surname": "Тестов",
  "patronymic": "Тестович",
  "birthDate": "1990-01-01",
  "gender": "MAN",
  "document": {
    "series": "1333",
    "number": "112233",
    "type": "PASSPORT",
    "issueDate": "2020-01-01"
  }
}
```
**Входные данные SOAP-приложения**:
```xml
<person>
    <name>Тест</name>
    <surname>Тестов</surname>
    <patronymic>Тестович</patronymic>
    <birthDate>1990-01-01</birthDate>
    <gender>MAN</gender>
    <document>
        <series>1333</series>
        <number>112233</number>
        <type>PASSPORT</type>
        <issueDate>2020-01-01</issueDate>
    </document>
</person>
```
 
 
**Что ожидается на выходе**:

```xml
<person name="Тест" surname="Тестов" patronymic="Тестович" birthDate="01.01.1990" gender="MAN">
    <document series="1333" number="112233" type="PASSPORT" issueDate="01.01.2020"/>
</person>
```


