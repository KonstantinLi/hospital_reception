<h1 align="center"><b>Реєстратура лікарні "MedEra"</b></h1>

<p align="center">

<img alt="GitHub issues" src="https://img.shields.io/github/issues-raw/KonstantinLi/hospital_reception?style=plastic">
<img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/KonstantinLi/hospital_reception?style=plastic">
<img alt="GitHub contributors" src="https://img.shields.io/github/contributors/KonstantinLi/hospital_reception?style=plastic">
<img alt="GitHub watchers" src="https://img.shields.io/github/watchers/KonstantinLi/hospital_reception?style=social">
</p>

---

## Опис
**MedEra** - це програма, що підвищує ефективність роботи реєстратури обласних центрів первинної медико-санітарної допомоги.
У базі зберігається інформація про графік роботи спеціалістів у відповідних закладах, персональна інформація про пацієнтів, а
також наявний функціонал їх запису до лікарів. 

---

## Функціонал
+ Програма містить конфіденційну інформацію, тому реалізований процес авторизації адміністраторів. 
    > :exclamation: Перед авторизацією весь користувацький інтерфейс заблокований.

<img src="./readme_assets/raw.png" alt="search process" style="width: 100%">
<img src="./readme_assets/main.png" alt="search process" style="width: 100%">
<img src="./readme_assets/auth.png" alt="search process" style="width: 100%">

+ Відображення розкладу спеціаліста.

<img src="./readme_assets/schedule.png" alt="search process" style="width: 100%">

+ Область, що знаходиться на місці зайнятого пацієнтом часу, є клікабельною. При її натисканні відображається персональна інформація про пацієнта.

<img src="./readme_assets/info.png" alt="search process" style="width: 100%">

+ Запис на консультацію.

<img src="./readme_assets/record.png" alt="search process" style="width: 100%">

+ Створення нового пацієнта та занесення його в базу.

<img src="./readme_assets/new.png" alt="search process" style="width: 100%">

+ База даних про всіх пацієнтів з можливістю фільтрації даних.

<img src="./readme_assets/table.png" alt="search process" style="width: 100%">

---

## Стек технологій
+ Java 17
+ JDBC / MySQL
+ Maven
+ JavaFX (Controls, FXML, BootstrapFX, JFXtras) / SceneBuilder
+ Log4J
+ Javadoc