# Iidea-hse-project
## Правила добавления коммитов
1. Создать ветку формата "_имя_-..."
2. Записать в неё свои коммиты
3. Создать Merge Request в master

Поскольку каждый отвечает за свою часть, не будет возникать Merge Conflict при Merge.
## Функционал
[Описание проекта](https://docs.google.com/document/d/1s6dP99IzJYPcG3f4WYiCwGJAqQC3J91okkQHRRr28us/edit?usp=sharing)
## Инструкция
Основной код лежит в директории app/src/main, тесты в app/src/androidTest и app/src/test соответственно. Проект выполняется в Android Studio.

## Структура проекта
### Activity 1
Это Activity, в котором будет находится вход/регистрация. К этому Activity относятся файлы MainActivity--точка входа, RegistrationFragment обрабатывает действия со слоем fragment_registration.xml. Это Activity представляется на экране при помощи файлов activity_main.xml, content_main.xml и nav_graph.xml из папки navigation.

### Activity 2
Это основное Activity, в котором будет лежать практически весь остальной проект. Главный файл для него MainScreenActivity, показ на экране с помощью activity_mainscreen.xml, content_mainscreen.xml и nav_graph_mainscreen.xml
