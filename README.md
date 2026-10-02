# Java Development

Учебный проект на Java — прототип банковского приложения для оформления кредита с микросервисной архитектурой.

## Исходный код

**Основная разработка велась в ветке [`develop`](https://github.com/dpnevsky/java-development/tree/develop).** В ней находятся исходники сервисов, тесты, Docker Compose и конфигурация CI. Ветка `main` содержит описание проекта.

Чтобы получить исходный код:

```bash
git clone --branch develop https://github.com/dpnevsky/java-development.git
cd java-development
```

## Процесс разработки

Проект разрабатывался поэтапно. Для отдельных микросервисов использовались ветки `feature/...`, а общей веткой для объединения результатов была `develop`.

1. В отдельных feature-ветках реализовывались сервисы «Калькулятор», «Сделка», «Заявка», «Досье» и API Gateway.
2. Завершённые этапы объединялись в `develop` через pull request. История PR отражает последовательное добавление сервисов и развитие приложения.
3. Конфигурация CI, сборка Docker-образов и запуск приложения через Docker Compose разрабатывались в ветке `devops`, затем были включены в `develop`.
4. В `main` размещена документация проекта. Для просмотра исходников и работы с приложением следует использовать `develop`.

Feature-ветки и `devops` сохранены вместе с историей коммитов и pull request: по ним можно проследить отдельные этапы разработки.

## Ветки репозитория

| Ветка | Назначение | Интеграция |
| --- | --- | --- |
| [`main`](https://github.com/dpnevsky/java-development/tree/main) | Описание проекта, архитектура и кредитный процесс. | Документация. |
| [`develop`](https://github.com/dpnevsky/java-development/tree/develop) | Общая версия приложения, объединяющая сервисы, тесты и инфраструктуру. | Основная ветка с исходным кодом. |
| [`feature/calculator/create`](https://github.com/dpnevsky/java-development/tree/feature/calculator/create) | Разработка сервиса «Калькулятор». | Объединена в `develop` через [PR #2](https://github.com/dpnevsky/java-development/pull/2). |
| [`feature/deal/create`](https://github.com/dpnevsky/java-development/tree/feature/deal/create) | Разработка сервиса «Сделка» и общего модуля `core`. | Объединена в `develop` через [PR #3](https://github.com/dpnevsky/java-development/pull/3). |
| [`feature/statement/create`](https://github.com/dpnevsky/java-development/tree/feature/statement/create) | Разработка сервиса «Заявка». | Объединена в `develop` через [PR #4](https://github.com/dpnevsky/java-development/pull/4). |
| [`feature/dossier/create`](https://github.com/dpnevsky/java-development/tree/feature/dossier/create) | Разработка сервиса «Досье». | Объединена в `develop` через [PR #5](https://github.com/dpnevsky/java-development/pull/5). |
| [`feature/gateway/create`](https://github.com/dpnevsky/java-development/tree/feature/gateway/create) | Разработка API Gateway. | Объединена в `develop` через [PR #6](https://github.com/dpnevsky/java-development/pull/6). |
| [`devops`](https://github.com/dpnevsky/java-development/tree/devops) | CI, сборка Docker-образов и Docker Compose. | Объединена в `develop` через [PR #7](https://github.com/dpnevsky/java-development/pull/7). |
| [`transactionbug`](https://github.com/dpnevsky/java-development/tree/transactionbug) | Отдельный эксперимент с отключением `@Transactional`. | Изменение не объединено в `develop`. |

## Технологии

- Java 17, Spring Boot 3.3.x.
- PostgreSQL, Spring Data JPA, Liquibase.
- Apache Kafka.
- Swagger / OpenAPI.
- JUnit, Mockito.
- Docker, Docker Compose.
- GitHub Actions, Codecov, SonarCloud.

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

