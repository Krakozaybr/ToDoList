# Тестовое задание для Simbirsoft
Основной функционал показан на .gif ниже

![Example of using](/images/preview.gif)

## Примечания
* Для корректной реализации BottomSheet (правильного позиционирования и исправления бага с рестором состояния) под нужды приложения пришлось несколько переписать BottomSheetScaffold
* Кнопки календаря Today и Checked скроллят календарь до месяца соответствующей даты
## Что можно добавить
* Изменение года в календаре главного экрана
* Временный Snackbar для возвращения последнего удаленного дела
* Предупреждение о несохраненных данных на экране редактирования
## Что использовалось
* Не самая сложная реализация MVI на экране редактирования/сохранения
* MVVM на экране списка
* Clean Architecture
* Dagger + Hilt
* Jetpack Compose
* Material Theme + поддержка Dynamic colors
* Room
* Voyager для навигации (на начальном этапе - Compose Navigation)
* Kotlin Coroutines
* Kotlin Flows
* Для календаря Calendar от kizitonwose
* Для диалогов времени Sheets-Compose-Dialogs от maxkeppeler
* JUnit4 + Mockito
