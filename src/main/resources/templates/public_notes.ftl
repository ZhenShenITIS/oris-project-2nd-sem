<!DOCTYPE html>
<html lang="ru">
<head><meta charset="UTF-8"><title>Публичные заметки</title></head>
<body>
<h2>Публичные заметки</h2>
<#if notes?has_content>
    <ul>
        <#list notes as note>
            <li>
                <strong>${note.title}</strong> — автор: ${note.author.name}
                <br>${note.content}
                <br><small>${note.createdAt}</small>
            </li>
        </#list>
    </ul>
<#else>
    <p>Нет публичных заметок.</p>
</#if>
</body>
</html>
