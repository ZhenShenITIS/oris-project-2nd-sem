<!DOCTYPE html>
<html lang="ru">
<head><meta charset="UTF-8"><title>Мои заметки</title></head>
<body>
<h2>Мои заметки</h2>
<a href="/notes/create">Создать заметку</a>
<#if notes?has_content>
    <ul>
        <#list notes as note>
            <li>
                <strong>${note.title}</strong> — ${note.isPublic?string("публичная", "приватная")}
                <br>${note.content}
                <br><small>${note.createdAt}</small>
                <br>
                <a href="/notes/${note.id}/edit">Редактировать</a>
                <form action="/notes/${note.id}/delete" method="post" style="display:inline">
                    <#if _csrf??><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></#if>
                    <button type="submit">Удалить</button>
                </form>
            </li>
        </#list>
    </ul>
<#else>
    <p>Заметок пока нет.</p>
</#if>
</body>
</html>
