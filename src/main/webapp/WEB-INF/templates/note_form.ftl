<!DOCTYPE html>
<html lang="ru">
<head><meta charset="UTF-8"><title>Заметка</title></head>
<body>
<h2><#if note??>Редактирование заметки<#else>Новая заметка</#if></h2>

<form action="<#if note??>/notes/${note.id}/edit<#else>/notes/create</#if>" method="post">
    <#if _csrf??><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></#if>

    <div>
        <label>Заголовок:
            <input type="text" name="title" value="<#if note??>${note.title}</#if>" required/>
        </label>
    </div>
    <div>
        <label>Содержимое:
            <textarea name="content" required><#if note??>${note.content}</#if></textarea>
        </label>
    </div>
    <div>
        <label>
            <input type="checkbox" name="isPublic" value="true"
                   <#if note?? && note.isPublic>checked</#if>/>
            Публичная
        </label>
    </div>

    <button type="submit">Сохранить</button>
</form>
<a href="/notes">← Назад</a>
</body>
</html>
