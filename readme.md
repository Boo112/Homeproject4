# HomeProject4

Домашнее задание по лекции Домашнее задание "База данных"

Требования к програмному обеспечению:
- для сборки проекта должен бысть установлен maven
- установлена JDK 8 версии
- установлен MySQL 

Порядок запуска приложения:
- в папке с проектом обязательно должна быть папка resources

- запускаем командную строку и переходим в папку с проектом >> cd "путь до проекта"

- собираем проект: в командной строке >> mvn clean install

- в результате в папке с проектом появится папка target, в которой
будет исходный запускаемый файл >> InfoUsers-1.0-SNAPSHOT.jar

- для запуска проекта в консоле вводим >> java -jar "полный путь до файла InfoUsers-1.0-SNAPSHOT.jar"

- в результате будет создано два файла(Users.xls,Users.pdf) в папке с проектом

- для подключения к БД, нужно изменить файл resources/database.config в папке resources в
папке с проектом.

пример параметров в файле: 
                    1.dbHost = localhost
                    2.dbPort = 3306
                    dbUser = root
                    dbPassword = 12345
                    dbName = users
                    
- кодировка БД cp1251 default collation                                                          
                                                                                               







