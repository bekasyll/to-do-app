# To-Do List Application (Spring Boot + JWT)

Это учебный проект на Spring Boot с REST API, JWT аутентификацией и PostgreSQL.  

## Функционал
- Регистрация и логин пользователей с JWT
- CRUD операции для задач
- Фильтрация задач по статусу (`completed`)
- Каждая задача принадлежит конкретному пользователю
- Валидация данных через DTO и кастомный валидатор
- Глобальный обработчик ошибок
- Stateless Spring Security с JWT

## Технологии
- Java 17
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- ModelMapper
- Maven

## Настройка
1. Склонируйте репозиторий:
```bash
git clone https://github.com/bekasyll/ToDoApp.git
cd ToDo List
````

2. Создайте базу данных PostgreSQL и пользователя:

```sql
CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(150),
    completed BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    user_id INTEGER REFERENCES users(id)
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    role VARCHAR(50) NOT NULL,
    active_token VARCHAR NOT NULL
);
```

3. Скопируйте `application.properties.example` в `application.properties` и настройте:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_app
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt-secret=your_secret
```

4. Соберите и запустите проект:

```bash
mvn clean install
mvn spring-boot:run
```

## Эндпойнты

* `POST /auth/registration` — регистрация
* `POST /auth/login` — логин
* `GET /api/tasks` — список задач текущего пользователя
* `GET /api/tasks/{id}` — получение задачи по ID
* `POST /api/tasks` — создать задачу
* `PUT /api/tasks/{id}` — обновить задачу
* `DELETE /api/tasks/{id}` — удалить задачу

```
Все `/api/tasks/**` требуют JWT в `Authorization` заголовке.
```

## Тестирование

* Используйте Postman или curl
* Добавьте JWT в заголовок:

```
Authorization: Bearer <токен>
```

## Дополнительно

* Фильтрация задач: `/api/tasks?completed=true|false`
* DTO разделены для Request/Response
* Для проверки уникальности имени пользователя реализован кастомный валидатор AppUserValidator.