Тестовое задание: 
  +  1. Приложение должно каждый день в 10 часов утра (кроме выходных дней) забирать курсы валют на текущую дату центробанка (http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx?WSDL , http://www.cbr.ru/scripts/Root.asp?PrtId=DWS) и сохранять в БД 
  +  2. Приложение должно иметь механизм basic http авторизации с использованием Spring Security, с ролями пользователей (создать двух пользователей с ролями админа и пользователя и пользователя с правами пользователя), разрешить админу доступ к swagger-ui например 
  +  3. Приложение для работы с данными должно использовать Spring Data , JPA 
  +  4. Приложение должно иметь профили запуска , определяемое в файле с properies : dev и prod 
  +  5. Профиль dev должно использовать in-memiry hsql для хранения данных 
  +  6. Профиль prod должно использовать postgresql 
  +  7. Реализовать REST API со следующим функционалом: 
  +  8. Запрос справочника валют к центробанку и вывод его клиенту в формате json , со следующей сортировкой: по VcharCode валюты в следующем порядке: RUB, USD, EUR, CNY, потом все остальные, которые не указаны явно в алфавитном порядке . 
  +  9. Порядок первых сортируемых валют должно задаваться в файле с properties , в порядке возрастания разделенных запятой 
  +  10. Запрос динамики курсов валют, который делает данный запрос к центробанку и возвращает клиенту в виде json 
  +  11. Запрос курсов перечня валют , который выполняет запрос к БД, и если не находит в БД на требуемую дату, делает запрос к центробанку- возвращает клиенту json (и сохраняет в бд если не было) 
  
Ответ: на выходе предоставить исходный код на Java в git репозитории. 

настройки для postgres: database::resultant, user::resultant, password::resultant

Собрать и запустить.

документация http://localhost:5550/api-guide.html
