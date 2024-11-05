# Создание простого API для управления библиотекой книг.

## Задача 1: Создание простого API для управления библиотекой книг
Создайте RESTful API на Spring Boot для управления библиотекой книг. API должен поддерживать следующие операции:

1. Получение списка всех книг.
2. Получение книги по идентификатору.
3. Добавление новой книги.
4. Обновление информации о книге.
5. Удаление книги по идентификатору.

Каждая книга должна иметь следующие поля: 
- `id`
- `title`
- `author`
- `publishedDate`

## Задача 2*: Добавьте в RESTful API функционал отправки и получения уведомлений при изменении данных о книгах с использованием RabbitMQ
RabbitMQ должен быть настроен для отправки сообщений в очередь при создании, обновлении или удалении книги.

- Создание новой книги
- Обновление информации о книге
- Удаление книги

Сервис-подписчик внутри приложения должен слушать очередь RabbitMQ и обрабатывать полученные сообщения, выполняя соответствующие операции:

- Сохранение новой книги в базу данных.
- Обновление существующей книги по ID.
- Удаление книги из базы данных по ID.

## Дополнительная информация
Задание два находится во второй ветке, rabbitMQ поднимается с помощью Docker, postman конфиг в корне. 