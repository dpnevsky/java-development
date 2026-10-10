# Loan Origination System

Учебный проект на Java — прототип банковского приложения для оформления кредита с микросервисной архитектурой.

## Исходный код

**Исходный код опубликован в `main`.** На главной странице репозитория доступны все сервисы, тесты, Docker Compose и конфигурация CI. Разработка велась в `develop`, затем эта ветка была объединена с `main` с сохранением истории коммитов.

Чтобы получить исходный код:

```bash
git clone https://github.com/dpnevsky/loan-origination-system.git
cd loan-origination-system
```

## Процесс разработки

Проект разрабатывался поэтапно. Для отдельных микросервисов использовались ветки `feature/...`, а общей веткой для объединения результатов была `develop`.

1. В отдельных feature-ветках реализовывались сервисы «Калькулятор», «Сделка», «Заявка», «Досье» и API Gateway.
2. Завершённые этапы объединялись в `develop` через pull request. История PR отражает последовательное добавление сервисов и развитие приложения.
3. Конфигурация CI, сборка Docker-образов и запуск приложения через Docker Compose разрабатывались в ветке `devops`, затем были включены в `develop`.
4. Ветка `develop` объединена с `main` обычным merge. Основная ветка теперь содержит приложение вместе с документацией; переключать ветку для просмотра кода не нужно.

Feature-ветки и `devops` сохранены вместе с историей коммитов и pull request: по ним можно проследить отдельные этапы разработки.

## Ветки репозитория

| Ветка | Назначение | Интеграция |
| --- | --- | --- |
| [`main`](https://github.com/dpnevsky/loan-origination-system/tree/main) | Исходники сервисов, тесты, инфраструктура и описание кредитного процесса. | Основная версия приложения. |
| [`develop`](https://github.com/dpnevsky/loan-origination-system/tree/develop) | Ветка интеграции этапов исходной разработки. | Объединена в `main`; сохранена для истории. |
| [`feature/calculator/create`](https://github.com/dpnevsky/loan-origination-system/tree/feature/calculator/create) | Разработка сервиса «Калькулятор». | Объединена в `develop` через [PR #2](https://github.com/dpnevsky/loan-origination-system/pull/2). |
| [`feature/deal/create`](https://github.com/dpnevsky/loan-origination-system/tree/feature/deal/create) | Разработка сервиса «Сделка» и общего модуля `core`. | Объединена в `develop` через [PR #3](https://github.com/dpnevsky/loan-origination-system/pull/3). |
| [`feature/statement/create`](https://github.com/dpnevsky/loan-origination-system/tree/feature/statement/create) | Разработка сервиса «Заявка». | Объединена в `develop` через [PR #4](https://github.com/dpnevsky/loan-origination-system/pull/4). |
| [`feature/dossier/create`](https://github.com/dpnevsky/loan-origination-system/tree/feature/dossier/create) | Разработка сервиса «Досье». | Объединена в `develop` через [PR #5](https://github.com/dpnevsky/loan-origination-system/pull/5). |
| [`feature/gateway/create`](https://github.com/dpnevsky/loan-origination-system/tree/feature/gateway/create) | Разработка API Gateway. | Объединена в `develop` через [PR #6](https://github.com/dpnevsky/loan-origination-system/pull/6). |
| [`devops`](https://github.com/dpnevsky/loan-origination-system/tree/devops) | CI, сборка Docker-образов и Docker Compose. | Объединена в `develop` через [PR #7](https://github.com/dpnevsky/loan-origination-system/pull/7). |
| [`transactionbug`](https://github.com/dpnevsky/loan-origination-system/tree/transactionbug) | Отдельный эксперимент с отключением `@Transactional`. | Изменение не объединено в `develop`. |

## Технологии

- Java 17, Spring Boot 3.3.x.
- PostgreSQL, Spring Data JPA, Liquibase.
- Apache Kafka.
- Swagger / OpenAPI.
- JUnit, Mockito.
- Docker, Docker Compose.
- GitHub Actions, JaCoCo; конфигурации Codecov и SonarCloud.

## Структура проекта

| Модуль | Назначение | Порт |
| --- | --- | --- |
| [`gateway`](gateway) | Входные REST endpoints и маршрутизация к сервисам. | 8086 |
| [`statement`](statement) | Прескоринг заявки и выбор кредитного предложения. | 8084 |
| [`deal`](deal) | Хранение заявки и кредита, управление этапами оформления. | 8082 |
| [`calculator`](calculator) | Кредитные предложения, скоринг, ПСК и график платежей. | 8080 |
| [`dossier`](dossier) | Обработка Kafka-событий и email-уведомления клиенту. | 8088 |
| [`core`](core) | Общие DTO, типы и утилиты; библиотека для сервисов. | — |

## Локальный запуск

Нужны JDK 17, Maven, Docker и Docker Compose. Сначала соберите общий модуль: Dockerfiles сервисов используют его JAR.

```bash
mvn -f core/pom.xml clean install
docker compose up --build -d
docker compose ps
```

Перед запуском задайте собственные SMTP-параметры в `dossier/src/main/resources/application.yml`; без доступного SMTP почтовые этапы процесса не пройдут. Compose поднимает PostgreSQL, Kafka с ZooKeeper и сервисы приложения. Kafka UI доступен на `http://localhost:8085`, входной Gateway — на `http://localhost:8086`.

## Что отрабатывалось в проекте

- Разработка REST API на Spring Boot.
- Работа с базой данных и миграциями.
- Разделение приложения на микросервисы.
- Синхронное взаимодействие сервисов и асинхронный обмен событиями через Kafka.
- Документирование API с помощью Swagger / OpenAPI.
- Тестирование, настройка CI и контейнеризация приложения.

## Архитектура

![Архитектура приложения](https://github.com/user-attachments/assets/5e2717d0-110e-455b-8482-c77efe4c05ba)

## Сценарий оформления кредита

![Кредитный процесс](https://github.com/user-attachments/assets/4fd470a6-87ed-406d-a397-38c17929b6c3)

1. Пользователь отправляет заявку на кредит.
2. Сервис «Заявка» выполняет предварительную проверку — прескоринг. Если заявка проходит проверку, она сохраняется в сервисе «Сделка» и передаётся в сервис «Калькулятор».
3. «Калькулятор» формирует четыре кредитных предложения (`LoanOffer`) с разными сочетаниями условий страхования и участия в зарплатном проекте либо возвращает отказ. Предложения передаются пользователю через сервис «Заявка».
4. Пользователь выбирает предложение. Запрос проходит через сервис «Заявка» в сервис «Сделка», где сохраняются данные заявки и кредита.
5. Сервис «Досье» отправляет клиенту письмо с предварительным одобрением и предложением завершить оформление.
6. Клиент передаёт в сервис «Сделка» дополнительные сведения, включая данные о работодателе и регистрации. «Калькулятор» выполняет скоринг и рассчитывает параметры кредита: полную стоимость кредита (ПСК) и график платежей. «Сделка» сохраняет обновлённую заявку и данные кредита на основе `CreditDto`, устанавливая кредиту статус `CALCULATED`.
7. Сервис «Досье» отправляет письмо с одобрением или отказом. При одобрении письмо содержит ссылку для запроса документов.
8. Клиент запрашивает документы. «Досье» отправляет их по электронной почте вместе со ссылкой для подтверждения согласия с условиями.
9. Клиент может отказаться от условий или согласиться. При согласии «Досье» отправляет код подтверждения и ссылку для подписания документов. Полученный код клиент передаёт в сервис «Сделка».
10. «Сделка» проверяет код. Если он совпадает с отправленным, кредит получает статус `ISSUED`, а заявка — `CREDIT_ISSUED`.

## Скоринг

![Схема скоринга](https://github.com/user-attachments/assets/93ac1473-bb38-4108-81ad-8d9b8ac557f9)

## API

![Схема API](https://github.com/user-attachments/assets/af88f78f-01dc-48d2-b43d-ac0722802d9b)

## Взаимодействие сервисов

![Последовательность взаимодействия сервисов](https://github.com/user-attachments/assets/0de0279d-259f-4dcb-a029-dce17f23195b)
