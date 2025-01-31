ТЗ:
Консольное CRUD приложение, которое имеет следующие сущности:
Writer (id, firstName, lastName, List<Post> posts)
Post (id, content, created, updated, List<Label> labels)
Label (id, name)
PostStatus (enum ACTIVE, UNDER_REVIEW, DELETED)

Каждая сущность имеет поле Status. В момент удаления, мы не удаляем запись из файла, а меняем её статус на DELETED.

В качестве хранилища данных используются текстовые файлы:
writers.json, posts.json, labels.json

Пользователь в консоли имеет возможность создания, получения, редактирования и удаления данных.
